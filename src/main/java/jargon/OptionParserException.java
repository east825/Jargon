package jargon;

/**
 * User: Mikhail Golubev
 * Date: 14.02.13
 * Time: 0:44
 */
public class OptionParserException extends RuntimeException {
    public OptionParserException() {
    }

    public OptionParserException(String message) {
        super(message);
    }

    public OptionParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public OptionParserException(Throwable cause) {
        super(cause);
    }
}
