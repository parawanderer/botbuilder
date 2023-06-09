package gg.botbuilder.bot.func.base;

import lombok.NonNull;

public interface IFunctionParser {
    FunctionFunctionTreeNode parse(@NonNull String expression) throws FunctionExpressionParsingException;
}
