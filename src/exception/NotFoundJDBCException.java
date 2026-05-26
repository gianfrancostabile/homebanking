package exception;

public class NotFoundJDBCException extends JDBCException {
    private static final String MESSAGE = "Data not found in Database";

    public NotFoundJDBCException() {
        super(MESSAGE);
    }
}