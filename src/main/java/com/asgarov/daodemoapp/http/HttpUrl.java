package com.asgarov.daodemoapp.http;

public class HttpUrl {

    /**
     * Looks like this:  `GET /student/7 HTTP/1.1`
     */
    private final String headerLine;

    HttpUrl(String headerLine) {
        this.headerLine = headerLine;
    }

    public String getRawUrl() {
        return headerLine.substring(headerLine.indexOf("/"), headerLine.lastIndexOf(" "));
    }

    public String getPath() {
        String rawUrl = getRawUrl();
        if (rawUrl.contains("?")) {
            return rawUrl.substring(0, rawUrl.indexOf("?"));
        }
        return rawUrl;
    }

    public String[] getSegments() {
        return getPath().substring(1).split("/");
    }

}