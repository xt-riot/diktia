package com.riot;

import java.io.Serializable;

public class Email implements Serializable {
    private boolean isNew;
    private String sender;
    private String receiver;
    private String subject;
    private String mainBody;

    public Email(String newSender, String newReceiver, String newSubject, String newMainBody) {
        this.sender = newSender;
        this.receiver = newReceiver;
        this.subject = newSubject;
        this.mainBody = newMainBody;
        this.isNew = true;
    }

    public boolean getIsNew() {
        return this.isNew;
    }
    public String getSender() {
        return this.sender;
    }
    public String getReceiver() {
        return this.receiver;
    }
    public String getSubject() {
        return this.subject;
    }
    public String getMainBody() {
        return this.mainBody;
    }

}
