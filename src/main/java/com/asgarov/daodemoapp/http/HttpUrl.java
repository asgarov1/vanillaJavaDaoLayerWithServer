package com.asgarov.daodemoapp.http;

import java.util.HashMap;
import java.util.Map;

public class HttpUrl {

    private final String headerLine;

    HttpUrl(String headerLine) {
        this.headerLine = headerLine;
    }

    public static HttpUrl valueOf(String headerLine) {
        return new HttpUrl(headerLine);
    }

    public String getRawUrl() {
        return headerLine.substring(headerLine.indexOf("/"), headerLine.lastIndexOf(" "));
    }

    public String getPath() {
        String rawUrl = getRawUrl();
        if(rawUrl.contains("?")){
            return rawUrl.substring(0, rawUrl.indexOf("?"));
        }
        return rawUrl;
    }

    public Map<String, String> getParameter() {
        Map<String, String> parameterMap = new HashMap<>();
        String rawUrl = getRawUrl();
        if(getRawUrl().contains("?")) {
            String[] parameters = rawUrl.substring(rawUrl.indexOf("?")+1).split("&");
            for (String parameterPair : parameters) {
                String key = parameterPair.split("=")[0];
                String value = parameterPair.split("=")[1];
                parameterMap.put(key, value);
            }
        }
        return parameterMap;
    }

    public int getParameterCount() {
        return getParameter().keySet().size();
    }

    public String[] getSegments() {
        return getPath().substring(1).split("/");
    }

    public String getFileName() {
        return getSegments()[getSegments().length - 1].contains(".") ? getSegments()[getSegments().length - 1] : "";
    }

    public String getExtension() {
        String lastSegment = getSegments()[getSegments().length - 1];
        return lastSegment.contains(".") ? lastSegment.substring(lastSegment.lastIndexOf(".")) : "";
    }

    public String getFragment() {
        return getRawUrl().contains("#") ? getRawUrl().substring(getRawUrl().lastIndexOf("#")+1) : "";
    }
}