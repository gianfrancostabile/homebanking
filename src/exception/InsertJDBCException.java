package exception;

public class InsertJDBCException extends JDBCException {
    private static final String MESSAGE = "There was an error inserting a data to Database";

    public InsertJDBCException() {
        super(MESSAGE);
    }

    public InsertJDBCException(Exception exception) {
        super(MESSAGE, exception);
    }
}
