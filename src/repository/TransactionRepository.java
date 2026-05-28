package repository;

import constant.CommonConstant;
import enums.Currency;
import enums.PaymentMethod;
import enums.TransactionType;
import exception.JDBCException;
import exception.NotFoundJDBCException;
import model.Transaction;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepository extends JDBCRepository<Transaction, String> {

    private static final String[] INSERT_GENERATED_COLUMNS = {"id"};
    private static final String INSERT_QUERY = "INSERT INTO Transactions(source_product_id, destination_product_id, card_id, type, payment_method, currency, amount, creation_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String UPDATE_QUERY = "UPDATE Transactions SET source_product_id = ?, destination_product_id = ?, card_id = ?, type = ?, payment_method = ?, currency = ?, amount = ?, creation_date = ? WHERE id = ?;";
    private static final String DELETE_ALL_QUERY = "DELETE FROM Transactions;";
    private static final String DELETE_QUERY = "DELETE FROM Transactions WHERE id = ?;";
    private static final String FIND_ALL_QUERY = "SELECT id, creation_date, type, payment_method, currency, amount, source_product_id, destination_product_id, card_id FROM Transactions";
    private static final String FIND_ONE_BY_ID_QUERY = "SELECT id, creation_date, type, payment_method, currency, amount, source_product_id, destination_product_id, card_id FROM Transactions WHERE id = ?";
    private static final String FIND_BY_CLIENT_ID_QUERY = """
            SELECT
                t.id,
                t.creation_date,
                t.type,
                t.payment_method,
                t.currency, 
                t.amount,
                t.source_product_id,
            	t.destination_product_id,
            	t.card_id
            FROM Transactions t
            LEFT JOIN Products p_src ON t.source_product_id = p_src.id
            LEFT JOIN Products p_dest ON t.destination_product_id = p_dest.id
            WHERE
                ((p_src.client_id = ? AND t.type IN ('DEBIT','TO_PAY'))
                OR (p_dest.client_id = ? AND t.type = 'CHARGE'))
                AND t.creation_date BETWEEN ? AND ?
                AND (? IS NULL OR t.type = ?)
            ORDER BY t.id DESC;
            """;

    private static TransactionRepository INSTANCE;

    private TransactionRepository() {
        super("jdbc:mysql://localhost:3306/testpalermo?user=root&password=nosequeponer");
    }

    public static TransactionRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TransactionRepository();
        }
        return INSTANCE;
    }

    @Override
    protected String insertImplementation(Connection connection, Transaction data) throws Exception {
        PreparedStatement statement = connection.prepareStatement(INSERT_QUERY, INSERT_GENERATED_COLUMNS);
        statement.setString(1, data.getSourceProductId());
        statement.setString(2, data.getDestinationProductId());
        statement.setString(3, data.getCardId());
        statement.setString(4, data.getType().name());
        statement.setString(5, data.getPaymentMethod().name());
        statement.setString(6, data.getCurrency().name());
        statement.setDouble(7, data.getAmount());
        statement.setObject(8, data.getCreationDate());
        statement.executeUpdate();
        ResultSet resultSet = statement.getGeneratedKeys();
        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return null;
    }

    @Override
    protected void updateImplementation(Connection connection, String id, Transaction data) throws Exception {
        PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY);
        statement.setString(1, data.getSourceProductId());
        statement.setString(2, data.getDestinationProductId());
        statement.setString(3, data.getCardId());
        statement.setString(4, data.getType().name());
        statement.setString(5, data.getPaymentMethod().name());
        statement.setString(6, data.getCurrency().name());
        statement.setDouble(7, data.getAmount());
        statement.setObject(8, data.getCreationDate());
        statement.setString(9, id);
        statement.execute();
    }

    @Override
    protected void deleteImplementation(Connection connection, String id) throws Exception {
        PreparedStatement statement = connection.prepareStatement(DELETE_QUERY);
        statement.setString(1, id);
        statement.execute();
    }

    @Override
    protected void deleteAllImplementation(Connection connection) throws Exception {
        PreparedStatement statement = connection.prepareStatement(DELETE_ALL_QUERY);
        statement.execute();
    }

    @Override
    protected List<Transaction> findAllImplementation(Connection connection) throws Exception {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(FIND_ALL_QUERY);
        List<Transaction> transactions = new ArrayList<>();
        while (resultSet.next()) {
            transactions.add(this.mapTransaction(resultSet));
        }
        return transactions;
    }

    @Override
    protected Transaction findOneByIdImplementation(Connection connection, String id) throws Exception {
        PreparedStatement statement = connection.prepareStatement(FIND_ONE_BY_ID_QUERY);
        statement.setString(1, id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return this.mapTransaction(resultSet);
        }
        throw new NotFoundJDBCException();
    }

    public List<Transaction> findTransactionByClientIdAndDateAndType(String clientId, LocalDateTime from, LocalDateTime to, TransactionType type) throws JDBCException {
        Connection connection = this.connect();
        ArrayList<Transaction> result = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(FIND_BY_CLIENT_ID_QUERY);
            statement.setString(1, clientId);
            statement.setString(2, clientId);
            statement.setObject(3, from);
            statement.setObject(4, to);
            if (type == null || TransactionType.NONE.equals(type)) {
                statement.setNull(5, java.sql.Types.VARCHAR);
                statement.setNull(6, java.sql.Types.VARCHAR);
            } else {
                statement.setString(5, type.name());
                statement.setString(6, type.name());
            }
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result.add(this.mapTransaction(resultSet));
            }
        } catch (Exception _) {
        } finally {
            this.disconnect(connection);
        }
        return result;
    }

    private Transaction mapTransaction(ResultSet resultSet) throws Exception {
        return new Transaction(
                resultSet.getString(1),
                resultSet.getObject(2, LocalDateTime.class),
                TransactionType.valueOf(resultSet.getString(3)),
                PaymentMethod.valueOf(resultSet.getString(4)),
                Currency.valueOf(resultSet.getString(5)),
                resultSet.getDouble(6),
                resultSet.getString(7),
                resultSet.getString(8),
                resultSet.getString(9)
        );
    }
}
