package exceptions;

/**
 * Exception thrown when a source solution is invalid
 */
public class SolutionInvalidException extends Exception {
    public SolutionInvalidException(String message) {
        super(message);
    }
    
    public SolutionInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}

