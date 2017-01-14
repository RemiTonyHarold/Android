package com.haroldfritsch.rssfeedaggregator.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.haroldfritsch.rssfeedaggregator.Adapter.NewsAdapter;
import com.haroldfritsch.rssfeedaggregator.Adapter.SourceAdapter;
import com.haroldfritsch.rssfeedaggregator.Model.News;
import com.haroldfritsch.rssfeedaggregator.Model.Source;
import com.haroldfritsch.rssfeedaggregator.R;
import com.haroldfritsch.rssfeedaggregator.Services.ApiHelper;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.List;

public class NewsListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView lvNews;
    private List<News> newses;
    private NewsAdapter adapter;
    private String selectedSourceId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_selection);
        lvNews = (ListView)findViewById(R.id.lvSources);
        adapter = new NewsAdapter(this, R.layout.row_news);
        lvNews.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lvNews.setOnItemClickListener(this);
        if (getIntent().hasExtra("sourceId")) {
            selectedSourceId = getIntent().getStringExtra("sourceId");
        } else {
            finish();
        }
        Log.e("endpoint", ApiHelper.BASE_URL + ApiHelper.NEWS_ENDPOINT + "/" + selectedSourceId);
        Ion.with(this).load(ApiHelper.BASE_URL + ApiHelper.NEWS_ENDPOINT + "/" + selectedSourceId)
                .as(new TypeToken<List<News>>(){})
                .setCallback(new FutureCallback<List<News>>() {
                    @Override
                    public void onCompleted(Exception e, List<News> result) {
                        if (e != null) {
                            e.printStackTrace();
                            return;
                        }
                        newses = result;
                        adapter.addAll(newses);
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, NewsDisplayActivity.class);
        intent.putExtra("newsUrl", adapter.getItem(position).getLink());
        startActivity(intent);
    }
}
