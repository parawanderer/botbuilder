package gg.botbuilder.bot.func;

import gg.botbuilder.bot.func.base.IFunctionTreeNode;
import gg.botbuilder.bot.func.eval.FunctionEvaluator;
import gg.botbuilder.bot.jsonpath.IJsonPathQueryableData;
import gg.botbuilder.func.FunctionRegistry;
import gg.botbuilder.func.IFunctionRegistry;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static gg.botbuilder.bot.func.base.ConstantFunctionTreeNode.*;
import static gg.botbuilder.bot.func.base.FunctionFunctionTreeNode.*;
import static gg.botbuilder.bot.func.base.VariableFunctionTreeNode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FunctionEvaluatorTest {
    private static IFunctionRegistry registry;

    @Mock
    IJsonPathQueryableData ctx;

    @BeforeAll
    public static void before() {
        registry = new FunctionRegistry();
    }

    @Test
    void givenBasicRandomFunction_whenEvaluate_thenEvaluateTrue() {
        var evaluator = new FunctionEvaluator(registry);
        IFunctionTreeNode function = fn("random", number(1), number(3));
        Object result = evaluator.evaluate(function, ctx);
        assertNotNull(result);
        assertInstanceOf(Integer.class, result);
        Integer casted = (Integer) result;
        assertTrue(Set.of(1, 2).contains(casted));
    }

    @Test
    void givenBasicContainsFunction_whenEvaluate_thenEvaluateTrue() {
        var evaluator = new FunctionEvaluator(registry);
        IFunctionTreeNode function = fn("contains", string("foobarbaz"), string("bar"));
        Object result = evaluator.evaluate(function, ctx);
        assertNotNull(result);
        assertInstanceOf(Integer.class, result);
        Integer casted = (Integer) result;
        assertEquals(1, casted);
    }

    @ParameterizedTest
    @ValueSource(strings = {"foobarbaz", "foo", "    '", " ", "", " okay   ", "\""})
    void givenBasicEqFunction_whenEvaluate_thenEvaluateTrue(String testCase) {
        var evaluator = new FunctionEvaluator(registry);
        IFunctionTreeNode function = fn("eq", string(testCase), string(testCase));
        Object result = evaluator.evaluate(function, ctx);
        assertNotNull(result);
        assertInstanceOf(Integer.class, result);
        Integer casted = (Integer) result;
        assertEquals(1, casted);
    }

    @ParameterizedTest
    @ValueSource(strings = {"foobarbaz", "foo", "    '", " ", "", " okay   ", "\""})
    void givenBasicEqsFunction_whenEvaluate_thenEvaluateTrue(String testCase) {
        var evaluator = new FunctionEvaluator(registry);
        IFunctionTreeNode function = fn("eqs", string(testCase), string(testCase));
        Object result = evaluator.evaluate(function, ctx);
        assertNotNull(result);
        assertInstanceOf(Integer.class, result);
        Integer casted = (Integer) result;
        assertEquals(1, casted);
    }

    @ParameterizedTest
    @ValueSource(longs = {1, -1, 20, 999999, 0, -66666666666L})
    void givenBasicEqFunction_whenEvaluate_thenEvaluateTrue(long testCase) {
        var evaluator = new FunctionEvaluator(registry);
        IFunctionTreeNode function = fn("eq", number(testCase), number(testCase));
        Object result = evaluator.evaluate(function, ctx);
        assertNotNull(result);
        assertInstanceOf(Integer.class, result);
        Integer casted = (Integer) result;
        assertEquals(1, casted);
    }

    @ParameterizedTest
    @ValueSource(longs = {1, -1, 20, 999999, 0, -66666666666L})
    void givenBasicEqsFunction_whenEvaluate_thenEvaluateTrue(long testCase) {
        var evaluator = new FunctionEvaluator(registry);
        IFunctionTreeNode function = fn("eqs", number(testCase), number(testCase));
        Object result = evaluator.evaluate(function, ctx);
        assertNotNull(result);
        assertInstanceOf(Integer.class, result);
        Integer casted = (Integer) result;
        assertEquals(1, casted);
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "  1", "   1    ", "\n1"})
    void givenBasicEqFunction_whenEvaluateForDifferentTypes_thenEvaluateTrue(String testCase) {
        var evaluator = new FunctionEvaluator(registry);
        IFunctionTreeNode function = fn("eq", string(testCase), number(1));
        Object result = evaluator.evaluate(function, ctx);
        assertNotNull(result);
        assertInstanceOf(Integer.class, result);
        Integer casted = (Integer) result;
        assertEquals(1, casted);
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "  1", "   1    ", "\n1"})
    void givenBasicEqFunction_whenEvaluateForDifferentTypes_thenEvaluateFalse(String testCase) {
        var evaluator = new FunctionEvaluator(registry);
        IFunctionTreeNode function = fn("eqs", string(testCase), number(1));
        Object result = evaluator.evaluate(function, ctx);
        assertNotNull(result);
        assertInstanceOf(Integer.class, result);
        Integer casted = (Integer) result;
        assertEquals(0, casted);
    }

    @ParameterizedTest
    @MethodSource("basicIfFunctionTestCases")
    void givenBasicIfFunction_whenEvaluate_thenEvaluateToCorrectValue(IFunctionTreeNode arg0, IFunctionTreeNode arg1, IFunctionTreeNode arg2, Object expected) {
        var evaluator = new FunctionEvaluator(registry);
        IFunctionTreeNode function = fn("if", arg0, arg1, arg2);
        Object result = evaluator.evaluate(function, ctx);
        assertNotNull(result);
        assertInstanceOf(expected.getClass(), result);
        assertEquals(expected, result);
    }

    private static Stream<Arguments> basicIfFunctionTestCases() {
        return Stream.of(
                Arguments.of(number(0), string("one"), string("two"), "two"),
                Arguments.of(number(1), string("one"), string("two"), "one"),
                Arguments.of(number(0), number(1), number(2), BigInteger.valueOf(2)),
                Arguments.of(number(1), number(1), number(2), BigInteger.valueOf(1)),
                Arguments.of(number(0), string("one"), number(2), BigInteger.valueOf(2)),
                Arguments.of(number(1), number(1), string("two"), BigInteger.valueOf(1))
        );
    }

    @Test
    void givenNestedIfFunctions_whenEvaluate_thenReturnProperValue() {
        var evaluator = new FunctionEvaluator(registry);
        IFunctionTreeNode function = fn("if",
                fn("eq", string("foo"), string("foo")),
                number(11),
                number(66));
        Object result = evaluator.evaluate(function, ctx);
        assertNotNull(result);
        assertInstanceOf(BigInteger.class, result);
        BigInteger casted = (BigInteger) result;
        assertEquals(BigInteger.valueOf(11), casted);
    }

    @Test
    void givenEqFunction_whenEvaluateAgainstVariable_thenEvaluateTrue() {
        when(ctx.queryOne("$.foo.bar")).thenReturn(Optional.of("foo"));

        var evaluator = new FunctionEvaluator(registry);
        IFunctionTreeNode function = fn("eq", string("foo"), var("$.foo.bar"));
        Object result = evaluator.evaluate(function, ctx);
        assertNotNull(result);
        assertInstanceOf(Integer.class, result);
        Integer casted = (Integer) result;
        assertEquals(1, casted);
    }

    @Test
    void givenEqFunction_whenEvaluateTwoVariables_thenEvaluateTrue() {
        when(ctx.queryOne("$.foo.bar")).thenReturn(Optional.of("foo"));
        when(ctx.queryOne("$.bar.baz")).thenReturn(Optional.of("foo"));

        var evaluator = new FunctionEvaluator(registry);
        IFunctionTreeNode function = fn("eq", var("$.bar.baz"), var("$.foo.bar"));
        Object result = evaluator.evaluate(function, ctx);
        assertNotNull(result);
        assertInstanceOf(Integer.class, result);
        Integer casted = (Integer) result;
        assertEquals(1, casted);
    }

    @Test
    void givenIfFunctionNestedEqFunction_whenEvaluateTwoVariables_thenReturnProperValue() {
        when(ctx.queryOne("$.foo.bar")).thenReturn(Optional.of("foo"));
        when(ctx.queryOne("$.bar.baz")).thenReturn(Optional.of("foo"));

        var evaluator = new FunctionEvaluator(registry);
        IFunctionTreeNode function = fn("if",
                fn("eq", var("$.bar.baz"), var("$.foo.bar")),
                string("we are expecting this one!"),
                string("and not this one :(")
        );
        Object result = evaluator.evaluate(function, ctx);
        assertNotNull(result);
        assertInstanceOf(String.class, result);
        String casted = (String) result;
        assertEquals("we are expecting this one!", casted);
    }

    @Test
    void givenIfFunctionNestedEqFunctionWithNestedResultValues_whenEvaluateTwoVariables_thenReturnProperValue() {
        when(ctx.queryOne("$.foo.bar")).thenReturn(Optional.of("foo"));
        when(ctx.queryOne("$.bar.baz")).thenReturn(Optional.of("foo"));
        when(ctx.queryOne("$.bax")).thenReturn(Optional.of("we are expecting this one!"));
        when(ctx.queryOne("$.poopSock")).thenReturn(Optional.of("and not this one :("));

        var evaluator = new FunctionEvaluator(registry);
        IFunctionTreeNode function = fn("if",
                fn("eq", var("$.bar.baz"), var("$.foo.bar")),
                var("$.bax"),
                var("$.poopSock")
        );
        Object result = evaluator.evaluate(function, ctx);
        assertNotNull(result);
        assertInstanceOf(String.class, result);
        String casted = (String) result;
        assertEquals("we are expecting this one!", casted);
    }
}
