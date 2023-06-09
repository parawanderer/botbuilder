package gg.botbuilder.bot.conf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import gg.botbuilder.bot.conf.model.ConfigContainerModel;

public class YamlConfigInterpreter implements IConfigInterpreter {
    private static final String BOT_ROOT = "bot";

    @Override
    public ConfigContainerModel interpret(String config) {
        try {// limit: 3MB
            ObjectMapper mapper = new YAMLMapper();
            var botRoot = mapper.readTree(config);
            var botOptions = botRoot.get(BOT_ROOT);
            ConfigContainerModel result = mapper.convertValue(botOptions, ConfigContainerModel.class);
            return result;
        } catch (JsonProcessingException e) {
            throw new ConfigParsingException("Could not parse configuration", e);
        }
    }
}
