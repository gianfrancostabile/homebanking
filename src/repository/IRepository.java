package repository;

import exception.JDBCException;

import java.util.List;

public interface IRepository<T, ID> {
    ID insert(T data) throws JDBCException;

    void update(ID id, T data) throws JDBCException;

    void delete(ID id) throws JDBCException;

    void deleteAll() throws JDBCException;

    List<T> findAll() throws JDBCException;

    T findOneById(ID id) throws JDBCException;
}
