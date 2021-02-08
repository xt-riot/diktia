package com.riot;

import java.io.Serializable;
import java.util.List;

public class Account implements Serializable {
    private String username;
    private String password;
    private List<Email> mailbox;

    public Account(String uName, String pwd) {
        this.username = uName;
        this.password = pwd;
    }

    public void setUsername(String uName) {
        this.username = uName;
    }
    public void setPassword(String pwd) {
        this.password = pwd;
    }
    public void addNewEmail(Email newEmail) {
        this.mailbox.add(newEmail);
    }

    public String getUsername() {
        return this.username;
    }
    public String getPassword() {
        return this.password;
    }

    public List<Email> getMailbox() {
        return this.mailbox;
    }
}
