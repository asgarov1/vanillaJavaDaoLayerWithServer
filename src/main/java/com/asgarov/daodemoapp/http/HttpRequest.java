package com.asgarov.daodemoapp.http;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.asgarov.daodemoapp.controller.Constants.CONTENT_LENGTH;
import static java.lang.Integer.parseInt;
import static java.lang.System.in;
import static java.lang.System.lineSeparator;

public class HttpRequest {

    /**
     * Regex to match content surrounded by curly braces {}
     */
    private final Pattern JSON_CONTENT_REGEX = Pattern.compile("\\{([^}]*)}");

    private final String receivedRequest;

    private HttpRequest(InputStream inputStream) throws IOException {
        receivedRequest = readRequest(inputStream);
    }

    public static HttpRequest valueOf(InputStream inputStream) throws IOException {
        return new HttpRequest(inputStream);
    }

    private String readRequest(InputStream inputStream) throws IOException {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            var requestBuilder = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                requestBuilder.append(line).append(lineSeparator());
            }
            if (requestBuilder.toString().contains(CONTENT_LENGTH)) {
                for (int i = 0; i < extractContentLength(requestBuilder); i++) {
                    requestBuilder.append((char) in.read());
                }
            }
            return requestBuilder.toString();
        } finally {
            in.close();
        }
    }

    private int extractContentLength(StringBuilder request) {
        String contentLengthLine = request.substring(request.indexOf(CONTENT_LENGTH));
        int start = contentLengthLine.indexOf(": ") + ": ".length();
        String substring = contentLengthLine.substring(start);
        int end = 0;
        for (int i = 0; i < substring.length(); i++) {
            if(!Character.isDigit(substring.charAt(i))){
                end = start + i; break;
            }
        }
        return parseInt(contentLengthLine.substring(start, end));
    }

    private ByteArrayInputStream getInputStream() {
        return new ByteArrayInputStream(receivedRequest.getBytes(StandardCharsets.UTF_8));
    }

    public String getMethod() {
        String headerLine = getHeaderLine();
        return headerLine.substring(0, headerLine.indexOf(" /"));
    }

    public String getJsonContent() {
        Matcher matcher = JSON_CONTENT_REGEX.matcher(receivedRequest);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    private String getHeaderLine() {
        String line;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(getInputStream()))) {
            while ((line = in.readLine()) != null && line.isEmpty()) { /* skipping to the headerLine */ }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return line;
    }

    public HttpUrl getUrl() {
        return new HttpUrl(getHeaderLine());
    }
}
