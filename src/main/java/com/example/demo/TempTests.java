package com.example.demo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;

public class TempTests {
    private static final String LOG_FILE_PATH = "logs/logs.txt";
    public static void main(String[] args) {
        String current_date = String.valueOf(LocalDate.now());
        System.out.println("Current date: " + current_date);
        System.out.println("Current date: " + current_date.replaceAll("-", ""));
        System.out.println("try: " + "logs_" + String.valueOf(LocalDate.now()).replaceAll("-","")+".log");
    }
}
