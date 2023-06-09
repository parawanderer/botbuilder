package gg.botbuilder.bot.jsonpath;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class JsonPathQueryableData implements IJsonPathQueryableData {
    @Getter
    private final Map<String,Object> data;

    @NonNull
    private final DocumentContext jsonDocContext;

    public JsonPathQueryableData(@NonNull Map<String, Object> context) {
        this.data = context;

        var jsonProvider = new JacksonJsonProvider();
        var mappingProvider = new JacksonMappingProvider();
        var conf = Configuration.builder()
                .jsonProvider(jsonProvider)
                .mappingProvider(mappingProvider)
                .build();

        this.jsonDocContext = JsonPath.using(conf).parse(this.data);
    }

    @Override
    @NonNull
    public List<String> query(String jsonPath) {
        // jsonpath evaluate this
        if (!JsonPathChecker.isLimitedJsonPath(jsonPath)) {
            throw new JsonPathQueryException("Query '" + jsonPath + "' is not valid");
        }
        try {
            // TODO: we should probably limit what is allowed here...
            var result = this.jsonDocContext.read(jsonPath, JsonNode.class);

            if (result.isValueNode()) {
                return List.of(result.asText());
            }
            if (result instanceof ArrayNode an && isArrayOfValueTypes(an)) {
                return convertToListOfString(an);
            }

        } catch (PathNotFoundException e) {
            throw new JsonPathDoesNotExistException("Path '" + jsonPath + "' does not exist", e);
        } catch (RuntimeException e) {
            throw new JsonPathQueryException("Query '" + jsonPath + "' resulted in error", e);
        }
        throw new JsonPathQueryException("Query '" + jsonPath + "' returned non-allowed value");
    }

    @Override
    @NonNull
    public Optional<String> queryOne(String jsonPath) {
        try {
            var queryResult = this.query(jsonPath);
            if (queryResult.size() >= 1) {
                return Optional.of(queryResult.get(0));
            }
        } catch (JsonPathDoesNotExistException e) { }
        return Optional.empty();
    }

    private static boolean isArrayOfValueTypes(@NonNull ArrayNode node) {
        return StreamSupport.stream(node.spliterator(), false).allMatch(JsonNode::isValueNode);
    }

    private static List<String> convertToListOfString(@NonNull ArrayNode node) {
        return StreamSupport.stream(node.spliterator(), false).map(JsonNode::asText).toList();
    }
}
