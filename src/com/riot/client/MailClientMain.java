package com.riot.client;

import java.io.IOException;

public class MailClientMain {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String address = args[0];
        int port = Integer.parseInt(args[1]);
        MailClient op = new MailClient(address, port);
    }

}
