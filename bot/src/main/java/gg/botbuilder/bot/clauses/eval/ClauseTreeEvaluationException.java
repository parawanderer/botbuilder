package gg.botbuilder.bot.clauses.eval;

public class ClauseTreeEvaluationException extends RuntimeException {
    public ClauseTreeEvaluationException(String message) {
        super(message);
    }

    public ClauseTreeEvaluationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClauseTreeEvaluationException(Throwable cause) {
        super(cause);
    }
}
