package com.haroldfritsch.rssfeedaggregator.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.haroldfritsch.rssfeedaggregator.R;

public class NewsDisplayActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_display);
        webView = (WebView)findViewById(R.id.webview);
        if (!getIntent().hasExtra("newsUrl")) {
            finish();
        }
        webView.loadUrl(getIntent().getStringExtra("newsUrl"));
    }
}
