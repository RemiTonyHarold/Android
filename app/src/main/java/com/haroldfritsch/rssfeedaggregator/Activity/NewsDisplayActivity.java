package com.haroldfritsch.rssfeedaggregator.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;

import com.haroldfritsch.rssfeedaggregator.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class NewsDisplayActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_display);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getIntent().getStringExtra("newsName"));
        }
        webView = (WebView)findViewById(R.id.webview);
        if (!getIntent().hasExtra("newsUrl")) {
            finish();
        }
        webView.loadUrl(getIntent().getStringExtra("newsUrl"));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
