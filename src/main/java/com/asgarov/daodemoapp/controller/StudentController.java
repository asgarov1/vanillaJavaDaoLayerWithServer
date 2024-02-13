package com.asgarov.daodemoapp.controller;

import com.asgarov.daodemoapp.dao.exception.DaoException;
import com.asgarov.daodemoapp.dao.impl.CourseDao;
import com.asgarov.daodemoapp.dao.impl.StudentCourseDao;
import com.asgarov.daodemoapp.dao.impl.StudentDao;
import com.asgarov.daodemoapp.domain.Course;
import com.asgarov.daodemoapp.domain.Student;
import com.asgarov.daodemoapp.domain.StudentCourse;
import com.asgarov.daodemoapp.http.HttpRequest;
import com.asgarov.daodemoapp.http.HttpResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

import static com.asgarov.daodemoapp.dao.AbstractDao.COULD_NOT_FIND_AN_OBJECT_WITH_SUCH_ID;

public class StudentController implements Controller {

    public static final String ENDPOINT = "student";
    private final StudentDao studentDao = new StudentDao();
    private final StudentCourseDao studentCourseDao = new StudentCourseDao();
    private final CourseDao courseDao = new CourseDao();

    @Override
    public HttpResponse handlePost(HttpRequest request) throws JsonProcessingException, DaoException {
        String requestBodyAsString = request.getJsonContent();
        Student student = OBJECT_MAPPER.readValue(requestBodyAsString, Student.class);
        Student createdStudent = studentDao.create(student);
        return created(OBJECT_MAPPER.writeValueAsString(createdStudent));
    }

    @Override
    public HttpResponse handleGet(HttpRequest request) throws JsonProcessingException, DaoException {
        String[] segments = request.getUrl().getSegments();
        if (segments.length == 2) {
            return getStudent(Integer.valueOf(segments[1]));
        } else if (segments.length == 3 && "courses".equals(segments[2])) {
            // GET /student/{id}/courses
            Integer id = Integer.valueOf(segments[1]);
            List<Course> courses = studentCourseDao.findAllByStudentId(id)
                    .stream()
                    .map(StudentCourse::getCourseId)
                    .map(courseDao::read)
                    .toList();
            return ok(courses);
        }
        return notFound();
    }

    private HttpResponse getStudent(Integer id) throws JsonProcessingException, DaoException {
        try {
            Student student = studentDao.read(id);
            System.out.printf("\tReturning result: %s%n", student);
            return ok(student);
        } catch (DaoException e) {
            if (COULD_NOT_FIND_AN_OBJECT_WITH_SUCH_ID.equals(e.getMessage())) {
                return notFound();
            }
            throw e;
        }
    }

    @Override
    public  HttpResponse handlePut(HttpRequest request) throws JsonProcessingException, DaoException {
        String[] segments = request.getUrl().getSegments();
        if (segments.length != 2) {
            return notFound();
        }

        String requestBodyAsString = request.getJsonContent();
        Student student = OBJECT_MAPPER.readValue(requestBodyAsString, Student.class);

        Integer id = Integer.valueOf(segments[1]);
        student.setStudentId(id);
        studentDao.update(student);

        return noContent();
    }

    @Override
    public  HttpResponse handleDelete(HttpRequest request) throws DaoException {
        String[] segments = request.getUrl().getSegments();
        if (segments.length != 2) {
            return notFound();
        }

        Integer id = Integer.valueOf(segments[1]);
        studentDao.delete(id);
        return noContent();
    }
}
