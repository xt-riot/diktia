package com.riot.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MailServer {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(5056);
        while(true) {
            Socket newClient = null;
            try {
                newClient = server.accept();
                ObjectInputStream inputStream = new ObjectInputStream(newClient.getInputStream());
                ObjectOutputStream outputStream = new ObjectOutputStream(newClient.getOutputStream());

                Thread clientHandler = new ClientHandler(newClient, inputStream, outputStream);
                clientHandler.start();
            }
            catch(Exception e) {
                newClient.close();
                e.printStackTrace();
            }
        }
    }
}
