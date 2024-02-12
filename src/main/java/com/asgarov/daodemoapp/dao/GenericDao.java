package com.asgarov.daodemoapp.dao;

import com.asgarov.daodemoapp.dao.exception.DaoException;

public interface GenericDao<T, K> {
    boolean create(T object) throws DaoException;

    T read(K id) throws DaoException;

    void update(T object) throws DaoException;

    void delete(K id) throws DaoException;
}
