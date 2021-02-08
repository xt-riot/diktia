package com.riot.client;

import com.riot.Account;
import com.riot.Email;
import com.riot.Messages;
import com.riot.Responses;

import java.io.*;
import java.net.Inet4Address;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class MailClient {
    private Account currentUser;
    private final Socket server;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private Messages serverMessage;

    private boolean state;


    public MailClient(String address, int port) throws IOException, ClassNotFoundException {
        this.server = new Socket(address, port);
        this.inputStream = new ObjectInputStream(this.server.getInputStream());
        this.outputStream = new ObjectOutputStream(this.server.getOutputStream());
        this.serverMessage = null;
        this.state = false;
        clientMain();
    }

    private void clientMain() throws ClassNotFoundException, IOException {
        while(true) {
            if(this.serverMessage.getResponse() != ((Messages)this.inputStream.readObject()).getResponse()) {
                this.serverMessage = (Messages)this.inputStream.readObject();
                serverResponse();
                continue;
            }

            if(this.state) {
                Scanner input = new Scanner(System.in);
                if(this.currentUser != null) {
                    switch(input.nextLine()) {
                        case "NewEmail": clientDraftEmail(); break;
                        case "ShowEmails": {
                            this.outputStream.writeObject(new Messages(Responses.Client_Request_MailBox));
                            this.outputStream.flush();
                            break;
                        }
                        case "ReadEmail": clientReadEmail(); break;
                        case "DeleteEmail": clientDeleteEmail(); break;
                        case "LogOut": {
                            this.outputStream.writeObject(new Messages(Responses.Client_Request_LogOut));
                            this.outputStream.flush();
                            break;
                        }
                        case "Exit": clientExit(); break;
                    }
                } else {
                    switch(input.nextLine()) {
                        case "LogIn": clientLogin(); break;
                        case "SignIn": clientRegister(); break;
                        case "Exit": clientExit(); break;
                    }
                }
            }
        }
    }

    private void serverResponse() {
        switch(this.serverMessage.getResponse()) {
            case Server_Response_NoUser: break;
            case Server_Response_EmailSent: System.out.println("The email has been sent successfully!"); break;

            case Server_Response_UserNameExists: System.out.println("The username/email you chose already exists."); break;
            case Server_Response_InvalidUsernameOrPassword: System.out.println("The username or the password is incorrect."); break;

            case Server_Response_InvalidEmailID: System.out.println("The email ID is invalid."); break;
            case Server_Response_InvalidReceiver: System.out.println("The receiver is not valid."); break;
            case Server_Response_UserLoggedIn: System.out.println("You are now logged in!"); this.currentUser = this.serverMessage.getAccount(); break;
            case Server_Response_UserLoggedOut: System.out.println("You are now logged out!"); this.currentUser = null; break;
            case Server_Response_MailBox: showMails(this.serverMessage.getMailbox()); break;
            case Server_Response_Ready: showChoices(); break;
        }
    }

    private void clientRegister() throws IOException {
        System.out.println("Enter a username or an email.");
        Scanner userInput = new Scanner(System.in);
        String uName = userInput.nextLine();
        while(uName.length() <= 0) {
            System.out.println("The given name or email is invalid. Please try again.");
            uName = userInput.nextLine();
        }
        System.out.println("Enter a password: ");
        String pwd = userInput.nextLine();
        while(pwd.length() <= 0) {
            System.out.println("The password you typed is invalid. Please try again.");
            pwd = userInput.nextLine();
        }

        Account newUser = new Account(uName, pwd);
        this.outputStream.writeObject(new Messages(Responses.Client_Request_Register, newUser));
        this.outputStream.flush();
    }

    private void clientLogin() throws IOException {
        System.out.println("Type your username");
        Scanner userInput = new Scanner(System.in);
        String uName = userInput.nextLine();
        System.out.println("Type your password");
        String pwd = userInput.nextLine();
        Account user = new Account(uName, pwd);
        this.outputStream.writeObject(new Messages(Responses.Client_Request_LogIn, user));
        this.outputStream.flush();
    }

    private void clientDraftEmail() throws IOException {
        Scanner input = new Scanner(System.in);
        System.out.println("Type the receiver");
        String receiver = input.nextLine();
        System.out.println("Type the subject");
        String subject = input.nextLine();
        System.out.println("Type the email");
        String mainBody = input.nextLine();
        Email email = new Email(this.currentUser.getUsername(), receiver, subject, mainBody);
        this.outputStream.writeObject(new Messages(Responses.Client_Request_NewEmail, email));
        this.outputStream.flush();
    }

    private void clientReadEmail() throws IOException {
        Scanner input = new Scanner(System.in);
        System.out.println("Type the email ID you want to read");
        int id = input.nextInt();
        this.outputStream.writeObject(new Messages(Responses.Client_Request_ReadEmail, id));
        this.outputStream.flush();
    }

    private void clientDeleteEmail() throws IOException {
        Scanner input = new Scanner(System.in);
        System.out.println("Type the email ID you want to delete");
        int id = input.nextInt();
        this.outputStream.writeObject(new Messages(Responses.Client_Request_DeleteEmail, id));
        this.outputStream.flush();
    }

    private void clientExit() throws IOException {
        this.outputStream.writeObject(new Messages(Responses.Client_Request_Exit));
        this.inputStream.close();
        this.outputStream.close();
        this.server.close();
        System.exit(0);
    }

    private void showMails(List<Email> email) {
        String temp = "";
        System.out.println("ID\tFrom\tSubject");
        for(int i = 0; i<email.size(); i++) {
            if( email.get(i).getIsNew() ) {
                temp = "[New]";
            }
            else temp = "\t";
            System.out.println(i + " " + temp + "\t" + email.get(i).getSender() + "\t" + email.get(i).getSubject());
        }
    }


    private void showChoices() {
        if (this.currentUser != null) {
            System.out.println("----------\n" +
                    "MailServer: \n" +
                    "---------- \n" +
                    "Welcome back " + currentUser.getUsername() + "!\n" +
                    "> NewEmail\n" +
                    "> ShowEmails\n" +
                    "> ReadEmail\n" +
                    "> DeleteEmail\n" +
                    "> LogOut\n" +
                    "> Exit\n");
        } else {
            System.out.println("----------\n" +
                    "MailServer: " +
                    "----------\n" +
                    "Hello, you are connected as guest.\n" +
                    "==========" +
                    "> LogIn\n" +
                    "> SignIn\n" +
                    "> Exit\n" +
                    "==========");
        }
        this.state = true;
    }

}
