package gg.botbuilder.func;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FunctionRegistryTest {
    private static FunctionRegistry registry;

    @BeforeAll
    public static void before() {
        registry = new FunctionRegistry();
    }

    @Test
    public void whenCreateFunctionRegistry_thenHasExpectedFunctions() {
        var functions = registry.getAllFunctions();
        assertNotNull(functions);
        assertEquals(Set.of("random", "eq", "contains"), functions.keySet());
    }

    @Test
    public void whenCreateFunctionRegistry_thenFunctionsHaveExpectedName() {
        var functions = registry.getAllFunctions();
        assertNotNull(functions);
        Set<Pair<String, String>> functionKeyAndDefinitionNamePairs = functions.entrySet().stream()
                .map(kvp -> Pair.of(kvp.getKey(), kvp.getValue().getDefinition().getName()))
                .collect(Collectors.toSet());
        assertEquals(Set.of(
                Pair.of("random", "random"),
                Pair.of("eq", "eq"),
                Pair.of("contains", "contains")
        ), functionKeyAndDefinitionNamePairs);
    }

    @Test
    public void whenCreateFunctionRegistry_thenFunctionsProvideDetails() {
        var functions = registry.getAllFunctions();
        assertNotNull(functions);
        for (var fn : functions.values()) {
            var def = fn.getDefinition();

            assertNotNull(def.getName());
            assertNotEquals("", def.getName().trim());

            assertNotNull(def.getDescription());
            assertNotEquals("", def.getName().trim());

            assertNotNull(def.getArguments());
            for (var argDef: def.getArguments()) {
                assertNotNull(argDef);

                assertNotNull(argDef.getArgumentName());
                assertNotEquals("", argDef.getArgumentName().trim());

                assertNotNull(argDef.getDescription());
                assertNotEquals("", argDef.getDescription().trim());
            }

            assertNotNull(def.getReturns());

            assertNotNull(def.getReturns().getDescription());
            assertNotEquals("", def.getReturns().getDescription().trim());
        }
    }
}
