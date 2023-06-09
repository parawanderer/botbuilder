package gg.botbuilder.bot.jsonpath;

public class JsonPathDoesNotExistException extends JsonPathQueryException {
    public JsonPathDoesNotExistException() {
    }

    public JsonPathDoesNotExistException(String message) {
        super(message);
    }

    public JsonPathDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonPathDoesNotExistException(Throwable cause) {
        super(cause);
    }
}
