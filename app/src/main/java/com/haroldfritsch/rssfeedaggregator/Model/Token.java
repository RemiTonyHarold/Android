package com.haroldfritsch.rssfeedaggregator.Model;

import java.util.Date;

/**
 * Created by fritsc_h on 19/01/2017.
 */

public class Token {
    private String accessToken;
    private String refreshToken;
  //  private Date expireDate;

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

  /*  public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }*/
}
