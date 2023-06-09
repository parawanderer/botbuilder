package gg.botbuilder.bot.clauses.eval;

import gg.botbuilder.bot.clauses.tree.*;
import gg.botbuilder.bot.jsonpath.JsonPathQueryException;
import gg.botbuilder.bot.jsonpath.IJsonPathQueryableData;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class ClauseTreeEvaluatorVisitor implements ICoditionTreeEvaluatorVisitor {
    @NonNull
    private final IJsonPathQueryableData context;

    public boolean evaluate(ClauseTreeRoot root) {
        return root.getRoot().accept(this);
    }

    @Override
    public boolean visitExpression(FunctionExpressionNode expression) {
        CLAUSE_FUNCTION operator = expression.getOperator();
        String subjectQuery = expression.getSubjectJsonQuery();
        String otherValue = expression.getFunctionInput();

        List<String> subjects = this.evaluateQuery(subjectQuery);
        if (subjects.size() == 0) {
            throw new ClauseTreeEvaluationException("Subject of function " + operator + " was empty");
        }

        var conf = getHandler(operator);
        var fn = conf.fn();

        if (!conf.allowMultiInput() && subjects.size() > 1) {
            throw new ClauseTreeEvaluationException("Function " + operator + " is not able to evaluate multi inputs");
        }

        // multi means any of the subjects
        for (var subject : subjects) {
            if (fn.evaluate(subject, otherValue)) return true;
        }

        return false;
    }

    @Override
    public boolean visitLogicalExpression(LogicalExpressionNode expression) {
        // since you can only ever have 3 of these a switch will suffice
        return switch (expression.getExpression()) {
            case OR -> this.processOrExpression(expression);
            case AND -> this.processAndExpression(expression);
            case NOT -> this.processNotExpression(expression);
        };
    }

    private List<String> evaluateQuery(String query) {
        // some logic to use context to evaluate query here...
        try {
            return this.context.query(query);
        } catch (JsonPathQueryException e) {
            throw new ClauseTreeEvaluationException("Could not find '" + query + "' in current context", e);
        }
    }

    private boolean processNotExpression(LogicalExpressionNode expression) {
        if (expression.getChildren().size() != 1) {
            throw new ClauseTreeEvaluationException("Invalid target clause of NOT expression");
        }
        IConditionNode node = expression.getChildren().get(0);
        return !node.accept(this);
    }

    private boolean processAndExpression(LogicalExpressionNode expression) {
        if (expression.getChildren().size() == 0) {
            throw new ClauseTreeEvaluationException("Invalid target clause of AND expression");
        }
        for (var node: expression.getChildren()) {
            if (!node.accept(this)) return false;
        }
        return true;
    }

    private boolean processOrExpression(LogicalExpressionNode expression) {
        if (expression.getChildren().size() == 0) {
            throw new ClauseTreeEvaluationException("Invalid target clause of OR expression");
        }
        for (var node: expression.getChildren()) {
            if (node.accept(this)) return true;
        }
        return false;
    }

    interface ClauseEvaluatorFunction {
        boolean evaluate(String input, String other);
    }

    static record EvaluatorConfig(ClauseEvaluatorFunction fn, boolean allowMultiInput) {}

    private static boolean evalEquals(String one, String other) {
        return one.equals(other);
    }

    private static boolean evalContains(String one, String other) {
        return one.contains(other);
    }

    private static boolean evalStartsWith(String one, String other) {
        return one.indexOf(other) == 0;
    }

    private static boolean evalEndsWith(String one, String other) {
        if (other.length() == 0) return true;
        int index = one.indexOf(other);
        if (index == -1) return false;
        int otherLength = other.length();
        return index + otherLength == one.length();
    }

    private static boolean evalRegex(String input, String regex) {
        var pattern = Pattern.compile(regex);
        var matcher = pattern.matcher(input);
        return matcher.find();
    }

    public static EvaluatorConfig getHandler(CLAUSE_FUNCTION expr) {
        return switch(expr) {
            case EQ -> new EvaluatorConfig(ClauseTreeEvaluatorVisitor::evalEquals, true);
            case CONTAINS -> new EvaluatorConfig(ClauseTreeEvaluatorVisitor::evalContains, true);
            case STARTS_WITH -> new EvaluatorConfig(ClauseTreeEvaluatorVisitor::evalStartsWith, false);
            case ENDS_WITH -> new EvaluatorConfig(ClauseTreeEvaluatorVisitor::evalEndsWith, false);
            case REGEX -> new EvaluatorConfig(ClauseTreeEvaluatorVisitor::evalRegex, false);
            default -> throw new ClauseTreeEvaluationException("No evaluation available for function " + expr);
        };
    }
}
