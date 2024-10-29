package org.codeforall.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

    private PrintWriter out;
    private BufferedReader in;
    private Socket clientSocket;


    public void start() {
        String hostName = "localhost";
        int portNumber = 8085;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            clientSocket = new Socket(hostName, portNumber);//CONNECTION ESTABLISHED

            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));


            //Creating a new thread with its own functionality
            ExecutorService readMessages = Executors.newSingleThreadExecutor();
            readMessages.submit(new Listen());


            /*This will be inside a loop. But before entering the loop,I need to
            create a new Thread to listen and print, which is above. */
            while (true) {
                String message = br.readLine();
                out.println(message);
                if (message.equals("/Exit")) {
                    clientSocket.close();
                }
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private class Listen implements Runnable {

        @Override
        public void run() {
            try {
                String messageFromServer = in.readLine();
                System.out.println(messageFromServer);

                while (!clientSocket.isClosed() || messageFromServer != null) {
                    messageFromServer = in.readLine();
                    System.out.println(messageFromServer);
                }

            } catch (IOException e) {
                System.out.println("There's no messages to read.");
            } finally{
                try {
                    clientSocket.close();
                } catch (IOException e) {

                }
            }
        }
    }
}
