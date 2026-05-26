package repository;

import constant.CardBrand;
import constant.CardType;
import exception.DeleteJDBCException;
import exception.JDBCException;
import exception.NotFoundJDBCException;
import model.Card;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CardRepository extends JDBCRepository<Card, String> {

    private static final String[] INSERT_GENERATED_COLUMNS = {"id"};
    private static final String INSERT_QUERY = "INSERT INTO Cards(product_id, brand, type, card_number, security_code, expiration_date, owner_name, available_debt_balance, debt_balance) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String UPDATE_QUERY = "UPDATE Cards SET product_id = ?, brand = ?, type = ?, card_number = ?, security_code = ?, expiration_date = ?, owner_name = ?, available_debt_balance = ?, debt_balance = ? WHERE id = ?;";
    private static final String DELETE_ALL_QUERY = "DELETE FROM Cards;";
    private static final String DELETE_QUERY = "DELETE FROM Cards WHERE id = ?;";
    private static final String FIND_ALL_QUERY = "SELECT id, product_id, brand, type, card_number, security_code, expiration_date, owner_name, available_debt_balance, debt_balance FROM Cards";
    private static final String FIND_ONE_BY_ID_QUERY = "SELECT id, product_id, brand, type, card_number, security_code, expiration_date, owner_name, available_debt_balance, debt_balance FROM Cards WHERE id = ?";
    private static final String FIND_BY_PRODUCT_ID_QUERY = "SELECT id, product_id, brand, type, card_number, security_code, expiration_date, owner_name, available_debt_balance, debt_balance FROM Cards WHERE product_id = ?";
    private static final String FIND_BY_PRODUCT_IDS_QUERY = "SELECT id, product_id, brand, type, card_number, security_code, expiration_date, owner_name, available_debt_balance, debt_balance FROM Cards WHERE product_id IN (";
    private static final String DELETE_BY_PRODUCT_ID_QUERY = "DELETE FROM Cards WHERE product_id = ?";
    private static final DateFormat EXPIRATION_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    public CardRepository(String user, String password) {
        super("jdbc:mysql://localhost:3306/testpalermo?user=" + user + " &password=" + password);
    }

    @Override
    protected String insertImplementation(Connection connection, Card data) throws Exception {
        PreparedStatement statement = connection.prepareStatement(INSERT_QUERY, INSERT_GENERATED_COLUMNS);
        statement.setString(1, data.getProductId());
        statement.setString(2, data.getBrand().name());
        statement.setString(3, data.getType().name());
        statement.setString(4, data.getCardNumber());
        statement.setString(5, data.getSecurityCode());
        statement.setString(6, EXPIRATION_DATE_FORMAT.format(data.getExpirationDate()));
        statement.setString(7, data.getOwnerName());
        statement.setDouble(8, data.getAvailableDebtBalance());
        statement.setDouble(9, data.getDebtBalance());
        statement.executeUpdate();
        ResultSet resultSet = statement.getGeneratedKeys();
        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return null;
    }

    @Override
    protected void updateImplementation(Connection connection, String id, Card data) throws Exception {
        PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY);
        statement.setString(1, data.getProductId());
        statement.setString(2, data.getBrand().name());
        statement.setString(3, data.getType().name());
        statement.setString(4, data.getCardNumber());
        statement.setString(5, data.getSecurityCode());
        statement.setString(6, EXPIRATION_DATE_FORMAT.format(data.getExpirationDate()));
        statement.setString(7, data.getOwnerName());
        statement.setDouble(8, data.getAvailableDebtBalance());
        statement.setDouble(9, data.getDebtBalance());
        statement.setString(10, id);
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
    protected List<Card> findAllImplementation(Connection connection) throws Exception {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(FIND_ALL_QUERY);
        List<Card> cards = new ArrayList<>();
        while (resultSet.next()) {
            cards.add(this.mapCard(resultSet));
        }
        return cards;
    }

    @Override
    protected Card findOneByIdImplementation(Connection connection, String id) throws Exception {
        PreparedStatement statement = connection.prepareStatement(FIND_ONE_BY_ID_QUERY);
        statement.setString(1, id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return this.mapCard(resultSet);
        }
        throw new NotFoundJDBCException();
    }

    public List<Card> findByProductId(String productIds) throws JDBCException {
        Connection connection = this.connect();
        List<Card> result = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(FIND_BY_PRODUCT_ID_QUERY);
            statement.setString(1, productIds);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result.add(this.mapCard(resultSet));
            }
            return result;
        } catch (Exception _) {
        } finally {
            this.disconnect(connection);
        }
        return result;
    }

    public List<Card> findByProductIds(List<String> productIds) throws JDBCException {
        Connection connection = this.connect();
        List<Card> result = new ArrayList<>();
        StringBuilder query = new StringBuilder(FIND_BY_PRODUCT_IDS_QUERY);
        for (String id : productIds) {
            query.append("?,");
        }
        query.deleteCharAt(query.length() - 1);
        query.append(")");
        try {
            PreparedStatement statement = connection.prepareStatement(query.toString());
            for (int i = 0; i < productIds.size(); i++) {
                statement.setString(i + 1, productIds.get(i));
            }
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result.add(this.mapCard(resultSet));
            }
            return result;
        } catch (Exception _) {
        } finally {
            this.disconnect(connection);
        }
        return result;
    }

    public void deleteByProductId(String productId) throws JDBCException {
        Connection connection = this.connect();
        try {
            PreparedStatement statement = connection.prepareStatement(DELETE_BY_PRODUCT_ID_QUERY);
            statement.setString(1, productId);
            statement.execute();
        } catch (Exception exception) {
            throw new DeleteJDBCException(exception);
        } finally {
            this.disconnect(connection);
        }
    }

    private Card mapCard(ResultSet resultSet) throws Exception {
        return new Card(
                resultSet.getString(1),
                resultSet.getString(2),
                CardBrand.valueOf(resultSet.getString(3)),
                CardType.valueOf(resultSet.getString(4)),
                resultSet.getString(5),
                resultSet.getString(6),
                EXPIRATION_DATE_FORMAT.parse(resultSet.getString(7)),
                resultSet.getString(8),
                resultSet.getDouble(9),
                resultSet.getDouble(10)
        );
    }
}
