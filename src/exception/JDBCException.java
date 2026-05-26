package exception;

public class JDBCException extends Exception {
    public JDBCException(String message) {
        super(message);
    }

    public JDBCException(String message, Exception exception) {
        super(message, exception);
    }
}
