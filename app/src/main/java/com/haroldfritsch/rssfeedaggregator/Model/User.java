package com.haroldfritsch.rssfeedaggregator.Model;

/**
 * Created by fritsc_h on 19/01/2017.
 */

public class User {
    private String id;
    private String email;
    private Token token;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}
