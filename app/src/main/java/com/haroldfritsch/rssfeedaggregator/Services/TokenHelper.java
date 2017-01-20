package com.haroldfritsch.rssfeedaggregator.Services;

/**
 * Created by fritsc_h on 20/01/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;

import com.haroldfritsch.rssfeedaggregator.Model.Token;

import java.util.Date;



public class TokenHelper {
    private static TokenHelper ourInstance = new TokenHelper();
    private Token token = null;

    public static TokenHelper getInstance() {
        return ourInstance;
    }

    private void loadTokenFromPreferences(Context context) {
        String flattenedToken = context
                .getSharedPreferences("rssfeed", Context.MODE_PRIVATE)
                .getString("token", "");
        if (!flattenedToken.equals("")) {
            this.token = Token.inflateToken(flattenedToken);
        }
    }

    public void logout(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("rssfeed", Context.MODE_PRIVATE).edit();
        editor.remove("token");
        editor.apply();
        this.token = null;
    }

    public Token getToken(Context context) {
        if (this.token == null) {
            loadTokenFromPreferences(context);
        }
        return this.token;
    }

    public void saveToken(Context context, Token token) {
        token.setTimestamp(new Date().getTime());
        this.token = token;
        SharedPreferences.Editor editor = context.getSharedPreferences("rssfeed", Context.MODE_PRIVATE).edit();
        editor.putString("token", token.flattenToken());
        editor.apply();
    }

    public boolean isUserLoggedIn(Context context) {
        return this.getToken(context) != null;
    }

    private TokenHelper() {
    }
}
