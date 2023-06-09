package gg.botbuilder.bot.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Map;

@Disabled
public class JsonPathTest {
    @Test
    void test() throws JsonProcessingException {
        var foo = """
                {
                    "bar": {
                        "baz": [1, 2, 3],
                        "bax": "aa",
                        "barz": true
                    }
                }
                """;

        var jsonProvider = new JacksonJsonProvider();
        var mappingProvider = new JacksonMappingProvider();
        var conf = Configuration.builder()
                .jsonProvider(jsonProvider)
                .mappingProvider(mappingProvider)
                .build();

        DocumentContext thing = JsonPath.using(conf).parse(foo);
        var result = thing.read("$.bar.baz[0]");

        ObjectMapper objectMapper = new ObjectMapper();
        var other = objectMapper.readValue(foo, new TypeReference<Map<String,Object>>(){});

        System.out.println(result);


        var customJson = generateJson();
        DocumentContext thing2 = JsonPath.using(conf).parse(customJson);

        var test = thing2.read("afaf");

        System.out.println("aa");
    }

    private static Object generateJson() {
        ObjectMapper map = new ObjectMapper();

        ObjectNode root = map.createObjectNode();
        ObjectNode message = map.createObjectNode();
        ObjectNode server = map.createObjectNode();
        ObjectNode channel = map.createObjectNode();
        ObjectNode sender = map.createObjectNode();

        root.set("message", message);

        message.set("server", server);
        server.put("name", "My epic server");
        server.put("invite_link", "https://foo");
        server.put("id", 1234567810);

        message.set("channel", channel);
        channel.put("name", "general");
        channel.put("id", 99999999);

        message.set("sender", sender);
        sender.put("id", 88888888);
        sender.put("name", "wanderer");

        var roles = map.createArrayNode();
        sender.set("roles", roles);
        roles.add(1283414142);
        roles.add(332522355);
        roles.add(235756353);
        roles.add(666565666);

        return map.convertValue(root, new TypeReference<Map<String,Object>>(){});
    }
}
