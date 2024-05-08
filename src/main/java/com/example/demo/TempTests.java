package com.example.demo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class TempTests {
    public static void main(String[] args) throws UnknownHostException {
        InetAddress host = InetAddress.getByName("SCLAP45");
        System.out.println(host);
        if (!host.getHostAddress().isEmpty()){
            System.out.println("Istnieje");
        }
    }
}
