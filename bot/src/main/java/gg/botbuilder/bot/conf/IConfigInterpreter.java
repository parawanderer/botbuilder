package gg.botbuilder.bot.conf;

import gg.botbuilder.bot.conf.model.ConfigContainerModel;

public interface IConfigInterpreter {
    ConfigContainerModel interpret(String config);
}
