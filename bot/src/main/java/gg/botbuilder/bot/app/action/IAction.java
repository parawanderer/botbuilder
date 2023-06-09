package gg.botbuilder.bot.app.action;

import gg.botbuilder.bot.conf.model.action.IThenActionArguments;

/**
 * A thing the bot can do as a result of a "then" statement
 */
public interface IAction {
    void execute(IThenActionArguments actionConfig);
}
