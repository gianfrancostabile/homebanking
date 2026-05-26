package repository;

import constant.ProductType;
import exception.DeleteJDBCException;
import exception.JDBCException;
import exception.NotFoundJDBCException;
import model.Card;
import model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductRepository extends JDBCRepository<Product, String> {

    private static final String[] INSERT_GENERATED_COLUMNS = {"id"};
    private static final String INSERT_QUERY = "INSERT INTO Products(client_id, alias, cbu, type, balance, creation_date) VALUES (?, ?, ?, ?, ?, ?);";
    private static final String UPDATE_QUERY = "UPDATE Products SET client_id = ?, alias = ?, cbu = ?, type = ?, balance = ?, creation_date = ? WHERE id = ?;";
    private static final String DELETE_ALL_QUERY = "DELETE FROM Products;";
    private static final String DELETE_QUERY = "DELETE FROM Products WHERE id = ?;";
    private static final String FIND_ALL_QUERY = "SELECT id, client_id, alias, cbu, type, balance, creation_date FROM Products";
    private static final String FIND_ONE_BY_ID_QUERY = "SELECT id, client_id, alias, cbu, type, balance, creation_date FROM Products WHERE id = ?";
    private static final String FIND_BY_CLIENT_ID_QUERY = "SELECT id, client_id, alias, cbu, type, balance, creation_date FROM Products WHERE client_id = ?";
    private static final String DELETE_BY_CLIENT_ID_QUERY = "DELETE FROM Products WHERE client_id = ?";
    private static final String FIND_ONE_BY_ID_ALIAS_CBU_QUERY = "SELECT id, client_id, alias, cbu, type, balance, creation_date FROM Products WHERE id = ? OR alias = ? OR cbu = ? LIMIT 1";
    private static final DateFormat CREATION_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    private static final CardRepository cardRepository = new CardRepository("root", "nosequeponer");

    public ProductRepository(String user, String password) {
        super("jdbc:mysql://localhost:3306/testpalermo?user=" + user + " &password=" + password);
    }

    @Override
    protected String insertImplementation(Connection connection, Product data) throws Exception {
        PreparedStatement statement = connection.prepareStatement(INSERT_QUERY, INSERT_GENERATED_COLUMNS);
        statement.setString(1, data.getClientId());
        statement.setString(2, data.getAlias());
        statement.setString(3, data.getCbu());
        statement.setString(4, data.getType().name());
        statement.setDouble(5, data.getBalance());
        statement.setString(6, CREATION_DATE_FORMAT.format(data.getCreationDate()));
        statement.executeUpdate();
        ResultSet resultSet = statement.getGeneratedKeys();
        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return null;
    }

    @Override
    protected void updateImplementation(Connection connection, String id, Product data) throws Exception {
        PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY);
        statement.setString(1, data.getClientId());
        statement.setString(2, data.getAlias());
        statement.setString(3, data.getCbu());
        statement.setString(4, data.getType().name());
        statement.setDouble(5, data.getBalance());
        statement.setString(6, CREATION_DATE_FORMAT.format(data.getCreationDate()));
        statement.setString(7, id);
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
    protected List<Product> findAllImplementation(Connection connection) throws Exception {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(FIND_ALL_QUERY);
        List<Product> products = new ArrayList<>();
        while (resultSet.next()) {
            products.add(this.mapProduct(resultSet));
        }
        return products;
    }

    @Override
    protected Product findOneByIdImplementation(Connection connection, String id) throws Exception {
        PreparedStatement statement = connection.prepareStatement(FIND_ONE_BY_ID_QUERY);
        statement.setString(1, id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return this.mapProduct(resultSet);
        }
        throw new NotFoundJDBCException();
    }

    public List<Product> findByClientId(String clientId) throws JDBCException {
        Connection connection = this.connect();
        List<Product> result = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(FIND_BY_CLIENT_ID_QUERY);
            statement.setString(1, clientId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result.add(this.mapProduct(resultSet));
            }
            return result;
        } catch (Exception _) {
        } finally {
            this.disconnect(connection);
        }
        return result;
    }

    public Optional<Product> findByIdOrAliasOrCbu(String input) throws JDBCException {
        Connection connection = this.connect();
        try {
            PreparedStatement statement = connection.prepareStatement(FIND_ONE_BY_ID_ALIAS_CBU_QUERY);
            statement.setString(1, input);
            statement.setString(2, input);
            statement.setString(3, input);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(this.mapProduct(resultSet));
            }
            return Optional.empty();
        } catch (Exception _) {
        } finally {
            this.disconnect(connection);
        }
        return Optional.empty();
    }

    public void deleteByClientId(String clientId) throws JDBCException {
        Connection connection = this.connect();
        try {
            PreparedStatement statement = connection.prepareStatement(DELETE_BY_CLIENT_ID_QUERY);
            statement.setString(1, clientId);
            statement.execute();
        } catch (Exception exception) {
            throw new DeleteJDBCException(exception);
        } finally {
            this.disconnect(connection);
        }
    }

    private Product mapProduct(ResultSet resultSet) throws Exception {
        String productId = resultSet.getString(1);
        List<Card> cards = cardRepository.findByProductId(productId);
        return new Product(
                productId,
                resultSet.getString(2),
                resultSet.getString(3),
                resultSet.getString(4),
                ProductType.valueOf(resultSet.getString(5)),
                resultSet.getDouble(6),
                cards,
                CREATION_DATE_FORMAT.parse(resultSet.getString(7))
        );
    }
}
