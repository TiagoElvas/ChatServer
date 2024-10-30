package org.codeforall.io;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerWorker implements Runnable {

    private  Socket clientSocket;
    private Server server;
    private String nameClient = "";
    private PrintWriter out;
    private BufferedReader in;

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

            while (!messageFromClient.equals("/Exit")) { //IF CLIENT TYPES /EXIT E EXIT FROM THE CHAT.

                if (messageFromClient.startsWith("/name")) { //CLIENT NEED TO PUT IS NAME FIRST.
                    String name[] = messageFromClient.split(" ");
                    if (name.length > 1) {
                        nameClient = name[1];
                    }

                } else {
                    String msg = nameClient + ": " + messageFromClient;
                    server.sendToAll(msg);

                }
                    messageFromClient = in.readLine();
                    System.out.println(nameClient + ": " + messageFromClient);
            }
            clientSocket.close();

        } catch (IOException e) {
            System.out.println("You're not receiving any messages.");
        }
    }

    public void send(String message) {
        out.println(message);
    }

}

