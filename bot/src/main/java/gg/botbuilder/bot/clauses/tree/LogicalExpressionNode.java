package gg.botbuilder.bot.clauses.tree;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class LogicalExpressionNode implements IConditionNode {
    @Getter
    private final CLAUSE_LOGICAL_OPERATOR expression;
    @Getter
    @NonNull
    private final List<IConditionNode> children;

    @Override
    public boolean accept(ICoditionTreeEvaluatorVisitor visitor) {
        return visitor.visitLogicalExpression(this);
    }

    public static LogicalExpressionNode and(@NonNull List<IConditionNode> children) {
        if (children.size() == 0)
            throw new ClauseDefinitionException("AND statement must contain one or more children");

        return new LogicalExpressionNode(CLAUSE_LOGICAL_OPERATOR.AND, children);
    }

    public static LogicalExpressionNode and(@NonNull IConditionNode... children) {
        if (children.length == 0)
            throw new ClauseDefinitionException("AND statement must contain one or more children");

        return new LogicalExpressionNode(CLAUSE_LOGICAL_OPERATOR.AND, List.of(children));
    }

    public static LogicalExpressionNode or(@NonNull List<IConditionNode> children) {
        if (children.size() == 0)
            throw new ClauseDefinitionException("OR statement must contain one or more children");

        return new LogicalExpressionNode(CLAUSE_LOGICAL_OPERATOR.OR, children);
    }

    public static LogicalExpressionNode or(@NonNull IConditionNode... children) {
        if (children.length == 0)
            throw new ClauseDefinitionException("OR statement must contain one or more children");

        return new LogicalExpressionNode(CLAUSE_LOGICAL_OPERATOR.OR, List.of(children));
    }

    public static LogicalExpressionNode not(@NonNull IConditionNode child) {
        return new LogicalExpressionNode(CLAUSE_LOGICAL_OPERATOR.NOT, List.of(child));
    }
}
