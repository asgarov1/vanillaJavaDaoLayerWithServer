package com.asgarov.daodemoapp;

import com.asgarov.daodemoapp.dispatcher.FrontDispatcher;

import java.io.IOException;
import java.net.ServerSocket;

import static com.asgarov.daodemoapp.util.PropertiesReader.getProperty;
import static java.lang.Integer.parseInt;

public class HttpServer {

    public static final String PORT = "server.port";

    public static void main(String[] args) throws IOException {
        int port = parseInt(getProperty(PORT));
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("Server is ready to accept connections...");
            while (true) {
                new Thread(new FrontDispatcher(server.accept())).start();
            }
        }
    }
}
