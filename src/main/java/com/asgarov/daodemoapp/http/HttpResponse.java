package com.asgarov.daodemoapp.http;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import static java.lang.System.lineSeparator;

public class HttpResponse {
    private HttpStatus status = HttpStatus.OK;
    private String content = "";

    public void setStatusCode(int status) {
        this.status = Arrays.stream(HttpStatus.values())
                .filter(httpStatus -> httpStatus.getCode() == status)
                .findFirst()
                .orElseThrow();
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void send(OutputStream network) throws IOException {
        String messageBuilder = "HTTP/1.1 " +
                status.getCode() + " " +
                status.getMessage() + lineSeparator() +
                lineSeparator() +
                content;

        network.write(messageBuilder.getBytes());
        network.flush();
    }
}
