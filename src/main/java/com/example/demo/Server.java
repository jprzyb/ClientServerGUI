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
    String destination;
    ReceiveController controller;
    public static Server xyz;

    public Server(ReceiveController controller){
        xyz = this;
        this.controller = controller;
        clientsThreads = new ArrayList<>();
        destination = "received_files/";
        thread = setThread();
    }

    public void start(){
        thread = setThread();
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
    public void setDestination(String destination){
        this.destination = destination;
        Logger.addLog("Destination directory: " + this.destination);
    }

    public static void abort(){
        xyz.thread.interrupt();
        for(Thread t : xyz.clientsThreads) t.interrupt();
        while (xyz.thread.isAlive()){
        }
    }
    private Thread setThread(){
        return new Thread(()->{
            try {
                serverSocket = new ServerSocket(PORT);
            } catch (IOException e) {
                Logger.addLog("Unable to start a server: " + e.getMessage());
            }
            Logger.addLog("Server started, waiting for client...");
            while(!thread.isInterrupted()){
                try {
                    Socket socket = serverSocket.accept();
                    Logger.addLog("Client connected: " + socket);
                    controller.addElementToGuiLogs("Client connected: " + socket);
                    Thread clientThread = new Thread(()->{
                        try{
                            Logger.addLog("Client started: " + socket);
                            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                            String fileName = dataInputStream.readUTF();
                            Logger.addLog("Received file name: " + fileName);
                            fileName = destination +fileName;
                            Logger.addLog("File directory mapped: " + fileName);
                            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
                            byte[] buffer = new byte[4096];
                            int bytesRead;
                            while ((bytesRead = dataInputStream.read(buffer)) != -1) {
                                fileOutputStream.write(buffer, 0, bytesRead);
                            }
                            fileOutputStream.close();
                            Logger.addLog("File received: " + fileName);
                            controller.addElementToGuiLogs("File received: " + fileName);
                        }catch (Exception e){
                            Logger.addLog("Something went wrong while receiving data!\n" + e.getMessage());
                        }
                        try {
                            controller.addElementToGuiLogs("Client disconnected: " + socket);
                            socket.close();
                        } catch (IOException e) {
                            Logger.addLog("Unable to close client connection!");
                        }
                    });
                    clientThread.start();
                    clientsThreads.add(clientThread);
                    clientsThreads.removeIf(t -> !t.isAlive());

                } catch (IOException e) {
                    if (!thread.isInterrupted()) Logger.addLog("Something went wrong while handling client!\n" + e.getMessage());
                }
            }
        });
    }
}
