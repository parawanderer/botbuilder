package gg.botbuilder.bot.repo;

import gg.botbuilder.bot.conf.model.ConfigContainerModel;

import java.util.List;
import java.util.Map;

public interface IConfigRepository {
    Map<String, ConfigContainerModel> pullConfigs(List<String> serverIds);
}
