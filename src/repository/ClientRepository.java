package repository;

import exception.NotFoundJDBCException;
import model.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClientRepository extends JDBCRepository<Client, String> {

    private static final String[] INSERT_GENERATED_COLUMNS = {"id"};
    private static final String INSERT_QUERY = "INSERT INTO Clients(name, lastName) VALUES (?, ?);";
    private static final String UPDATE_QUERY = "UPDATE Clients SET name = ?, lastName = ? WHERE id = ?;";
    private static final String DELETE_ALL_QUERY = "DELETE FROM Clients;";
    private static final String DELETE_QUERY = "DELETE FROM Clients WHERE id = ?;";
    private static final String FIND_ALL_QUERY = "SELECT id, name, lastName FROM Clients";
    private static final String FIND_ONE_BY_ID_QUERY = "SELECT id, name, lastName FROM Clients WHERE id = ?";

    private static ClientRepository INSTANCE;
    private ClientRepository() {
        super("jdbc:mysql://localhost:3306/testpalermo?user=root&password=nosequeponer");
    }

    public static ClientRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClientRepository();
        }
        return INSTANCE;
    }

    @Override
    protected String insertImplementation(Connection connection, Client data) throws Exception {
        PreparedStatement statement = connection.prepareStatement(INSERT_QUERY, INSERT_GENERATED_COLUMNS);
        statement.setString(1, data.getName());
        statement.setString(2, data.getLastName());
        statement.executeUpdate();
        ResultSet resultSet = statement.getGeneratedKeys();
        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return null;
    }

    @Override
    protected void updateImplementation(Connection connection, String id, Client data) throws Exception {
        PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY);
        statement.setString(1, data.getName());
        statement.setString(2, data.getLastName());
        statement.setString(3, id);
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
    protected List<Client> findAllImplementation(Connection connection) throws Exception {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(FIND_ALL_QUERY);
        List<Client> people = new ArrayList<>();
        while (resultSet.next()) {
            Client person = new Client(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3));
            people.add(person);
        }
        return people;
    }

    @Override
    protected Client findOneByIdImplementation(Connection connection, String id) throws Exception {
        PreparedStatement statement = connection.prepareStatement(FIND_ONE_BY_ID_QUERY);
        statement.setString(1, id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return new Client(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3));
        }
        throw new NotFoundJDBCException();
    }
}
