package gg.botbuilder.bot.func;

import gg.botbuilder.bot.func.base.FunctionFunctionTreeNode;
import gg.botbuilder.bot.func.base.NestedFunctionParser;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static gg.botbuilder.bot.func.base.ConstantFunctionTreeNode.*;
import static gg.botbuilder.bot.func.base.FunctionFunctionTreeNode.*;
import static gg.botbuilder.bot.func.base.VariableFunctionTreeNode.*;
import static org.junit.jupiter.api.Assertions.*;

public class NestedFunctionParserTest {
    @ParameterizedTest
    @ValueSource(strings = {
            "fn()",
            "fn(1)",
            "fn(1,2)",
            "f___n()",
            "fn(\"one\")",
            "fn(\"one\",-2)",
            "fn(1,2,33,444,555,-666,\"-7777\")",
            "fn(\"one\\\"two!\")"
    })
    void givenBasicValidFunctions_wheParse_thenNoThrow(String testCase) {
        var parser = new NestedFunctionParser();
        assertDoesNotThrow(() -> parser.parse(testCase));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "fn()",
            "fn( )",
            "fn(1)",
            "fn( 1 )",
            "fn(1, 2)",
            "fn(1, -2)",
            "fn(-99999999999999999999999999)",
            "fn(\"1\")",
            "fn(  \"  1 \" )",
            "fn(1,\"2\",3)",
            "fn(1, \"2\", 3)",
            "fn($.some.var)",
            "fn(  $.some.var  )",
            "fn(  $.some.var, 1, \"2\", $.anotherVar  )",
            "fn( \"  one man said \\\", hello, world\\\" and   then wandered away\" )",
            "fn( \"\" )",
            "fn( \" \" )",
            "fn( \",\\,\\\" \", \"\\\",\" )",
    })
    void giveComplexerFunctions_whenParse_thenNoThrow(String testCase) {
        var parser = new NestedFunctionParser();
        assertDoesNotThrow(() -> parser.parse(testCase));
    }

    @ParameterizedTest
    @MethodSource("expectedFunctionNodesBasicParser")
    void givenBasicValidFunctions_whenParse_thenParsedCorrectly(String testCase, FunctionFunctionTreeNode expectedFunction) {
        var parser = new NestedFunctionParser();
        FunctionFunctionTreeNode result = parser.parse(testCase);
        assertEquals(expectedFunction, result);
    }

    @ParameterizedTest
    @MethodSource("expectedFunctionNodesNestedParser")
    void givenNestedValidFunctions_whenParse_thenParsedCorrectly(String testCase, FunctionFunctionTreeNode expectedFunction) {
        var parser = new NestedFunctionParser();
        FunctionFunctionTreeNode result = parser.parse(testCase);
        assertEquals(expectedFunction, result);
    }

    private static Stream<Arguments> expectedFunctionNodesBasicParser() {
        return Stream.of(
                Arguments.arguments("fn()", fn("fn")),
                Arguments.arguments("myFunction()", fn("myFunction")),
                Arguments.arguments("fn( )", fn("fn")),
                Arguments.arguments("fn(1)", fn("fn", number(1))),
                Arguments.arguments("fn( 1 )", fn("fn", number(1))),
                Arguments.arguments("fn(1, 2)", fn("fn", number(1), number(2))),
                Arguments.arguments("fn(1, -2)", fn("fn", number(1), number(-2))),
                Arguments.arguments("fn(-99999999999999999999999999)", fn("fn", number("-99999999999999999999999999"))),
                Arguments.arguments("fn(\"1\")", fn("fn", string("1"))),
                Arguments.arguments("fn(  \"  1 \" )", fn("fn", string("  1 "))),
                Arguments.arguments("fn(1,\"2\",3)", fn("fn", number(1), string("2"), number(3))),
                Arguments.arguments("fn(1, \"2 \", 3)", fn("fn", number(1), string("2 "), number(3))),
                Arguments.arguments("fn($.some.var)", fn("fn", var("$.some.var"))),
                Arguments.arguments("fn(  $.some.var  )", fn("fn", var("$.some.var"))),
                Arguments.arguments("fn(  $.some.var, 1, \"2\", $.anotherVar  )", fn("fn", var("$.some.var"), number(1), string("2"), var("$.anotherVar"))),
                Arguments.arguments("fn( \"  one man said \\\", hello, world\\\" and   then wandered away\" )", fn("fn", string("  one man said \\\", hello, world\\\" and   then wandered away"))),
                Arguments.arguments("fn( \"\" )", fn("fn", string(""))),
                Arguments.arguments("fn( \" \" )", fn("fn", string(" "))),
                Arguments.arguments("fn( \",\\,\\\" \", \"\\\",\" )", fn("fn", string(",\\,\\\" "), string("\\\",")))
        );
    }

    private static Stream<Arguments> expectedFunctionNodesNestedParser() {
        return Stream.of(
                Arguments.arguments("fn(fn())", fn("fn", fn("fn"))),
                Arguments.arguments("fn(one(),two())", fn("fn", fn("one"), fn("two"))),
                Arguments.arguments("fn(one(),two(\"one\", 2, $.three))", fn("fn", fn("one"), fn("two", string("one"), number(2), var("$.three")))),
                Arguments.arguments("a(b(),c(),d(g(h(),i(j(),k(),\"\\\",\", l(\"m\", 2)))),e(\"f\"))",
                        fn("a",
                                fn("b"),
                                fn("c"),
                                fn("d",
                                        fn("g",
                                                fn("h"),
                                                fn("i",
                                                        fn("j"),
                                                        fn("k"),
                                                        string("\\\","),
                                                        fn("l",
                                                                string("m"),
                                                                number(2))))),
                                fn("e", string("f"))
                        )),
                Arguments.arguments("eq(random(),abs($.some.value))", fn("eq", fn("random"), fn("abs", var("$.some.value"))))
//                Arguments.arguments("myFunction()", fn("myFunction")),
//                Arguments.arguments("fn( )", fn("fn")),
//                Arguments.arguments("fn(1)", fn("fn", number(1))),
//                Arguments.arguments("fn( 1 )", fn("fn", number(1))),
//                Arguments.arguments("fn(1, 2)", fn("fn", number(1), number(2))),
//                Arguments.arguments("fn(1, -2)", fn("fn", number(1), number(-2))),
//                Arguments.arguments("fn(-99999999999999999999999999)", fn("fn", number("-99999999999999999999999999"))),
//                Arguments.arguments("fn(\"1\")", fn("fn", string("1"))),
//                Arguments.arguments("fn(  \"  1 \" )", fn("fn", string("  1 "))),
//                Arguments.arguments("fn(1,\"2\",3)", fn("fn", number(1), string("2"), number(3))),
//                Arguments.arguments("fn(1, \"2 \", 3)", fn("fn", number(1), string("2 "), number(3))),
//                Arguments.arguments("fn($.some.var)", fn("fn", var("$.some.var"))),
//                Arguments.arguments("fn(  $.some.var  )", fn("fn", var("$.some.var"))),
//                Arguments.arguments("fn(  $.some.var, 1, \"2\", $.anotherVar  )", fn("fn", var("$.some.var"), number(1), string("2"), var("$.anotherVar"))),
//                Arguments.arguments("fn( \"  one man said \\\", hello, world\\\" and   then wandered away\" )", fn("fn", string("  one man said \\\", hello, world\\\" and   then wandered away"))),
//                Arguments.arguments("fn( \"\" )", fn("fn", string(""))),
//                Arguments.arguments("fn( \" \" )", fn("fn", string(" "))),
//                Arguments.arguments("fn( \",\\,\\\" \", \"\\\",\" )", fn("fn", string(",\\,\\\" "), string("\\\",")))
        );
    }
}
