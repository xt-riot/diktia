package com.riot;

import java.io.Serializable;
import java.util.List;

public class Messages implements Serializable {
    private Responses response;
    private Account account;
    private Email email;
    private List<Email> mailbox;
    private int id;


    public Messages(Responses res) {
        this.response = res;
    }
    public Messages(Responses res, Account acc) {
        this.response = res;
        this.account = acc;
    }
    public Messages(Responses res, Email e) {
        this.response = res;
        this.email = e;
    }
    public Messages(Responses res, List<Email> list) {
        this.response = res;
        this.mailbox = list;
    }
    public Messages(Responses res, int id) {
        this.response = res;
        this.id = id;
    }

    public Responses getResponse() {
        return this.response;
    }
    public Account getAccount() {
        return this.account;
    }
    public Email getEmail() {
        return this.email;
    }
    public int getId() {
        return this.id;
    }
    public List<Email> getMailbox() { return this.mailbox; }
}
