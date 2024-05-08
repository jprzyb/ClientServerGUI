package com.example.demo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class Server{
    public static final int PORT = 12345;
    private static boolean isRunning;
    Thread thread;
    List<Thread> clientsList;

    public Server(){
        clientsList = new ArrayList<>();
        isRunning = false;
    }

    public void start(){
        isRunning = true;
        thread = new Thread(()->{
            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                serverSocket.setSoTimeout(100);
                while (true) {
                    System.out.println("listening " + thread.isAlive());
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Połączono z klientem: " + clientSocket);

                    Thread clientHandler = new Thread(new ClientHandler(clientSocket));
                    clientsList.add(clientHandler);
                    clientHandler.start();
                }
            } catch (IOException e) {
                e.getMessage();
            }
        });
        thread.start();
    }

    public void stop(){
        System.out.println("stopping1");
        isRunning = false;
        for(Thread a : clientsList){
            System.out.println("stopping2 "+a);
            a.interrupt();
        }
        thread.interrupt();
        System.out.println("stopping3");
        System.out.println(thread.isAlive());
    }
}
