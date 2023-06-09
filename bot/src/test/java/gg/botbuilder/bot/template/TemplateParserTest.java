package gg.botbuilder.bot.template;

import com.fasterxml.jackson.core.JsonProcessingException;
import gg.botbuilder.bot.jsonpath.JsonPathQueryableData;
import gg.botbuilder.bot.testutil.ContextMakerUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TemplateParserTest {
    private static Map<String,Object> MINIMAL_CONTEXT;
    private static Map<String,Object> ELABORATE_CONTEXT;

    @BeforeAll
    static void beforeAll() throws JsonProcessingException {
        MINIMAL_CONTEXT = ContextMakerUtil.toContextMap("""
                {
                    "message": {
                        "sender": {
                            "name": "john"
                        }
                    }
                }
                """);

        ELABORATE_CONTEXT = ContextMakerUtil.toContextMap("""
                {
                    "message": {
                        "sender": {
                            "name": "john",
                            "at": "<@1234567890>",
                            "id": "1234567890"
                        },
                        "server": {
                            "name": "Our Cool Server!",
                            "id": "111111111",
                            "at": "<@111111111>",
                            "link": "https://url"
                        },
                        "channel": {
                            "name": "general",
                            "id": "22222222222",
                            "at": "<@22222222222>"
                        }
                    },
                    "ENV": {
                        "MY_ENVIRONMENT_VARIABLE": "foo bar baz",
                        "ANOTHERONE": "44444"
                    }
                }
                """);
    }

    @Test
    void givenContextAndString_whenApply_thenExpectedResult() {
        var ctx = new JsonPathQueryableData(MINIMAL_CONTEXT);
        var placer = new TemplateParser(ctx);
        String result = placer.apply("I'd like to see my variable replaced here: $.message.sender.name ... did it succeed?");
        assertEquals("I'd like to see my variable replaced here: john ... did it succeed?", result);
    }

    @Test
    void givenStringWithRepetitions_whenApply_thenExpectedResult() {
        var ctx = new JsonPathQueryableData(MINIMAL_CONTEXT);
        var placer = new TemplateParser(ctx);
        String result = placer.apply("So let's see here: $.message.sender.name | $.message.sender.name | $.message.sender.name | $.message.sender.name | and that is it!");
        assertEquals("So let's see here: john | john | john | john | and that is it!", result);
    }

    @Test
    void givenComplexTemplateUseCase_whenApply_thenExpectedResult() {
        var ctx = new JsonPathQueryableData(ELABORATE_CONTEXT);
        var placer = new TemplateParser(ctx);

        var template =
"""
Hello $.message.sender.at and welcome to our _awesome_ discord server $.message.server.at!
You likely used our invitation link [here]($.message.server.link), right?

We hope you will enjoy your time chatting in some of our chats, like $.message.channel.at where you are chatting right now, but also <@8888888888>!

PS: the secret number is `$.ENV.ANOTHERONE`! But don't tell anyone!
""";

        String result = placer.apply(template);

        assertEquals(
"""
Hello <@1234567890> and welcome to our _awesome_ discord server <@111111111>!
You likely used our invitation link [here](https://url), right?

We hope you will enjoy your time chatting in some of our chats, like <@22222222222> where you are chatting right now, but also <@8888888888>!

PS: the secret number is `44444`! But don't tell anyone!
""",
                result);
    }

    @Test
    void givenComplexTemplateUseCase_whenApplyWithMissingContext_thenExpectedResult() {
        var ctx = new JsonPathQueryableData(MINIMAL_CONTEXT);
        var placer = new TemplateParser(ctx);

        var template =
"""
Hello $.message.sender.name and welcome to our _awesome_ discord server $.message.server.@!
You likely used our invitation link [here]($.message.server.link), right?

We hope you will enjoy your time chatting in some of our chats, like $.message.channel.@ where you are chatting right now, but also <@8888888888>!

PS: the secret number is `$.ENV.ANOTHERONE`! But don't tell anyone! (Talk to you later, $.message.sender.name!)
""";

        String result = placer.apply(template);

        assertEquals(
"""
Hello john and welcome to our _awesome_ discord server $.message.server.@!
You likely used our invitation link [here]($.message.server.link), right?

We hope you will enjoy your time chatting in some of our chats, like $.message.channel.@ where you are chatting right now, but also <@8888888888>!

PS: the secret number is `$.ENV.ANOTHERONE`! But don't tell anyone! (Talk to you later, john!)
""",
                result);
    }
}
