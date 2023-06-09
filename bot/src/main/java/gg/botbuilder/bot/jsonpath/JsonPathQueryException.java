package gg.botbuilder.bot.jsonpath;

public class JsonPathQueryException extends RuntimeException {

    public JsonPathQueryException() {
    }

    public JsonPathQueryException(String message) {
        super(message);
    }

    public JsonPathQueryException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonPathQueryException(Throwable cause) {
        super(cause);
    }
}
