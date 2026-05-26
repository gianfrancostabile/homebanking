package exception;

public class ConnectionJDBCException extends JDBCException {
    private static final String MESSAGE = "There was an error trying to connect to Database";

    public ConnectionJDBCException() {
        super(MESSAGE);
    }

    public ConnectionJDBCException(Exception exception) {
        super(MESSAGE, exception);
    }
}
