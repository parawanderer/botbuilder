package gg.botbuilder.bot.clauses;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import gg.botbuilder.bot.clauses.tree.*;
import gg.botbuilder.bot.conf.ConfigParsingException;

import java.io.IOException;
import java.util.*;

public class ClauseTreeDeserializer extends StdDeserializer<ClauseTreeRoot> {
    public ClauseTreeDeserializer() {
        super(ClauseTreeRoot.class);
    }

    private static final Map<String, CLAUSE_LOGICAL_OPERATOR> LOGICAL_OPERATORS = Map.of(
            CLAUSE_LOGICAL_OPERATOR.AND.toLowerCase(), CLAUSE_LOGICAL_OPERATOR.AND,
            CLAUSE_LOGICAL_OPERATOR.OR.toLowerCase(), CLAUSE_LOGICAL_OPERATOR.OR,
            CLAUSE_LOGICAL_OPERATOR.NOT.toLowerCase(), CLAUSE_LOGICAL_OPERATOR.NOT
    );

    private static final Map<String, CLAUSE_FUNCTION> FUNCTIONS = Map.of(
            CLAUSE_FUNCTION.CONTAINS.toLowerCase(), CLAUSE_FUNCTION.CONTAINS,
            CLAUSE_FUNCTION.EQ.toLowerCase(), CLAUSE_FUNCTION.EQ,
            CLAUSE_FUNCTION.ENDS_WITH.toLowerCase(), CLAUSE_FUNCTION.ENDS_WITH,
            CLAUSE_FUNCTION.STARTS_WITH.toLowerCase(), CLAUSE_FUNCTION.STARTS_WITH,
            CLAUSE_FUNCTION.REGEX.toLowerCase(), CLAUSE_FUNCTION.REGEX
    );

    @Override
    public ClauseTreeRoot deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode root = p.getCodec().readTree(p);
        if (root.isObject()) {
            IConditionNode rootNode = this.processRoot(root);
            return new ClauseTreeRoot(rootNode);
        }
        throw new ConfigParsingException("Invalid expression tree (root cannot be non-object)");
    }

    private IConditionNode processRoot(JsonNode root) {
        // two options: either a wrapping container expression (AND/OR/NOT) or a singular expression
        return this.processNode(root);
    }

    private IConditionNode processNode(JsonNode node) {
        if (node.size() != 1) {
            throw new ConfigParsingException("Invalid expression tree (node cannot have more than one top-level key-value entry (this defines the operation))");
        }

        var rootField = node.fields().next();
        String key = rootField.getKey();
        JsonNode value = rootField.getValue();

        if (isLogicalOp(key)) {
            return processLogicalOperation(getLogicalOp(key), value);
        }
        if (isFunction(key)) {
            return processFunction(getFunction(key), value);
        }

        throw new ConfigParsingException("Could not parse node expression, '" + key + "' is not an allowed operation");
    }


    private IConditionNode processLogicalOperation(CLAUSE_LOGICAL_OPERATOR op, JsonNode node) {
        // a single child or a list of them (with NOT only allowing a single item in general...)
        if (!node.isContainerNode()) {
            throw new ConfigParsingException("Expected child of logical expression to be single expression OR list of expressions");
        }

        return switch(op) {
            case AND -> this.processAnd(node);
            case OR -> this.processOr(node);
            case NOT -> this.processNot(node);
        };
    }

    private IConditionNode processAnd(JsonNode node) {
        return this.processLogicalListExpression(CLAUSE_LOGICAL_OPERATOR.AND, node);
    }

    private IConditionNode processOr(JsonNode node) {
        return this.processLogicalListExpression(CLAUSE_LOGICAL_OPERATOR.OR, node);
    }

    private IConditionNode processLogicalListExpression(CLAUSE_LOGICAL_OPERATOR expr, JsonNode node) {
        Iterator<JsonNode> iter;
        if (node.isArray()) {
            if (node.size() == 0)
                throw new ConfigParsingException(expr + " expression must have at least one inner expression or function");
            iter = node.elements();
        } else {
            iter  = List.of(node).iterator();
        }

        List<IConditionNode> clauses = new ArrayList<>();
        while (iter.hasNext()) {
            JsonNode child = iter.next();
            final IConditionNode childClause = this.processNode(child);
            clauses.add(childClause);
        }

        return new LogicalExpressionNode(expr, clauses);
    }

    private IConditionNode processNot(JsonNode node) {
        JsonNode child;
        if (node.isArray()) {
            if (node.size() != 1)
                throw new ConfigParsingException("Not operation should hold only a single nested expression");

            child = node.elements().next();
        } else {
            child  = node;
        }

        IConditionNode childNode = this.processNode(child);
        return new LogicalExpressionNode(CLAUSE_LOGICAL_OPERATOR.NOT, List.of(childNode));
    }

    private IConditionNode processFunction(CLAUSE_FUNCTION function, JsonNode node) {
        // body is just an ordered list of the input values
        if (!node.isArray() || node.size() != 2)
            throw new ConfigParsingException("Function " + function + " must be provided two and exactly 2 input values. " +
                    "The first is the subject (json query expression) and the second is the second argument given to the function. These must be strings");

        ArrayNode elements = (ArrayNode) node;
        JsonNode subject = elements.get(0);
        JsonNode other = elements.get(1);

        if (!subject.isTextual())
            throw new ConfigParsingException("Function " + function + " must be provided the 1st argument as a json query expression (string)");

        if (other.isObject()) {
            throw new ConfigParsingException("Function " + function + " must be provided a non-object (value type) as its second argument. Must be a value type");
        }

        if (other.isArray()) {
            throw new ConfigParsingException("Function " + function + " does not accept a list as its second argument. Must be a value type");
        }

        return new FunctionExpressionNode(function, subject.textValue(), other.asText());
    }

    private static boolean isLogicalOp(String key) {
        return LOGICAL_OPERATORS.containsKey(key);
    }

    private static CLAUSE_LOGICAL_OPERATOR getLogicalOp(String key) {
        return LOGICAL_OPERATORS.get(key);
    }

    private static boolean isFunction(String key) {
        return FUNCTIONS.containsKey(key);
    }

    private static CLAUSE_FUNCTION getFunction(String key) {
        return FUNCTIONS.get(key);
    }
}
