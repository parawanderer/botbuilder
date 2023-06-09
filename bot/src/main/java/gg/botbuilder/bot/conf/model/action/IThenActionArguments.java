package gg.botbuilder.bot.conf.model.action;

import gg.botbuilder.bot.clauses.tree.ClauseTreeRoot;
import gg.botbuilder.bot.conf.model.DISCORD_ACTION_TYPE;

import java.util.Optional;

public interface IThenActionArguments {
    DISCORD_ACTION_TYPE getActionType();
    Optional<ClauseTreeRoot> getWhere();
}
