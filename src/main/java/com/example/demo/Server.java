package com.example.demo;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server{
    public static final int PORT = 12345;
    Thread thread;
    List<Thread> clientsThreads;
    ServerSocket serverSocket;

    public Server(){
        clientsThreads = new ArrayList<>();
        thread = new Thread(()->{
            while(!thread.isInterrupted()){
                try {
                    serverSocket = new ServerSocket(PORT);
                    Logger.addLog("Server started, waiting for client...");

                    Socket socket = serverSocket.accept();
                    Logger.addLog("Client connected: " + socket);
                    Thread clientThread = new Thread(()->{
                        try{
                            Logger.addLog("Client started: " + socket);
                            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                            String fileName = dataInputStream.readUTF();
                            Logger.addLog("Recieved file name: " + fileName);
                            fileName = "recived_files/" +fileName;
                            Logger.addLog("File directory mapped: " + fileName);
                            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
                            byte[] buffer = new byte[4096];
                            int bytesRead;
                            while ((bytesRead = dataInputStream.read(buffer)) != -1) {
                                fileOutputStream.write(buffer, 0, bytesRead);
                            }
                            fileOutputStream.close();
                            Logger.addLog("File recived: " + fileName);
                        }catch (Exception e){
                            Logger.addLog("Something went wrong while recieving data!\n" + e.getMessage());
                        }
                    });
                    clientThread.start();
                    clientsThreads.add(clientThread);
                    clientsThreads.removeIf(t -> !t.isAlive());

                } catch (IOException e) {
                    Logger.addLog("Something went wrong while handling client!\n" + e.getMessage());
                }
            }
        });
    }

    public void start(){
        thread.start();
    }

    public void stop(){
        try {
            serverSocket.close();
            thread.interrupt();
            Logger.addLog("Stopping server!");
            long startTime = System.currentTimeMillis();
            while (thread.isAlive()){}
            long stopTime = System.currentTimeMillis();
            Logger.addLog("Server stopped after " + (stopTime-startTime) + "ms.");
        } catch (IOException e) {
            Logger.addLog("Something went wrong while stopping the server!" + e.getMessage());
        }
        for(Thread a : clientsThreads){
            a.interrupt();
        }
        thread.interrupt();
    }
}
