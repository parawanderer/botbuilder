package gg.botbuilder.bot.clauses;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import gg.botbuilder.bot.clauses.tree.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClauseTreeDeserializerTest {
    @Test
    void givenClauseTree_whenDeserialized_thenResultExpected() throws JsonProcessingException {
        var yaml =
"""
or:
  - eq:
      - "$.one"
      - "first"
  - eq:
      - "$.one"
      - "second"
""";

        ObjectMapper mapper = new YAMLMapper();
        var tree = mapper.readTree(yaml);
        ClauseTreeRoot root = mapper.convertValue(tree, ClauseTreeRoot.class);

        assertNotNull(root);
        assertNotNull(root.getRoot());
        IConditionNode rootNode = root.getRoot();
        assertInstanceOf(LogicalExpressionNode.class, rootNode);
        LogicalExpressionNode rootNodeCasted = (LogicalExpressionNode) rootNode;
        assertEquals(CLAUSE_LOGICAL_OPERATOR.OR, rootNodeCasted.getExpression());
        assertNotNull(rootNodeCasted.getChildren());
        assertEquals(2, rootNodeCasted.getChildren().size());

        IConditionNode child1 = rootNodeCasted.getChildren().get(0);
        assertNotNull(child1);
        assertInstanceOf(FunctionExpressionNode.class, child1);
        var castedChild1 = (FunctionExpressionNode) child1;
        assertEquals("$.one", castedChild1.getSubjectJsonQuery());
        assertEquals("first", castedChild1.getFunctionInput());

        IConditionNode child2 = rootNodeCasted.getChildren().get(1);
        assertNotNull(child2);
        assertInstanceOf(FunctionExpressionNode.class, child2);
        var castedChild2 = (FunctionExpressionNode) child2;
        assertEquals("$.one", castedChild2.getSubjectJsonQuery());
        assertEquals("second", castedChild2.getFunctionInput());
    }


}
