package gg.botbuilder.bot.func.base;

public interface IFunctionTreeNode {
    Object accept(IFunctionTreeNodeVisitor visitor);
}
