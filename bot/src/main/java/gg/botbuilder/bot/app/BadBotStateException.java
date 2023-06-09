package gg.botbuilder.bot.app;

public class BadBotStateException extends RuntimeException {
    public BadBotStateException() {
        super();
    }

    public BadBotStateException(String message) {
        super(message);
    }

    public BadBotStateException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadBotStateException(Throwable cause) {
        super(cause);
    }
}
