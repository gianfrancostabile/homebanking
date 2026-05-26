package repository;

import constant.TransactionType;
import exception.NotFoundJDBCException;
import model.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepository extends JDBCRepository<Transaction, String> {

    private static final String[] INSERT_GENERATED_COLUMNS = {"id"};
    private static final String INSERT_QUERY = "INSERT INTO Transactions(source_product_id, destination_product_id, type, amount, creation_date) VALUES (?, ?, ?, ?, ?);";
    private static final String UPDATE_QUERY = "UPDATE Transactions SET source_product_id = ?, destination_product_id = ?, type = ?, amount = ?, creation_date = ? WHERE id = ?;";
    private static final String DELETE_ALL_QUERY = "DELETE FROM Transactions;";
    private static final String DELETE_QUERY = "DELETE FROM Transactions WHERE id = ?;";
    private static final String FIND_ALL_QUERY = "SELECT id, creation_date, type, amount, source_product_id, destination_product_id FROM Transactions";
    private static final String FIND_ONE_BY_ID_QUERY = "SELECT id, creation_date, type, amount, source_product_id, destination_product_id FROM Transactions WHERE id = ?";
    private static final DateFormat CREATION_DATE_FORMAT = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");

    public TransactionRepository(String user, String password) {
        super("jdbc:mysql://localhost:3306/testpalermo?user=" + user + " &password=" + password);
    }

    @Override
    protected String insertImplementation(Connection connection, Transaction data) throws Exception {
        PreparedStatement statement = connection.prepareStatement(INSERT_QUERY, INSERT_GENERATED_COLUMNS);
        statement.setString(1, data.getSourceProductId());
        statement.setString(2, data.getDestinationProductId());
        statement.setString(3, data.getType().name());
        statement.setDouble(4, data.getAmount());
        statement.setString(5, CREATION_DATE_FORMAT.format(data.getCreationDate()));
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
        statement.setString(3, data.getType().name());
        statement.setDouble(4, data.getAmount());
        statement.setString(5, CREATION_DATE_FORMAT.format(data.getCreationDate()));
        statement.setString(6, id);
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

    private Transaction mapTransaction(ResultSet resultSet) throws Exception {
        return new Transaction(
                resultSet.getString(1),
                CREATION_DATE_FORMAT.parse(resultSet.getString(2)),
                TransactionType.valueOf(resultSet.getString(3)),
                resultSet.getDouble(4),
                resultSet.getString(5),
                resultSet.getString(6)
        );
    }
}
