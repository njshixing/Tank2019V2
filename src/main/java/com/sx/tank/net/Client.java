package com.sx.tank.net;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client {

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 8888);

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        writer.write("hello ");
        writer.newLine();
        writer.flush();
        writer.close();
        System.in.read();
        socket.close();
    }
}
