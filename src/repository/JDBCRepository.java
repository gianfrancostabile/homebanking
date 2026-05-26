package repository;

import exception.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public abstract class JDBCRepository<T, ID> implements IRepository<T, ID> {
    private final String connectionUrl;

    protected JDBCRepository(String connectionUrl) {
        this.connectionUrl = connectionUrl;
    }

    protected Connection connect() throws ConnectionJDBCException {
        try {
            return DriverManager.getConnection(connectionUrl);
        } catch (SQLException exception) {
            throw new ConnectionJDBCException(exception);
        }
    }

    protected void disconnect(Connection connection) throws DisconnectionJDBCException {
        if (connection == null) {
            return;
        }
        try {
            if (connection.isClosed()) {
                return;
            }
            if (!connection.getAutoCommit()) {
                connection.commit();
            }
            connection.close();
        } catch (SQLException exception) {
            throw new DisconnectionJDBCException(exception);
        }
    }

    @Override
    public ID insert(T data) throws JDBCException {
        Connection connection = this.connect();
        try {
            return this.insertImplementation(connection, data);
        } catch (Exception exception) {
            throw new InsertJDBCException(exception);
        } finally {
            this.disconnect(connection);
        }
    }

    @Override
    public void update(ID id, T data) throws JDBCException {
        Connection connection = this.connect();
        try {
            this.updateImplementation(connection, id, data);
        } catch (Exception exception) {
            throw new UpdateJDBCException(exception);
        } finally {
            this.disconnect(connection);
        }
    }

    @Override
    public void delete(ID id) throws JDBCException {
        Connection connection = this.connect();
        try {
            this.deleteImplementation(connection, id);
        } catch (Exception exception) {
            throw new DeleteJDBCException(exception);
        } finally {
            this.disconnect(connection);
        }
    }

    @Override
    public void deleteAll() throws JDBCException {
        Connection connection = this.connect();
        try {
            this.deleteAllImplementation(connection);
        } catch (Exception exception) {
            throw new DeleteJDBCException(exception);
        } finally {
            this.disconnect(connection);
        }
    }

    @Override
    public List<T> findAll() throws JDBCException {
        Connection connection = this.connect();
        try {
            return this.findAllImplementation(connection);
        } catch (Exception exception) {
            throw new FindJDBCException(exception);
        } finally {
            this.disconnect(connection);
        }
    }

    @Override
    public T findOneById(ID id) throws JDBCException {
        Connection connection = this.connect();
        try {
            return this.findOneByIdImplementation(connection, id);
        } catch (Exception exception) {
            throw new FindJDBCException(exception);
        } finally {
            this.disconnect(connection);
        }
    }

    protected abstract ID insertImplementation(Connection connection, T data) throws Exception;

    protected abstract void updateImplementation(Connection connection, ID id, T data) throws Exception;

    protected abstract void deleteImplementation(Connection connection, ID id) throws Exception;

    protected abstract void deleteAllImplementation(Connection connection) throws Exception;

    protected abstract List<T> findAllImplementation(Connection connection) throws Exception;

    protected abstract T findOneByIdImplementation(Connection connection, ID id) throws Exception;
}
