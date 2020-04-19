package com.sx.tank.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress("localhost", 8888));

        boolean started = true;
        while (started) {
            Socket socket = serverSocket.accept();
            new Thread(() -> {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    System.out.println(reader.readLine());

                    reader.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        serverSocket.close();
    }
}
