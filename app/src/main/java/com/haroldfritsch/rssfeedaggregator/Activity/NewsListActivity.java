package com.haroldfritsch.rssfeedaggregator.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.haroldfritsch.rssfeedaggregator.Adapter.NewsAdapter;
import com.haroldfritsch.rssfeedaggregator.Adapter.SourceAdapter;
import com.haroldfritsch.rssfeedaggregator.Model.News;
import com.haroldfritsch.rssfeedaggregator.Model.NewsResponse;
import com.haroldfritsch.rssfeedaggregator.Model.Source;
import com.haroldfritsch.rssfeedaggregator.R;
import com.haroldfritsch.rssfeedaggregator.Services.ApiHelper;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class NewsListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView lvNews;
    private List<News> newses;
    private NewsAdapter adapter;
    private LinearLayout llfilters;
    private String selectedSourceId = null;

    private boolean filtersShown = false;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);
        llfilters = (LinearLayout)findViewById(R.id.llFilters);
        lvNews = (ListView)findViewById(R.id.lvNews);
        adapter = new NewsAdapter(this, R.layout.row_news);
        lvNews.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lvNews.setOnItemClickListener(this);
        Log.e("endpoint", ApiHelper.BASE_URL + ApiHelper.NEWS_ENDPOINT);
        Ion.with(this).load(ApiHelper.BASE_URL + ApiHelper.NEWS_ENDPOINT)
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.news_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.account:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.filter:
                llfilters.setVisibility(filtersShown?View.GONE:View.VISIBLE);
                filtersShown = !filtersShown;
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        News news = adapter.getItem(position);
        if (news != null) {
            Intent intent = new Intent(this, NewsDisplayActivity.class);
            intent.putExtra("newsUrl", news.getLink());
            intent.putExtra("newsName", news.getTitle());
            startActivity(intent);
        }
    }
}
