package com.asgarov.daodemoapp.http;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.asgarov.daodemoapp.http.Constants.*;
import static java.lang.Integer.parseInt;
import static java.lang.System.in;
import static java.lang.System.lineSeparator;

public class HttpRequest {

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

    public Map<String, String> getHeaders() {
        return readHeaders();
    }

    public int getHeaderCount() {
        return getHeaders().size();
    }

    public String getUserAgent() {
        return getHeaders().get(USER_AGENT);
    }

    public int getContentLength() {
        return getHeaders().containsKey(CONTENT_LENGTH) ? parseInt(getHeaders().get(CONTENT_LENGTH)) : 0;
    }

    public String getContentType() {
        return getHeaders().getOrDefault(CONTENT_TYPE, "");
    }

    public InputStream getContentStream() {
        return new ByteArrayInputStream(getContentString().getBytes(StandardCharsets.UTF_8));
    }

    public String getContentString() {
        return Arrays.stream(receivedRequest
                        .split("\n"))
                .filter(line -> line.contains("[") || line.contains("{"))
                .map(line -> line.replace("\r", ""))
                .findFirst()
                .orElseGet(this::getLastLine);
    }

    private String getLastLine() {
        return receivedRequest.split("\n")[receivedRequest.split("\n").length - 1];
    }

    public byte[] getContentBytes() {
        return getContentString().getBytes();
    }

    private Map<String, String> readHeaders() {
        Map<String, String> headers = new HashMap<>();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                if (line.contains(":") && !line.contains("{")) {
                    headers.put(line.split(": ")[0], line.split(": ")[1]);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return headers;
    }

    private String getHeaderLine() {
        String line = "";
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
