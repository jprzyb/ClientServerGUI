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

            printWriter.println(LocalDateTime.now()  + " | " + msg);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void checkIfExist(){
        File file = new File(LOG_FILE_PATH);
        if (!file.exists()) {
            String directoryPath = file.getParent();
            File directory = new File(directoryPath);
            if (!directory.exists()) directory.mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
