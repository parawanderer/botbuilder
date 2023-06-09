package gg.botbuilder.bot.conf;


import gg.botbuilder.bot.conf.model.ConfigContainerModel;
import gg.botbuilder.bot.conf.model.DISCORD_ACTION_TYPE;
import gg.botbuilder.bot.conf.model.DISCORD_EVENT_TYPE;
import gg.botbuilder.bot.conf.model.action.DiscordThenActionModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class YamlConfigInterpreterTest {
    private static String TEST_CONFIG;

    @BeforeAll
    static void getTestResources() {
        TEST_CONFIG = readContents("config-ideas.yml");
    }

    @Test
    void givenValidYamlConfig_whenInterpreted_thenResultReturned() {
        var interpreter = new YamlConfigInterpreter();
        ConfigContainerModel result = interpreter.interpret(TEST_CONFIG);
        assertNotNull(result);
    }

    @Test
    void givenValidYamlConfig_whenInterpreted_thenResultCustomisationExpected() {
        var interpreter = new YamlConfigInterpreter();
        ConfigContainerModel result = interpreter.interpret(TEST_CONFIG);
        var customisation = result.getCustomisation();
        assertNotNull(customisation);
        assertEquals("My Bot", customisation.getName());
        assertEquals("https://s3-link-goes-here", customisation.getProfilePictureUrl());
        assertEquals("watermark by default else custom", customisation.getStatus());
    }

    @Test
    void givenValidYamlConfig_whenInterpreted_thenResultModulesExpected() {
        var interpreter = new YamlConfigInterpreter();
        ConfigContainerModel result = interpreter.interpret(TEST_CONFIG);
        var modules = result.getModules();
        assertNotNull(modules);
        var list = modules.getList();
        assertEquals(List.of("midjourney", "crypto", "chatgpt", "minecraft-server", "authentication", "calculator"), list);
    }

    @Test
    void givenValidYamlConfig_whenInterpreted_thenConfigVariablesExpected() {
        var interpreter = new YamlConfigInterpreter();
        ConfigContainerModel result = interpreter.interpret(TEST_CONFIG);
        var config = result.getConfig();
        assertNotNull(config);
        var variables = config.getVariables();
        assertNotNull(variables);
        assertEquals(2, variables.size());
        var expectedVars = Map.of(
            "MY_VARIABLE", "poop sock",
            "SOME_TOKEN", "aaaa"
        );
        assertEquals(expectedVars, variables);
    }

    @Test
    void givenValidYamlConfig_whenInterpreted_thenEventsExpected() {
        var interpreter = new YamlConfigInterpreter();
        ConfigContainerModel result = interpreter.interpret(TEST_CONFIG);
        var config = result.getConfig();
        assertNotNull(config);
        var discordConf = config.getDiscord();
        assertNotNull(discordConf);
        var events = discordConf.getEvents();
        assertNotNull(events);
        assertEquals(1, events.size());
        var event = events.get(0);
        assertEquals(DISCORD_EVENT_TYPE.MESSAGE_READ, event.getOn());
        var where = event.getWhere();
        assertTrue(where.isPresent());
        assertNotNull( where.get().getRoot()); // test the expression tree generation separately somewhere....
        var then = event.getThen();
        assertNotNull(then);
        assertEquals(5, then.size());
        assertEquals(
                List.of(DISCORD_ACTION_TYPE.REPLY, DISCORD_ACTION_TYPE.SEND_MESSAGE, DISCORD_ACTION_TYPE.LOG, DISCORD_ACTION_TYPE.ROLE_ADD, DISCORD_ACTION_TYPE.WEBHOOK),
                then.stream().map(DiscordThenActionModel::getActionType).collect(Collectors.toList())
        );
    }

    static String readContents(String fileName) {
        try {
            var stream = YamlConfigInterpreterTest.class.getClassLoader().getResourceAsStream(fileName);
            return new String(stream.readAllBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
