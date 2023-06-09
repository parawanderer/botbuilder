package gg.botbuilder.bot.app.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gg.botbuilder.bot.jsonpath.IJsonPathQueryableData;
import gg.botbuilder.bot.jsonpath.JsonPathQueryableData;

import java.util.Map;

public class QueryableEvaluationContextFactory {
    static IJsonPathQueryableData build(Object contextPojo) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> asMap = mapper.convertValue(contextPojo, new TypeReference<>(){});
        return new JsonPathQueryableData(asMap);
    }
}
