package org.codeforall.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private String name = "localhost";
    private int portNumber = 8085;
    private PrintWriter out;
    private BufferedReader in;
    private Socket clientSocket;

    private List<ServerWorker> serverWorkers = new LinkedList<>();

    public void start() {

        try {
            ServerSocket sc = new ServerSocket(portNumber);

            while (true) {
                clientSocket = sc.accept();
                ExecutorService listenClient = Executors.newCachedThreadPool();
                ServerWorker sw = new ServerWorker(clientSocket, this);
                listenClient.submit(sw);
                serverWorkers.add(sw);
            }


        } catch (IOException ex) {
            System.out.println("Server not reachable");
        }

    }

    public void sendToAll(String message){
        for(ServerWorker serverWorker: serverWorkers){
            serverWorker.send(message);
        }
    }

}
