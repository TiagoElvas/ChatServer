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

        //CONNECTION ESTABLISHED
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            clientSocket = new Socket(hostName, portNumber);

            //STREAMS
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));


            //Creating a new thread with its own functionality
            ExecutorService readMessages = Executors.newSingleThreadExecutor();
            readMessages.submit(new Listen());

            /*AFTER DE MAIN THREAD CREATE ANOTHER, IT WILL ENTER ITS OWN LOOP TO KEEP LISTENING MESSAGES FROM TERMINAL*/
            while (true) {

                //READ THE MESSAGE THAT IS TYPED ON THE TERMINAL
                String message = br.readLine();

                //PRINT THE MESSAGE
                out.println(message);

                //IF CLIENT TYPES /EXIT, THE CONNECTION CLOSES.
                if (message.equals("/Exit")) {
                    clientSocket.close();
                }

            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private class Listen implements Runnable {

        //THE NEW THREAD WILL LISTEN TO ANY MESSAGES FROM THE SERVER

        private String messageFromServer = "";
        @Override
        public void run() {
            try {


                System.out.println("Enter your name please (type /name before entering your name)");



                //WHILE THE CONNECTION ISN'T CLOSED, KEEP LISTENING.
                while (!clientSocket.isClosed()) {

                    //READ THE LINE;
                    messageFromServer = in.readLine();

                    //THIS PREVENTS FROM APPEARS ONE LAST TIME NULL AFTER CLOSE THE SERVER
                    if (messageFromServer != null) {
                        System.out.println(messageFromServer);
                    } else {
                        System.out.println("The server went down.");
                        System.exit(0);
                    }
                }

            } catch (IOException e) {
                System.out.println("There's no messages to read.");
            }
            }
        }
    }

