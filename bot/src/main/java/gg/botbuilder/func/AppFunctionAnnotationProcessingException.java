package gg.botbuilder.func;

public class AppFunctionAnnotationProcessingException extends RuntimeException {
    public AppFunctionAnnotationProcessingException() {
    }

    public AppFunctionAnnotationProcessingException(String message) {
        super(message);
    }

    public AppFunctionAnnotationProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppFunctionAnnotationProcessingException(Throwable cause) {
        super(cause);
    }
}
