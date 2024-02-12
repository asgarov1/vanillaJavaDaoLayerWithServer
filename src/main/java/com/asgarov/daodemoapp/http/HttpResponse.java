package com.asgarov.daodemoapp.http;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.System.lineSeparator;

public class HttpResponse {

    private HttpStatus status = HttpStatus.OK;
    private final Map<String, String> headers = new HashMap<>();
    private String content = "";
    private String contentType;
    private String serverHeader = "Java HTTP Server v1.0";

    public Map<String, String> getHeaders() {
        return headers;
    }

    public int getContentLength() {
        return content.length();
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getStatusCode() {
        return status.getCode();
    }

    public void setStatusCode(int status) {
        this.status = Arrays.stream(HttpStatus.values())
                .filter(httpStatus -> httpStatus.getCode() == status)
                .findFirst()
                .orElseThrow();
    }

    public String getStatus() {
        return this.status.getMessage();
    }

    public void addHeader(String header, String value) {
        headers.put(header, value);
    }

    public String getServerHeader() {
        return this.serverHeader;
    }

    public void setServerHeader(String server) {
        this.serverHeader = server;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setContent(byte[] content) {
        setContent(new ByteArrayInputStream(content));
    }

    public void setContent(InputStream stream) {
        this.content = new BufferedReader(new InputStreamReader(stream))
                .lines().collect(Collectors.joining("\n"));
    }

    public void send(OutputStream network) throws IOException {
        var messageBuilder = new StringBuilder();

        messageBuilder.append("HTTP/1.1 ")
                .append(status.getCode()).append(" ")
                .append(status.getMessage()).append(lineSeparator());

        headers.keySet().forEach(key -> {
            messageBuilder.append(key).append(": ").append(headers.get(key)).append(lineSeparator());
        });

        messageBuilder.append(lineSeparator());
        messageBuilder.append(content);

        network.write(messageBuilder.toString().getBytes());
        network.flush();
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "status=" + status +
                ", headers=" + headers +
                ", content='" + content + '\'' +
                ", contentType=" + contentType +
                ", serverHeader='" + serverHeader + '\'' +
                '}';
    }
}
