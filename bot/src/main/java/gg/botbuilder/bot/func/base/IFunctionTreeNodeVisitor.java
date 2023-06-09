package gg.botbuilder.bot.func.base;

public interface IFunctionTreeNodeVisitor {
    Object visit(FunctionFunctionTreeNode functionFunctionTreeNode);
    Object visit(ConstantFunctionTreeNode constantFunctionTreeNode);
    Object visit(VariableFunctionTreeNode variableFunctionTreeNode);
}
