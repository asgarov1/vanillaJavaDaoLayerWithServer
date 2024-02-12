package com.asgarov.daodemoapp.controller;


import com.asgarov.daodemoapp.http.HttpRequest;
import com.asgarov.daodemoapp.http.HttpResponse;

public interface Controller {
    HttpResponse handleRequest(HttpRequest request);
}
