package com.haroldfritsch.rssfeedaggregator.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haroldfritsch.rssfeedaggregator.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean isLoginShowed = false;
    private boolean isRegisterShowed = false;

    private LinearLayout llRoot;

    private LinearLayout llLogin;
    private LinearLayout llLoginFields;
    private TextView tvLoginDescription;

    private LinearLayout llRegister;
    private LinearLayout llRegisterFields;
    private TextView tvRegisterDescription;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        llRoot = (LinearLayout)findViewById(R.id.activity_login);
        llLogin = (LinearLayout)findViewById(R.id.llLogin);
        llLogin.setOnClickListener(this);
        llLoginFields = (LinearLayout)findViewById(R.id.llLoginFields);
        tvLoginDescription = (TextView)findViewById(R.id.tvLoginDescription);
        llRegister = (LinearLayout)findViewById(R.id.llRegister);
        llRegister.setOnClickListener(this);
        llRegisterFields = (LinearLayout)findViewById(R.id.llRegisterfields);
        tvRegisterDescription = (TextView)findViewById(R.id.tvRegisterDescription);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llLogin:
                deployLogin();
                break;
            case R.id.llRegister:
                deployRegister();
                break;
        }
    }

    private void deployLogin() {
        isLoginShowed = true;
        isRegisterShowed = false;
        llRegisterFields.setVisibility(View.GONE);
        llLoginFields.setVisibility(View.VISIBLE);
    }

    private void deployRegister() {
        isLoginShowed = false;
        isRegisterShowed = true;
        llRegisterFields.setVisibility(View.VISIBLE);
        llLoginFields.setVisibility(View.GONE);
    }

}
