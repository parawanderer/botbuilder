package gg.botbuilder.bot.func.eval;

public class AppFunctionExecutionException extends RuntimeException {
    public AppFunctionExecutionException() {
    }

    public AppFunctionExecutionException(String message) {
        super(message);
    }

    public AppFunctionExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppFunctionExecutionException(Throwable cause) {
        super(cause);
    }
}
