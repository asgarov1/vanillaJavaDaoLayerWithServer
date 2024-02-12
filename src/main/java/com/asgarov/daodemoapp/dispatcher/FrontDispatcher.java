package com.asgarov.daodemoapp.dispatcher;

import com.asgarov.daodemoapp.controller.Controller;
import com.asgarov.daodemoapp.controller.StudentController;
import com.asgarov.daodemoapp.http.HttpRequest;
import com.asgarov.daodemoapp.http.HttpResponse;
import com.asgarov.daodemoapp.http.HttpStatus;

import java.io.IOException;
import java.net.Socket;

import static com.asgarov.daodemoapp.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class FrontDispatcher implements Runnable {

    private final Socket connectionSocket;

    public FrontDispatcher(Socket connectionSocket) {
        this.connectionSocket = connectionSocket;
    }

    @Override
    public void run() {
        try {
            var request = HttpRequest.valueOf(connectionSocket.getInputStream());
            HttpResponse response = getResponse(request);
            response.send(connectionSocket.getOutputStream());
            connectionSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private HttpResponse getResponse(HttpRequest request) {
        var response = new HttpResponse();
        try {
            response = handle(request);
        } catch (IllegalArgumentException e) {
            response.setStatusCode(HttpStatus.NOT_FOUND.getCode());
        }
        return response;
    }

    private HttpResponse handle(HttpRequest request) {
        String path;
        try {
            path = request.getUrl().getSegments()[0];
        } catch (NullPointerException e){
            //Postman's first NPE creating request handler
            return new HttpResponse();
        }

        Controller controller = switch (path) {
            case "student" -> new StudentController();
            case "course" -> new CourseController();
            default -> throw new IllegalArgumentException("Unexpected url path: " + path);
        };
        try {
            return controller.handleRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
            HttpResponse httpResponse = new HttpResponse();
            httpResponse.setStatusCode(INTERNAL_SERVER_ERROR.getCode());
            return httpResponse;
        }
    }
}
