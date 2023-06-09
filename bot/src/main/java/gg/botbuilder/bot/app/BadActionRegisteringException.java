package gg.botbuilder.bot.app;

public class BadActionRegisteringException extends RuntimeException {
    public BadActionRegisteringException() {
    }

    public BadActionRegisteringException(String message) {
        super(message);
    }

    public BadActionRegisteringException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadActionRegisteringException(Throwable cause) {
        super(cause);
    }
}
