package gg.botbuilder.bot.clauses;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import gg.botbuilder.bot.clauses.eval.ClauseTreeEvaluatorVisitor;
import gg.botbuilder.bot.jsonpath.JsonPathQueryableData;
import gg.botbuilder.bot.clauses.tree.ClauseTreeRoot;
import gg.botbuilder.bot.testutil.ContextMakerUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;

import static gg.botbuilder.bot.clauses.tree.FunctionExpressionNode.*;
import static gg.botbuilder.bot.clauses.tree.LogicalExpressionNode.and;
import static gg.botbuilder.bot.clauses.tree.LogicalExpressionNode.or;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClauseTreeEvaluatorVisitorTest {
    private static Map<String,Object> CONTEXT1;
    private static Map<String,Object> CONTEXT2;
    private static Map<String,Object> MINIMAL_CONTEXT;
    private static ClauseTreeRoot ROOT;

    @BeforeAll
    static void beforeAll() throws JsonProcessingException {
        CONTEXT1 = buildContext();
        CONTEXT2 = buildContext2();
        MINIMAL_CONTEXT = ContextMakerUtil.toContextMap("""
                {
                    "message": {
                        "sender": {
                            "name": "john"
                        }
                    }
                }
                """);
        ROOT = buildClause();
    }

    @Test
    void givenSingleExpressionEq_whenEvaluate_thenEvaluateTrue() {
        var ctx = new JsonPathQueryableData(MINIMAL_CONTEXT);
        var visitor = new ClauseTreeEvaluatorVisitor(ctx);
        var expr = new ClauseTreeRoot(eq("$.message.sender.name", "john"));
        boolean result = visitor.evaluate(expr);
        assertTrue(result);
    }

    @Test
    void givenSingleExpressionEq_whenEvaluate_thenEvaluateFalse() {
        var ctx = new JsonPathQueryableData(MINIMAL_CONTEXT);
        var visitor = new ClauseTreeEvaluatorVisitor(ctx);
        var expr = new ClauseTreeRoot(eq("$.message.sender.name", "pete"));
        boolean result = visitor.evaluate(expr);
        assertFalse(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"john", "joh", "jo", "j", ""})
    void givenSingleExpressionContains_whenEvaluate_thenEvaluateTrue(String testCase) {
        var ctx = new JsonPathQueryableData(MINIMAL_CONTEXT);
        var visitor = new ClauseTreeEvaluatorVisitor(ctx);
        var expr = new ClauseTreeRoot(contains("$.message.sender.name", testCase));
        boolean result = visitor.evaluate(expr);
        assertTrue(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"pete", "pet", "pe", "p", "agheuiwhtui"})
    void givenSingleExpressionContains_whenEvaluate_thenEvaluateFalse(String testCase) {
        var ctx = new JsonPathQueryableData(MINIMAL_CONTEXT);
        var visitor = new ClauseTreeEvaluatorVisitor(ctx);
        var expr = new ClauseTreeRoot(contains("$.message.sender.name", testCase));
        boolean result = visitor.evaluate(expr);
        assertFalse(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"john", "joh", "jo", "j", ""})
    void givenSingleExpressionStartsWith_whenEvaluate_thenEvaluateTrue(String testCase) {
        var ctx = new JsonPathQueryableData(MINIMAL_CONTEXT);
        var visitor = new ClauseTreeEvaluatorVisitor(ctx);
        var expr = new ClauseTreeRoot(startsWith("$.message.sender.name", testCase));
        boolean result = visitor.evaluate(expr);
        assertTrue(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"pete", "pet", "pe", "p"})
    void givenSingleExpressionStartsWith_whenEvaluate_thenEvaluateFalse(String testCase) {
        var ctx = new JsonPathQueryableData(MINIMAL_CONTEXT);
        var visitor = new ClauseTreeEvaluatorVisitor(ctx);
        var expr = new ClauseTreeRoot(startsWith("$.message.sender.name", testCase));
        boolean result = visitor.evaluate(expr);
        assertFalse(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"john", "ohn", "hn", "n", ""})
    void givenSingleExpressionEndsWith_whenEvaluate_thenEvaluateTrue(String testCase) {
        var ctx = new JsonPathQueryableData(MINIMAL_CONTEXT);
        var visitor = new ClauseTreeEvaluatorVisitor(ctx);
        var expr = new ClauseTreeRoot(endsWith("$.message.sender.name", testCase));
        boolean result = visitor.evaluate(expr);
        assertTrue(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"te", "pete", "fadfasdfasdf"})
    void givenSingleExpressionEndsWith_whenEvaluate_thenEvaluateFalse(String testCase) {
        var ctx = new JsonPathQueryableData(MINIMAL_CONTEXT);
        var visitor = new ClauseTreeEvaluatorVisitor(ctx);
        var expr = new ClauseTreeRoot(endsWith("$.message.sender.name", testCase));
        boolean result = visitor.evaluate(expr);
        assertFalse(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"^jo.n$", "^john$", "j.*$", "john", "j[eao]h[ean]", "j.{3}", ""})
    void givenSingleExpressionRegex_whenEvaluate_thenEvaluateTrue(String testCase) {
        var ctx = new JsonPathQueryableData(MINIMAL_CONTEXT);
        var visitor = new ClauseTreeEvaluatorVisitor(ctx);
        var expr = new ClauseTreeRoot(regex("$.message.sender.name", testCase));
        boolean result = visitor.evaluate(expr);
        assertTrue(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"^je.n$", "ageqwetAQERwegtERFDS", "john.", "j[ae]hn", "(john){2,}", "^$"})
    void givenSingleExpressionRegex_whenEvaluate_thenEvaluateFalse(String testCase) {
        var ctx = new JsonPathQueryableData(MINIMAL_CONTEXT);
        var visitor = new ClauseTreeEvaluatorVisitor(ctx);
        var expr = new ClauseTreeRoot(regex("$.message.sender.name", testCase));
        boolean result = visitor.evaluate(expr);
        assertFalse(result);
    }

    @Test
    void givenExtensiveContextAndSomeClauses_whenEvaluate_thenEvaluateTrue() {
        var ctx = new JsonPathQueryableData(CONTEXT2);
        var visitor = new ClauseTreeEvaluatorVisitor(ctx);
        boolean result = visitor.evaluate(ROOT);
        assertTrue(result);
    }

    @Test
    void givenExtensiveContextAndSomeClauses_whenEvaluate_thenEvaluateFalse() {
        var ctx = new JsonPathQueryableData(CONTEXT1);
        var visitor = new ClauseTreeEvaluatorVisitor(ctx);
        boolean result = visitor.evaluate(ROOT);
        assertFalse(result);
    }

    private static ClauseTreeRoot buildClause() {
        var clause = new ClauseTreeRoot(and(
                eq("$.message.server.id", "1081879642513866762"),
                eq("$.message.channel.id", "1081879643168194562"),
                or(
                        eq("$.message.sender.roles", "1114649835484745749"),
                        eq("$.message.sender.id", "192148309123530753")
                )
        ));

        return clause;
    }

    private static Map<String,Object> buildContext2() throws JsonProcessingException {
        var context = """
        {
            "message": {
                "server": {
                    "id": "1081879642513866762",
                    "name": "My Awesome Server!",
                    "link": "https://url"
                },
                "channel": {
                    "id": 1081879643168194562,
                    "name": "general"
                },
                "sender": {
                    "name": "wanderer",
                    "id": "192148309123530753",
                    "roles": [
                        "1114649835484745743",
                        "1104649835484745749"
                    ]
                }
            }
        }
        """;

        return ContextMakerUtil.toContextMap(context);
    }

    private static Map<String,Object> buildContext() {
        ObjectMapper map = new ObjectMapper();

        ObjectNode root = map.createObjectNode();
        ObjectNode message = map.createObjectNode();
        ObjectNode server = map.createObjectNode();
        ObjectNode channel = map.createObjectNode();
        ObjectNode sender = map.createObjectNode();

        root.set("message", message);

        message.set("server", server);
        server.put("name", "My epic server");
        server.put("link", "https://foo");
        server.put("id", "1081879642513866762");

        message.set("channel", channel);
        channel.put("name", "memes");
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

        return map.convertValue(root, new TypeReference<>(){});
    }
}
