package com.asgarov.daodemoapp.controller;


import com.asgarov.daodemoapp.http.HttpRequest;
import com.asgarov.daodemoapp.http.HttpResponse;
import com.asgarov.daodemoapp.http.HttpStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.asgarov.daodemoapp.controller.Constants.*;
import static com.asgarov.daodemoapp.http.HttpStatus.*;

public interface Controller {

    ObjectMapper OBJECT_MAPPER = new ObjectMapper();


    default HttpResponse handleRequest(HttpRequest request) throws Exception {
        if (request.getMethod().equals(POST)) {
            return handlePost(request);
        } else if (request.getMethod().equals(GET)) {
            return handleGet(request);
        } else if (request.getMethod().equals(PUT)) {
            return handlePut(request);
        } else if (request.getMethod().equals(DELETE)) {
            return handleDelete(request);
        }

        return notFound();
    }

    HttpResponse handlePost(HttpRequest request) throws Exception;
    HttpResponse handleGet(HttpRequest request) throws Exception;
    HttpResponse handlePut(HttpRequest request) throws Exception;
    HttpResponse handleDelete(HttpRequest request) throws Exception;

    default HttpResponse notFound() {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode(NOT_FOUND.getCode());
        return httpResponse;
    }

    default HttpResponse noContent() {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode(NO_CONTENT.getCode());
        return httpResponse;
    }

    static HttpResponse internalServerError(String message) {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode(INTERNAL_SERVER_ERROR.getCode());
        httpResponse.setContent(message);
        return httpResponse;
    }

    default HttpResponse created() {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode(CREATED.getCode());
        return httpResponse;
    }

    default HttpResponse created(String content) {
        HttpResponse httpResponse = created();
        httpResponse.setContent(content);
        return httpResponse;
    }

    default HttpResponse ok(Object object) throws JsonProcessingException {
        HttpResponse response = new HttpResponse();
        response.setStatusCode(HttpStatus.OK.getCode());
        response.setContent(OBJECT_MAPPER.writeValueAsString(object));
        return response;
    }
}
