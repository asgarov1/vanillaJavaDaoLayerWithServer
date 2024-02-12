package com.asgarov.daodemoapp.dao.impl;

import com.asgarov.daodemoapp.dao.AbstractDao;
import com.asgarov.daodemoapp.dao.exception.DaoException;
import com.asgarov.daodemoapp.domain.Course;
import com.asgarov.daodemoapp.util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDao extends AbstractDao<Course, Integer> {


    @Override
    protected String getCreateQuery(Course course) {
        return "INSERT INTO course (course_name, course_description) VALUES (?,?);";
    }

    @Override
    protected String getSelectByIdQuery() {
        return "SELECT * FROM course WHERE course_id = ?;";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE course SET course_name = ?, course_description = ? WHERE course_id = ?;";
    }

    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM course WHERE course_id = ?;";
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
    protected void setObjectIntoStatement(PreparedStatement statement, Course course) throws DaoException {
        try {
            if (course.getId() != 0) {
                statement.setString(1, course.getName());
                statement.setString(2, course.getDescription());
                statement.setInt(3, course.getId());
            } else {
                statement.setString(1, course.getName());
                statement.setString(2, course.getDescription());
            }
        } catch (SQLException e) {
            throw new DaoException(e.getMessage(), e);
        }
    }

    @Override
    protected Course readObject(ResultSet resultSet) throws DaoException {
        Course course = new Course();
        try {
            course.setId(resultSet.getInt("course_id"));
            course.setName(resultSet.getString("course_name"));
            course.setDescription(resultSet.getString("course_description"));
        } catch (SQLException e) {
            throw new DaoException(e.getMessage(), e);
        }
        return course;
    }

    /**
     * When calling this method don't forget to also delete studentCourses entry
     *  `studentCourseDao.delete(course);`
     * @param courseId the id of the course
     * @throws DaoException {@link DaoException}
     */
    @Override
    public void delete(Integer courseId) throws DaoException {
        super.delete(courseId);
    }
}
