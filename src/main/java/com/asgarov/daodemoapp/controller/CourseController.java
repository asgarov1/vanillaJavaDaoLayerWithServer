package com.asgarov.daodemoapp.controller;

import com.asgarov.daodemoapp.dao.exception.DaoException;
import com.asgarov.daodemoapp.dao.impl.CourseDao;
import com.asgarov.daodemoapp.domain.Course;
import com.asgarov.daodemoapp.http.HttpRequest;
import com.asgarov.daodemoapp.http.HttpResponse;
import com.asgarov.daodemoapp.http.HttpStatus;
import com.fasterxml.jackson.core.JsonProcessingException;

import static com.asgarov.daodemoapp.dao.AbstractDao.COULD_NOT_FIND_AN_OBJECT_WITH_SUCH_ID;

public class CourseController implements Controller {

    public static final String ENDPOINT = "course";

    private final CourseDao courseDao = new CourseDao();

    @Override
    public HttpResponse handlePost(HttpRequest request) throws JsonProcessingException, DaoException {
        String requestBodyAsString = request.getJsonContent();
        Course course = OBJECT_MAPPER.readValue(requestBodyAsString, Course.class);
        Course createdCourse = courseDao.create(course);
        return created(OBJECT_MAPPER.writeValueAsString(createdCourse));
    }

    @Override
    public HttpResponse handleGet(HttpRequest request) throws JsonProcessingException, DaoException {
        String[] segments = request.getUrl().getSegments();
        if (segments.length != 2) {
            return notFound();
        }
        try {
            Integer id = Integer.valueOf(segments[1]);
            Course course = courseDao.read(id);
            HttpResponse response = new HttpResponse();
            response.setStatusCode(HttpStatus.OK.getCode());
            response.setContent(OBJECT_MAPPER.writeValueAsString(course));
            return response;
        } catch (DaoException e) {
            if (COULD_NOT_FIND_AN_OBJECT_WITH_SUCH_ID.equals(e.getMessage())) {
                return notFound();
            }
            throw e;
        }
    }

    @Override
    public HttpResponse handlePut(HttpRequest request) throws JsonProcessingException, DaoException {
        String[] segments = request.getUrl().getSegments();
        if (segments.length != 2) {
            return notFound();
        }

        String requestBodyAsString = request.getJsonContent();
        Course course = OBJECT_MAPPER.readValue(requestBodyAsString, Course.class);

        Integer id = Integer.valueOf(segments[1]);
        course.setId(id);
        courseDao.update(course);

        return noContent();
    }

    @Override
    public HttpResponse handleDelete(HttpRequest request) throws DaoException {
        String[] segments = request.getUrl().getSegments();
        if (segments.length != 2) {
            return notFound();
        }

        Integer id = Integer.valueOf(segments[1]);
        courseDao.delete(id);
        return noContent();
    }
}
