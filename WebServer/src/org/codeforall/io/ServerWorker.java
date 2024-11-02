package org.codeforall.io;

//import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class ServerWorker implements Runnable {

    private final Socket clientSocket;
    private final Server server;
    private String name = "";
    private boolean isNameSet = false;
    private final PrintWriter out;
    private final BufferedReader in;
    private String color;

    public ServerWorker(Socket clientSocket, Server server) throws IOException {
        this.clientSocket = clientSocket;
        this.server = server;
            /*
            InputStreamReader inputStream = new InputStreamReader(clientSocket.getInputStream());
            in = new BufferedReader(inputStream);
            */
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(), true);
    }


    @Override
    public void run() {

        try {

            while (!clientSocket.isClosed()) {
                String messageFromClient = in.readLine();
                getMessage(messageFromClient);

                if (messageFromClient == null) {
                    System.out.println("Client " + name + " closed, exiting...");
                }
            }
            clientSocket.close();
            server.removeWorker(this);

        } catch (IOException e) {
            System.out.println("You're not receiving any messages.");
        }

    }


    //This returns the message to himself - see line 66 and 70 to understand the difference.
    public void send(String message) {
        out.println(message);
    }

    //This method handles all types (/...).
    public void getMessage(String message) {

        if (message.startsWith("/Exit")) {
            try {
                server.sendToAll(name + " left the chat"); // sends to all clients that this client left the chat
                System.out.println(name + " left the chat"); //prints on the server which client left the chat
                clientSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }


        if (!isNameSet) {
            if (message.startsWith("/name")) {
                String[] name = message.split(" ", 2);
                if (name.length > 1) {
                    this.name = name[1];
                    isNameSet = true;
                    send(showOptions());
                    server.sendToAll("Welcome " + name + " to the chat!");
                } else {
                    server.sendToAll("name format incorrect. Use /name [your_name]");
                }
            } else {
                send("Please insert your name first to use the chat");
            }

        } else {
            if (message.startsWith("/name")) {
                send("You already set your name");
            } else {
                String msg = name + ": " + message;
                server.sendToAll(msg);
                System.out.println(msg);
            }
        }


        if(message.startsWith("/joke")){
            String[] jokes = {
                    "How do ghosts stay in shape? They exorcise.",
                    "Did you get a haircut? No, I got them all cut.",
                    "What do you call a fish without eyes? A fsh.",
                    "Whoever stole my depression medication — I hope you're happy now"
            };
            int random = (int)Math.floor(Math.random() * 4);
            server.sendToAll(jokes[random]);
        }


        /*if(message.startsWith("/ctext_" + color));
        switch (color){
            case "red":
                color = "\u001B[31m";
                break;
            case "blue":
                color = "\u001B[34m";
                break;
            default:
                color = "\u001B[0m";
        }
        
         */
    }

    public String showOptions() {
        return "Choose between this list of options and have fun: \n" +
                "Type '/joke' for random jokes! \n" +
                "Type '/ctext_red/white/blue/purple' for changing the color of your text! \n" +
                "Type '/Exit' for exiting the chat";

    }
}


