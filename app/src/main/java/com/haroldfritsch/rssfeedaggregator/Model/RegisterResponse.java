package com.haroldfritsch.rssfeedaggregator.Model;

/**
 * Created by fritsc_h on 19/01/2017.
 */

public class RegisterResponse {
    private User user;
    private Token token;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    private String error;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}
