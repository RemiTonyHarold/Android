package com.haroldfritsch.rssfeedaggregator.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.haroldfritsch.rssfeedaggregator.Model.RegisterResponse;
import com.haroldfritsch.rssfeedaggregator.Model.Token;
import com.haroldfritsch.rssfeedaggregator.R;
import com.haroldfritsch.rssfeedaggregator.Services.ApiHelper;
import com.haroldfritsch.rssfeedaggregator.Services.TokenHelper;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogin;
    private EditText etLoginEmail;
    private EditText etLoginPassword;
    private LinearLayout llLogin;
    private LinearLayout llLoginFields;

    private Button btnRegister;
    private EditText etRegisterEmail;
    private EditText etRegisterPassword;
    private LinearLayout llRegister;
    private LinearLayout llRegisterFields;

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
        if (TokenHelper.getInstance().isUserLoggedIn(this)) {
            loadManageCustomFeedsActivity();
        }
        llLogin = (LinearLayout) findViewById(R.id.llLogin);
        llLogin.setOnClickListener(this);
        llLoginFields = (LinearLayout) findViewById(R.id.llLoginFields);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        etRegisterEmail = (EditText) findViewById(R.id.etEmail);
        etRegisterPassword = (EditText) findViewById(R.id.etPassword);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
        etLoginEmail = (EditText) findViewById(R.id.etLoginEmail);
        etLoginPassword = (EditText) findViewById(R.id.etLoginPassword);
        llRegister = (LinearLayout) findViewById(R.id.llRegister);
        llRegister.setOnClickListener(this);
        llRegisterFields = (LinearLayout) findViewById(R.id.llRegisterfields);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llLogin:
                deployLogin();
                break;
            case R.id.btnLogin:
                if (isLoginFormValid()) {
                    logUserIn();
                }
                break;
            case R.id.llRegister:
                deployRegister();
                break;
            case R.id.btnRegister:
                if (isRegisterFormValid()) {
                    registerUser();
                }
                break;
        }
    }

    private void loadManageCustomFeedsActivity() {
        Intent intent = new Intent(LoginActivity.this, ManageCustomFeedsActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean isLoginFormValid() {
        etLoginEmail.setError(null);
        etLoginPassword.setError(null);

        if (!isValidEmail(etLoginEmail.getText().toString())) {
            etLoginEmail.setError(getString(R.string.error_email_invalid));
            return false;
        }
        if (etLoginPassword.length() < 6) {
            etLoginPassword.setError(getString(R.string.error_password_length));
            return false;
        }
        return true;
    }

    private void logUserIn() {
        Ion.with(this).load(ApiHelper.BASE_URL + ApiHelper.AUTH_ENDPOINT)
                .setBodyParameter("email", etLoginEmail.getText().toString())
                .setBodyParameter("password", etLoginPassword.getText().toString())
                .as(new TypeToken<Token>(){})
                .setCallback(new FutureCallback<Token>() {
                    @Override
                    public void onCompleted(Exception e, Token result) {
                        if (e != null) {
                            e.printStackTrace();
                            return;
                        }
                        if (result.getError() != null) {
                            Toast.makeText(LoginActivity.this, result.getError(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        TokenHelper.getInstance().saveToken(LoginActivity.this, result);
                        loadManageCustomFeedsActivity();
                    }
                });
    }

    private void deployLogin() {
        llRegisterFields.setVisibility(View.GONE);
        llLoginFields.setVisibility(View.VISIBLE);
    }

    public static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;
        return Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void deployRegister() {
        llRegisterFields.setVisibility(View.VISIBLE);
        llLoginFields.setVisibility(View.GONE);
    }

    private boolean isRegisterFormValid() {
        etRegisterEmail.setError(null);
        etRegisterPassword.setError(null);

        if (!isValidEmail(etRegisterEmail.getText().toString())) {
            etRegisterEmail.setError(getString(R.string.error_email_invalid));
            return false;
        }
        if (etRegisterPassword.length() < 6) {
            etRegisterPassword.setError(getString(R.string.error_password_length));
            return false;
        }
        return true;
    }

    private void registerUser() {
        Ion.with(this).load(ApiHelper.BASE_URL + ApiHelper.REGISTER_ENDPOINT)
                .setBodyParameter("email", etRegisterEmail.getText().toString())
                .setBodyParameter("password", etRegisterPassword.getText().toString())
                .as(new TypeToken<RegisterResponse>(){})
                .setCallback(new FutureCallback<RegisterResponse>() {
                    @Override
                    public void onCompleted(Exception e, RegisterResponse result) {
                        if (e != null) {
                            e.printStackTrace();
                            return;
                        }
                        if (result.getError() != null) {
                            Toast.makeText(LoginActivity.this, result.getError(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        TokenHelper.getInstance().saveToken(LoginActivity.this, result.getToken());
                        loadManageCustomFeedsActivity();
                    }
                });
    }
}
