package exception;

public class DisconnectionJDBCException extends JDBCException {
    private static final String MESSAGE = "There was an error trying to disconnect the Database";

    public DisconnectionJDBCException() {
        super(MESSAGE);
    }

    public DisconnectionJDBCException(Exception exception) {
        super(MESSAGE, exception);
    }
}
