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
    private final static int portNumber = 8085;
    private Socket clientSocket;
    private List<ServerWorker> serverWorkers = new LinkedList<>();

    public void start() {

        try {
            ServerSocket sc = new ServerSocket(portNumber); //IT'S READY FOR ESTABLISHING THE CONNECTION

            while (true) {
                clientSocket = sc.accept(); //ESTABLISH THE CONNECTION
                ExecutorService listenClient = Executors.newCachedThreadPool(); //CREATE A THREAD
                ServerWorker sw = new ServerWorker(clientSocket, this); //GIVES TO THE THREAD THE REST OF THE RESPONSIBILITY
                listenClient.submit(sw); //STARTS THE THREAD;
                serverWorkers.add(sw);  //ADDS TO THE LIST THE OBJECT;
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


    //Criar um método que lista todos os clients dentro do chat. usa synchronized enquanto estou a iterar não quero que adiciones
    //ou retires pessoas ao chat.

    //Dar cores das letras a cada usuário.

    //permitir trocar imagens ou gifts

    //fazer thread safety. Usar uma lista Collections.synchronizedList
}
