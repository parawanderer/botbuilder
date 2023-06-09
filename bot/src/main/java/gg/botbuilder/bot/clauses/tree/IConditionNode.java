package gg.botbuilder.bot.clauses.tree;

import java.util.List;

public interface IConditionNode {
    List<IConditionNode> getChildren();
    boolean accept(ICoditionTreeEvaluatorVisitor visitor);
}
