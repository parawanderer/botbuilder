package gg.botbuilder.bot.jsonpath;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class JsonPathQueryableDataTest {
    private static final Map<String, Object> TEST_DATA = Map.of(
        "Foo", Map.of(
                "bar", 1,
                "baz", "lalalla",
                "bax", List.of("foo", "bar"),
                "no", List.of(342, 25, 266, 4),
                "_mixed_valuetypes", List.of(111, "222", true)
        ),
       "Bar", "poop sock",
       "Baz", List.of(
               Map.of(
                       "one", 1,
                       "two", "2",
                       "three", true
               ),
            Map.of(
                    "one", 2,
                    "two", "3",
                    "three", false
            )
       )
    );

    @Test
    void givenValidTestData_whenGetData_thenDataReturned() {
        var queryableData = new JsonPathQueryableData(TEST_DATA);
        var data = queryableData.getData();
        assertNotNull(data);
    }

    @ParameterizedTest
    @ValueSource(strings = {"$.Bar", "$.['Bar']", "$.[\"Bar\"]"})
    void givenValidTestData_whenQueryWithBasicQuery_thenDataReturned(String query) {
        var queryableData = new JsonPathQueryableData(TEST_DATA);
        List<String> result = queryableData.query(query);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("poop sock", result.get(0));
    }

    @ParameterizedTest
    @ValueSource(strings = {"$.Bar", "$.['Bar']", "$.[\"Bar\"]"})
    void givenValidTestData_whenQueryWithBasicQueryOne_thenDataReturned(String query) {
        var queryableData = new JsonPathQueryableData(TEST_DATA);
        Optional<String> result = queryableData.queryOne("$.Bar");
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals("poop sock", result.get());
    }

    @ParameterizedTest
    @ValueSource(strings = {"$.['Foo'].no[1,2]", "$.['Foo'].no[-2]", "$.['Foo'].no[:2]", "$.['Foo'].no[1:2]", "$.Foo.*", "$.Foo.no.max()"})
    void givenValidTestData_whenQueryWithAlternateBasicQuery_thenExceptionThrown(String notAllowedQuery) {
        var queryableData = new JsonPathQueryableData(TEST_DATA);
        assertThrows(
                JsonPathQueryException.class,
                () -> queryableData.query(notAllowedQuery),
                "Currently advanced formats like `" + notAllowedQuery + "` are not intended to be supported"
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"$.['Foo'].no[1,2]", "$.['Foo'].no[-2]", "$.['Foo'].no[:2]", "$.['Foo'].no[1:2]", "$.Foo.*", "$.Foo.no.max()"})
    void givenValidTestData_whenQueryWithAlternateBasicQueryOne_thenExceptionThrown(String notAllowedQuery) {
        var queryableData = new JsonPathQueryableData(TEST_DATA);
        assertThrows(
                JsonPathQueryException.class,
                () -> queryableData.queryOne(notAllowedQuery),
                "Currently advanced formats like `" + notAllowedQuery + "` are not intended to be supported"
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"$.Foo.baz", "$.['Foo'].baz", "$.['Foo'].['baz']", "$.Foo.['baz']"})
    void givenValidTestData_whenQueryWithDepthQuery_thenDataReturned(String query) {
        var queryableData = new JsonPathQueryableData(TEST_DATA);
        List<String> result = queryableData.query(query);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("lalalla", result.get(0));
    }

    @ParameterizedTest
    @ValueSource(strings = {"$.Foo.baz", "$.['Foo'].baz", "$.['Foo'].['baz']", "$.Foo.['baz']"})
    void givenValidTestData_whenQueryWithDepthQueryOne_thenDataReturned(String query) {
        var queryableData = new JsonPathQueryableData(TEST_DATA);
        Optional<String> result = queryableData.queryOne(query);
        assertTrue(result.isPresent());
        assertEquals("lalalla", result.get());
    }

    @Test
    void givenValidTestData_whenQueryWithDepthQueryInteger_thenDataReturned() {
        var queryableData = new JsonPathQueryableData(TEST_DATA);
        List<String> result = queryableData.query("$.Foo.no.[0]");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("342", result.get(0));
    }

    @Test
    void givenValidTestData_whenQueryWithBasicQueryOneInteger_thenDataReturned() {
        var queryableData = new JsonPathQueryableData(TEST_DATA);
        Optional<String> result = queryableData.queryOne("$.Foo.no.[0]");
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals("342", result.get());
    }

    @Test
    void givenValidTestData_whenQueryForValueTypeList_thenListReturned() {
        var queryableData = new JsonPathQueryableData(TEST_DATA);
        List<String> result = queryableData.query("$.Foo.bax");
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("foo", result.get(0));
        assertEquals("bar", result.get(1));
    }

    @Test
    void givenValidTestData_whenQueryForMixedValueTypeList_thenListReturnedAllStrings() {
        var queryableData = new JsonPathQueryableData(TEST_DATA);
        List<String> result = queryableData.query("$.Foo._mixed_valuetypes");
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("111", result.get(0));
        assertEquals("222", result.get(1));
        assertEquals("true", result.get(2));
    }

    @Test
    void givenValidTestData_whenQueryForObject_thenExceptionThrown() {
        var queryableData = new JsonPathQueryableData(TEST_DATA);
        assertThrows(
                JsonPathQueryException.class,
                () -> queryableData.query("$.Foo"),
                "Currently using a result that is an object is not supposed to be supported"
        );
    }

    @Test
    void givenValidTestData_whenQueryOneForObject_thenExceptionThrown() {
        var queryableData = new JsonPathQueryableData(TEST_DATA);
        assertThrows(
                JsonPathQueryException.class,
                () -> queryableData.queryOne("$.Foo"),
                "Currently using a result that is an object is not supposed to be supported"
        );
    }

    @Test
    void givenValidTestData_whenQueryForNonExistent_thenExceptionThrown() {
        var queryableData = new JsonPathQueryableData(TEST_DATA);
        assertThrows(
                JsonPathDoesNotExistException.class,
                () -> queryableData.query("$.Foo.i.do.not.exist"),
                "An exception should be thrown for non-existent paths"
        );
    }

    @Test
    void givenValidTestData_whenQueryOneForNonExistent_thenOptionalEmpty() {
        var queryableData = new JsonPathQueryableData(TEST_DATA);
        Optional<String> result = queryableData.queryOne("$.Foo.i.do.not.exist");
        assertTrue(result.isEmpty());
    }
}
