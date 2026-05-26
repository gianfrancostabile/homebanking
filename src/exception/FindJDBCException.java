package exception;

public class FindJDBCException extends JDBCException {
    private static final String MESSAGE = "There was an error finding a data to Database";

    public FindJDBCException() {
        super(MESSAGE);
    }

    public FindJDBCException(Exception exception) {
        super(MESSAGE, exception);
    }
}
