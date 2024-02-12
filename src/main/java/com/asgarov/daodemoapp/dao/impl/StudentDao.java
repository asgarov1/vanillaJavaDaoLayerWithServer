package com.asgarov.daodemoapp.dao.impl;

import com.asgarov.daodemoapp.dao.AbstractDao;
import com.asgarov.daodemoapp.dao.exception.DaoException;
import com.asgarov.daodemoapp.domain.Student;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentDao extends AbstractDao<Student, Integer> {

    @Override
    protected String getCreateQuery(Student student) {
        return "INSERT INTO students (group_id, first_name, last_name) VALUES (?,?,?);";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE students SET group_id = ?, first_name = ?, last_name = ? WHERE student_id = ?;";
    }

    @Override
    protected String getSelectByIdQuery() {
        return "SELECT * FROM students WHERE student_id = ?;";
    }

    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM students WHERE student_id = ?";
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
    protected void setObjectIntoStatement(PreparedStatement statement, Student student) throws DaoException {
        try {
            if (student.getStudentId() != 0) {
                statement.setInt(1, student.getGroupId());
                statement.setString(2, student.getFirstName());
                statement.setString(3, student.getLastName());
                statement.setInt(4, student.getStudentId());
            } else {
                statement.setInt(1, student.getGroupId());
                statement.setString(2, student.getFirstName());
                statement.setString(3, student.getLastName());
            }
        } catch (SQLException e) {
            throw new DaoException(e.getMessage(), e);
        }
    }

    @Override
    protected Student readObject(ResultSet resultSet) throws DaoException {
        Student student = new Student();
        try {
            student.setStudentId(resultSet.getInt("student_id"));
            student.setGroupId(resultSet.getInt("group_id"));
            student.setFirstName(resultSet.getString("first_name"));
            student.setLastName(resultSet.getString("last_name"));
        } catch (SQLException e) {
            throw new DaoException(e.getMessage(), e);
        }
        return student;
    }
}
