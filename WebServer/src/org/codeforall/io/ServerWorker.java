package org.codeforall.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerWorker implements Runnable {

    private Socket clientSocket;
    private Server server;
    private String name = "c1";
    PrintWriter out;
    BufferedReader in;

    public ServerWorker(Socket clientSocket, Server server) {
        this.clientSocket = clientSocket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            /*
            InputStreamReader inputStreamReader = new InputStreamReader(clientSocket.getInputStream());
            in = new BufferedReader(inputStreamReader);
            */
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String messageFromClient = in.readLine();
            System.out.println(messageFromClient);

            while (!messageFromClient.equals("/Exit")) {
                System.out.println(messageFromClient);

                if (messageFromClient.startsWith("/name")) {
                    System.out.println();
                }
                server.sendToAll(messageFromClient);
                messageFromClient = in.readLine();
            }
            clientSocket.close();
            System.out.println("Client disconnect ");
        } catch (IOException e) {
            System.out.println("You're not receiving any messages.");
        }
    }
    public void send(String message) {
        out.println(message);
    }
}

