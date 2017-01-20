package com.haroldfritsch.rssfeedaggregator.Model;

import com.google.gson.Gson;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by fritsc_h on 19/01/2017.
 */

public class Token extends RealmObject {
    private String accessToken;
    private String refreshToken;
    private Long timestamp;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    private String error;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String flattenToken() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static Token inflateToken(String flattenedToken) {
        Gson gson = new Gson();
        return gson.fromJson(flattenedToken, Token.class);
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
