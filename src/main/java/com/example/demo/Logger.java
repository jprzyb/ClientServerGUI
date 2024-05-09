package com.example.demo;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Logger {
    private static final String LOG_FILE_PATH = "logs/log_" + String.valueOf(LocalDate.now()).replaceAll("-","")+".log";
    public static void addLog(String msg){
        checkIfExist();

        try (FileWriter fileWriter = new FileWriter(LOG_FILE_PATH, true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
             PrintWriter printWriter = new PrintWriter(bufferedWriter)) {

            printWriter.println(LocalDateTime.now()  + ": " + msg);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void checkIfExist(){
        File file = new File(LOG_FILE_PATH);

        // Sprawdzenie, czy plik istnieje
        if (file.exists()) {
            System.out.println("Plik istnieje.");
        } else {
            // Pobranie ścieżki katalogu
            String directoryPath = file.getParent();
            File directory = new File(directoryPath);

            // Sprawdzenie, czy katalog istnieje
            if (!directory.exists()) {
                // Tworzenie katalogu, jeśli nie istnieje
                if (directory.mkdirs()) {
                    System.out.println("Utworzono katalog: " + directoryPath);
                } else {
                    System.out.println("Nie udało się utworzyć katalogu.");
                    return;
                }
            }

            // Utworzenie pliku
            try {
                if (file.createNewFile()) {
                    System.out.println("Utworzono nowy plik: " + LOG_FILE_PATH);
                } else {
                    System.out.println("Nie udało się utworzyć pliku.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
