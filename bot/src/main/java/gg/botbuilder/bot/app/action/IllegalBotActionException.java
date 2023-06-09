package gg.botbuilder.bot.app.action;

public class IllegalBotActionException extends RuntimeException {
    public IllegalBotActionException() {
    }

    public IllegalBotActionException(String message) {
        super(message);
    }

    public IllegalBotActionException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalBotActionException(Throwable cause) {
        super(cause);
    }
}
