package com.asgarov.daodemoapp;

import com.asgarov.daodemoapp.dispatcher.FrontDispatcher;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.asgarov.daodemoapp.util.PropertiesReader.getProperty;
import static java.lang.Integer.parseInt;

public class HttpServer {

    public static final String PORT = "server.port";


    public static void main(String[] args) throws IOException {
        int port = parseInt(getProperty(PORT));

        try (ServerSocket server = new ServerSocket(port);
             ExecutorService executorService = Executors.newSingleThreadExecutor()) {
            System.out.println("Server is ready to accept connections...");
            while (true) {
                try {
                    Socket acceptedConnection = server.accept(); // accept is a blocking call
                    executorService.submit(() -> {
                        FrontDispatcher frontDispatcher = new FrontDispatcher(acceptedConnection);
                        frontDispatcher.handleRequest();
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
