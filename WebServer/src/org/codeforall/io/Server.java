package org.codeforall.io;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final static int port = 8085;
    private final List<ServerWorker> serverWorkers = Collections.synchronizedList(new ArrayList<>());

    public void start() {

        System.out.println("DEBUG: Server instance is: " + this);
        ExecutorService listenClient = Executors.newCachedThreadPool(); //CREATE A THREAD

        try {
            System.out.println("Binding to port " + port + ", please wait...");
            ServerSocket sc = new ServerSocket(port); //IT'S READY FOR ESTABLISHING THE CONNECTION
            System.out.println("Server started " + sc);

            while (true) {

                Socket clientSocket = sc.accept(); //ESTABLISH THE CONNECTION
                System.out.println("Client accepted " + clientSocket);

                ServerWorker sw = new ServerWorker(clientSocket, this); //GIVES TO THE THREAD THE REST OF THE RESPONSIBILITY
                serverWorkers.add(sw);  //ADDS TO THE LIST THE OBJECT;
                listenClient.submit(sw); //STARTS THE THREAD;
            }

        } catch (IOException ex) {
            System.out.println("Unable to start server on port: " + port);
        }
    }

    public void sendToAll(String message) {

        synchronized (serverWorkers) {

            for (ServerWorker serverWorker : serverWorkers) {
                serverWorker.send(message);
            }
        }
    }

    public void removeWorker(ServerWorker sw) {

        synchronized (serverWorkers) {
            serverWorkers.remove(sw);
        }
    }

}

