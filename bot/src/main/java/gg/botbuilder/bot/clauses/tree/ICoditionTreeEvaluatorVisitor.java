package gg.botbuilder.bot.clauses.tree;

public interface ICoditionTreeEvaluatorVisitor {
    boolean visitExpression(FunctionExpressionNode expression);
    boolean visitLogicalExpression(LogicalExpressionNode expression);
}
