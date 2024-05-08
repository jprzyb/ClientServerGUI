package com.example.demo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable{
    public static final int PORT = 12345;
    private static boolean isRunning;
    List<Thread> clientsList;

    public Server(){
        clientsList = new ArrayList<>();
        isRunning = false;
    }


    @Override
    public void run() {
        isRunning = true;
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Serwer jest uruchomiony i nasłuchuje na porcie " + PORT);

            while (isRunning) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Połączono z klientem: " + clientSocket);

                Thread clientHandler = new Thread(new ClientHandler(clientSocket));
                clientsList.add(clientHandler);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop(){
        isRunning = false;
        for(Thread a : clientsList){
            a.interrupt();
        }
    }
}
