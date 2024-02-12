package com.asgarov.daodemoapp.dao;

import com.asgarov.daodemoapp.dao.exception.DaoException;
import com.asgarov.daodemoapp.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractDao<T, K> implements GenericDao<T, K> {
    private static final int UPDATE_EXECUTED_SUCCESSFULLY = 1;

    // CRUD Queries
    protected abstract String getCreateQuery(T object);

    protected abstract String getSelectByIdQuery();

    protected abstract String getUpdateQuery();

    protected abstract String getDeleteQuery();


    /**
     * Setting id into a prepared statement
     */
    protected abstract void setIdIntoStatement(PreparedStatement preparedStatement, K id)
            throws DaoException;

    protected abstract void setObjectIntoStatement(PreparedStatement preparedStatement, T object)
            throws DaoException;

    protected abstract T readObject(ResultSet resultSet) throws DaoException;


    @Override
    public boolean create(T object) throws DaoException {
        String createQuery = getCreateQuery(object);
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(createQuery)) {

            setObjectIntoStatement(statement, object);
            if (statement.executeUpdate() < UPDATE_EXECUTED_SUCCESSFULLY) {
                throw new DaoException("Problem with creating the object!");
            }
            return true;
        } catch (SQLException e) {
            throw new DaoException(e.getMessage(), e);
        }
    }

    @Override
    public T read(K id) throws DaoException {
        String selectByIdQuery = getSelectByIdQuery();
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(selectByIdQuery)) {

            setIdIntoStatement(statement, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return readObject(resultSet);
            } else {
                throw new DaoException("Couldn't find an object with such ID!");
            }
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void update(T object) throws DaoException {
        String updateQuery = getUpdateQuery();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(updateQuery)) {

            setObjectIntoStatement(statement, object);

            if (statement.executeUpdate() < UPDATE_EXECUTED_SUCCESSFULLY) {
                throw new DaoException("Problem with updating the object!");
            }
        } catch (SQLException e) {
            throw new DaoException(e.getMessage(), e);
        }
    }

    @Override
    public void delete(K id) throws DaoException {
        String deleteQuery = getDeleteQuery();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(deleteQuery)) {

            setIdIntoStatement(statement, id);
            if (statement.executeUpdate() < UPDATE_EXECUTED_SUCCESSFULLY) {
                throw new DaoException("Problem with deleting the object!");
            }

        } catch (SQLException e) {
            throw new DaoException(e.getMessage(), e);
        }
    }
}
