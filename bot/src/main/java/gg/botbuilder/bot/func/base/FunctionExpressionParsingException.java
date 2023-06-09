package gg.botbuilder.bot.func.base;

public class FunctionExpressionParsingException extends RuntimeException {
    public FunctionExpressionParsingException() {
    }

    public FunctionExpressionParsingException(String message) {
        super(message);
    }

    public FunctionExpressionParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    public FunctionExpressionParsingException(Throwable cause) {
        super(cause);
    }
}
