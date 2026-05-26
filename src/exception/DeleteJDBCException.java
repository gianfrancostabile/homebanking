package exception;

public class DeleteJDBCException extends JDBCException {
    private static final String MESSAGE = "There was an error deleting a data to Database";

    public DeleteJDBCException() {
        super(MESSAGE);
    }

    public DeleteJDBCException(Exception exception) {
        super(MESSAGE, exception);
    }
}
