package exception;

public class UpdateJDBCException extends JDBCException {
    private static final String MESSAGE = "There was an error updating a data to Database";

    public UpdateJDBCException() {
        super(MESSAGE);
    }

    public UpdateJDBCException(Exception exception) {
        super(MESSAGE, exception);
    }
}
