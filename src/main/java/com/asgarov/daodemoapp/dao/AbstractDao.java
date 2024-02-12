package com.asgarov.daodemoapp.dao;

import com.asgarov.daodemoapp.dao.exception.DaoException;
import com.asgarov.daodemoapp.util.ConnectionFactory;

import java.sql.*;

public abstract class AbstractDao<T, K> implements GenericDao<T, K> {
    private static final int UPDATE_EXECUTED_SUCCESSFULLY = 1;
    public static final String COULD_NOT_FIND_AN_OBJECT_WITH_SUCH_ID = "Couldn't find an object with such ID!";

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
    public T create(T object) throws DaoException {
        String createQuery = getCreateQuery(object);
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(createQuery, Statement.RETURN_GENERATED_KEYS)) {

            setObjectIntoStatement(statement, object);
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    setId(object, (K) generatedKeys.getObject(1));
                } else {
                    throw new SQLException("Problem with creating the object!");
                }
            }
            return object;
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
                throw new DaoException(COULD_NOT_FIND_AN_OBJECT_WITH_SUCH_ID);
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
