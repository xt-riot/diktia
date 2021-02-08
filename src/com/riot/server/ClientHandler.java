package com.riot.server;

import com.riot.Account;
import com.riot.Email;
import com.riot.Messages;
import com.riot.Responses;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

public class ClientHandler extends Thread {
    private final Socket client;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private Messages clientMessage;


    private Account currentUser;
    private HashMap<String, Account> allUsers;

    public ClientHandler(Socket newClient, ObjectInputStream in, ObjectOutputStream out) throws IOException, ClassNotFoundException {
        this.client = newClient;
        this.inputStream = in;
        this.outputStream = out;
        allUsers = new HashMap<>();
        Account defaultUser1 = new Account("melidis@csd.auth.gr", "0000");
        Account defaultUser2 = new Account("pmelidi@csd.gr", "0000");
        this.allUsers.put(defaultUser1.getUsername(), defaultUser1);
        this.allUsers.put(defaultUser2.getUsername(), defaultUser2);
        handlerMain();
    }

    private void handlerMain() throws IOException, ClassNotFoundException {
        while(true) {
            if(this.clientMessage.getResponse() != ((Messages)this.inputStream.readObject()).getResponse()) {
                this.clientMessage = (Messages)this.inputStream.readObject();
                switch(this.clientMessage.getResponse()) {
                    case Client_Request_DeleteEmail: deleteEmail(this.clientMessage.getId()); break;
                    case Client_Request_Exit: exit(); break;
                    case Client_Request_LogOut: logOut(); break;
                    case Client_Request_MailBox: showEmails(); break;
                    case Client_Request_NewEmail: newEmail(this.clientMessage.getEmail()); break;
                    case Client_Request_ReadEmail: readEmail(this.clientMessage.getId()); break;
                    case Client_Request_LogIn: logIn(this.clientMessage.getAccount()); break;
                    case Client_Request_Register: register(this.clientMessage.getAccount()); break;
                }
            }
        }
    }

    public void register(Account acc) throws IOException {
        String username = acc.getUsername();
        if(this.allUsers.containsKey(username))
            this.outputStream.writeObject(new Messages(Responses.Server_Response_UserNameExists));
        else this.allUsers.put(username, acc);
    }
    public void logIn(Account acc) throws IOException {
        String username = acc.getUsername();
        String pwd = acc.getPassword();
        if(this.allUsers.containsKey(username)) {
            if(this.allUsers.get(username).getPassword().equals(pwd)) {
                this.currentUser = acc;
                this.outputStream.writeObject(new Messages(Responses.Server_Response_UserLoggedIn, this.currentUser));
            }
        } else this.outputStream.writeObject(new Messages(Responses.Server_Response_InvalidUsernameOrPassword));
    }
    public void newEmail(Email e) throws IOException {
        String receiver = e.getReceiver();
        if(this.allUsers.containsKey(receiver)) {
            this.allUsers.get(receiver).addNewEmail(e);
            this.outputStream.writeObject(new Messages(Responses.Server_Response_EmailSent));
        }
        else this.outputStream.writeObject(new Messages(Responses.Server_Response_InvalidReceiver));
    }
    public void showEmails() throws IOException {
        this.outputStream.writeObject(new Messages(Responses.Server_Response_MailBox, this.currentUser.getMailbox()));
    }
    public void readEmail(int id) throws IOException {
        if(this.currentUser.getMailbox().size() >= id)
            this.outputStream.writeObject(new Messages(Responses.Server_Response_ReadEmail, this.currentUser.getMailbox().get(id-1)));
        else this.outputStream.writeObject(new Messages(Responses.Server_Response_InvalidEmailID));
    }
    public void deleteEmail(int id) throws IOException {
        if(this.currentUser.getMailbox().size() >= id) {
            this.currentUser.getMailbox().remove(id);
            this.outputStream.writeObject(new Messages(Responses.Server_Response_EmailDeleted));

        }
        else this.outputStream.writeObject(new Messages(Responses.Server_Response_InvalidEmailID));
    }
    public void logOut() throws IOException {
        this.currentUser = null;
        this.outputStream.writeObject(new Messages(Responses.Server_Response_UserLoggedOut));
    }
    public void exit() throws IOException {
        this.client.close();
        this.outputStream.close();
        this.inputStream.close();
    }



}
