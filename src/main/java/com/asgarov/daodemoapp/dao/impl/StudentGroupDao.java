package com.asgarov.daodemoapp.dao.impl;


import com.asgarov.daodemoapp.dao.AbstractDao;
import com.asgarov.daodemoapp.dao.exception.DaoException;
import com.asgarov.daodemoapp.domain.StudentGroup;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class StudentGroupDao extends AbstractDao<StudentGroup, Integer> {

    @Override
    protected String getCreateQuery(StudentGroup group) {
        return "INSERT INTO student_group (group_name) VALUES (?);";
    }

    @Override
    protected String getSelectByIdQuery() {
        return "SELECT * FROM student_group WHERE group_id = ?;";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE groups SET student_group = ? WHERE group_id = ?;";
    }

    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM student_group WHERE group_id = ?";
    }

    @Override
    protected void setIdIntoStatement(PreparedStatement statement, Integer id) throws DaoException {
        try {
            statement.setInt(1, id);
        } catch (SQLException e) {
            throw new DaoException(e.getMessage(), e);
        }
    }

    @Override
    protected void setObjectIntoStatement(PreparedStatement statement, StudentGroup group) throws DaoException {
        try {
            if (group.getId() != 0) {
                statement.setString(1, group.getName());
                statement.setInt(2, group.getId());
            } else {
                statement.setString(1, group.getName());
            }
        } catch (SQLException e) {
            throw new DaoException(e.getMessage(), e);
        }
    }

    @Override
    protected StudentGroup readObject(ResultSet resultSet) throws DaoException {
        StudentGroup group = new StudentGroup();
        try {
            group.setId(resultSet.getInt("group_id"));
            group.setName(resultSet.getString("group_name"));
        } catch (SQLException e) {
            throw new DaoException(e.getMessage(), e);
        }
        return group;
    }
}
