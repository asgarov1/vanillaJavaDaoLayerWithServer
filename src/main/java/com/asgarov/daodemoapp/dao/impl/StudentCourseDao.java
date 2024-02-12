package com.asgarov.daodemoapp.dao.impl;

import com.asgarov.daodemoapp.dao.AbstractDao;
import com.asgarov.daodemoapp.dao.exception.DaoException;
import com.asgarov.daodemoapp.domain.StudentCourse;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentCourseDao extends AbstractDao<StudentCourse, Integer> {

    @Override
    protected String getCreateQuery(StudentCourse course) {
        return "INSERT INTO student_course (student_id, course_id) VALUES (?,?);";
    }

    @Override
    protected String getSelectByIdQuery() {
        return "SELECT * FROM student_course WHERE student_course_id = ?;";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE student_course SET student_id = ?, course_id = ? WHERE student_course_id = ?;";
    }

    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM student_course WHERE student_course_id = ?;";
    }

    @Override
    protected void setIdIntoStatement(PreparedStatement statement, Integer studentCourseId) throws DaoException {
        try {
            statement.setInt(1, studentCourseId);
        } catch (SQLException e) {
            throw new DaoException(e.getMessage(), e);
        }
    }

    @Override
    protected void setObjectIntoStatement(PreparedStatement statement, StudentCourse studentCourse) throws DaoException {
        try {
            if (studentCourse.getStudentCourseId() != 0) {
                // update use case
                statement.setInt(1, studentCourse.getStudentId());
                statement.setInt(2, studentCourse.getCourseId());
                statement.setInt(3, studentCourse.getStudentCourseId());
            } else {
                // create use case
                statement.setInt(1, studentCourse.getStudentId());
                statement.setInt(2, studentCourse.getCourseId());
            }
        } catch (SQLException e) {
            throw new DaoException(e.getMessage(), e);
        }
    }

    @Override
    protected StudentCourse readObject(ResultSet resultSet) throws DaoException {
        StudentCourse studentCourse = new StudentCourse();
        try {
            studentCourse.setStudentCourseId(resultSet.getInt("student_course_id"));
            studentCourse.setStudentId(resultSet.getInt("student_id"));
            studentCourse.setCourseId(resultSet.getInt("course_id"));
        } catch (SQLException e) {
            throw new DaoException(e.getMessage(), e);
        }
        return studentCourse;
    }
}
