package gg.botbuilder.bot.clauses.tree;

public class ClauseDefinitionException extends RuntimeException {
    public ClauseDefinitionException() {
    }

    public ClauseDefinitionException(String message) {
        super(message);
    }

    public ClauseDefinitionException(String message, Throwable cause) {
        super(message, cause);
    }
}
