package gg.botbuilder.bot.conf;

public class ConfigParsingException extends RuntimeException {
    public ConfigParsingException(String message) {
        super(message);
    }

    public ConfigParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigParsingException(Throwable cause) {
        super(cause);
    }
}
