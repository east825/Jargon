package jargon;

/**
 * User: Mikhail Golubev
 * Date: 14.02.13
 * Time: 0:44
 */
public class ArgParserException extends RuntimeException {
    public ArgParserException() {
    }

    public ArgParserException(String message) {
        super(message);
    }

    public ArgParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArgParserException(Throwable cause) {
        super(cause);
    }
}
