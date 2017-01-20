package com.haroldfritsch.rssfeedaggregator.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.haroldfritsch.rssfeedaggregator.Adapter.NewsAdapter;
import com.haroldfritsch.rssfeedaggregator.Model.Category;
import com.haroldfritsch.rssfeedaggregator.Model.News;
import com.haroldfritsch.rssfeedaggregator.R;
import com.haroldfritsch.rssfeedaggregator.Services.ApiHelper;
import com.haroldfritsch.rssfeedaggregator.Services.TokenHelper;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class NewsListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView lvNews;
    private List<News> newses;
    private NewsAdapter adapter;
    private Category selectedCategory = null;

    private Realm realm = Realm.getDefaultInstance();


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        lvNews = (ListView)findViewById(R.id.lvNews);
        adapter = new NewsAdapter(this, R.layout.row_news);
        lvNews.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lvNews.setOnItemClickListener(this);
        newses = realm.where(News.class).findAll().sort("pubDate", Sort.DESCENDING);
        adapter.addAll(newses);
        updateNews();
        downloadCategories();
    }

    private void updateNews() {
        final Long lastFetchTimestamp = getSharedPreferences("rssfeed", MODE_PRIVATE).getLong("lastFetchTimestamp", 0);
        if (TokenHelper.getInstance().isUserLoggedIn(this)) {
            Toast.makeText(this, "Utilisateur connecté", Toast.LENGTH_SHORT).show();
            Ion.with(this).load(
                    ApiHelper.BASE_URL + ApiHelper.NEWS_ENDPOINT
                            + "?timestamp=" + Long.toString(lastFetchTimestamp))
                    .addHeader("RSS-TOKEN", TokenHelper.getInstance().getToken(this).getAccessToken())
                    .as(new TypeToken<List<News>>(){})
                    .setCallback(new FutureCallback<List<News>>() {
                        @Override
                        public void onCompleted(Exception e, final List<News> result) {
                            if (e != null) {
                                e.printStackTrace();
                                return;
                            }
                            updateLastFetchTimestamp();
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.copyToRealmOrUpdate(result);
                                    newses = realm.where(News.class).findAll().sort("pubDate", Sort.DESCENDING);
                                    adapter.clear();
                                    adapter.addAll(newses);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    });
        } else {
            Toast.makeText(this, "Utilisateur non connecté", Toast.LENGTH_SHORT).show();
            Ion.with(this).load(ApiHelper.BASE_URL + ApiHelper.NEWS_ENDPOINT
                    + "?timestamp=" + Long.toString(lastFetchTimestamp))
                    .as(new TypeToken<List<News>>(){})
                    .setCallback(new FutureCallback<List<News>>() {
                        @Override
                        public void onCompleted(Exception e, final List<News> result) {
                            if (e != null) {
                                e.printStackTrace();
                                return;
                            }
                            updateLastFetchTimestamp();
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.copyToRealmOrUpdate(result);
                                    newses = realm.where(News.class).findAll().sort("pubDate", Sort.DESCENDING);
                                    adapter.clear();
                                    adapter.addAll(newses);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    });
        }
    }

    private void updateLastFetchTimestamp() {
        long timestamp = new Date().getTime();
        SharedPreferences.Editor editor = getSharedPreferences("rssfeed", MODE_PRIVATE).edit();
        editor.putLong("lastFetchTimestamp", timestamp);
        editor.apply();
    }

    private void downloadCategories() {
        Ion.with(this).load(ApiHelper.BASE_URL + ApiHelper.CATEGORIES_ENDPOINT)
                .as(new TypeToken<List<Category>>(){})
                .setCallback(new FutureCallback<List<Category>>() {
                    @Override
                    public void onCompleted(Exception e, final List<Category> result) {
                        if (e != null) {
                            e.printStackTrace();
                            return;
                        }
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.copyToRealmOrUpdate(result);
                            }
                        });
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
                showCategoryFilter();
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

    private void showCategoryFilter() {
        final RealmResults<Category> categories = realm.where(Category.class).findAllSorted("name");
        final ArrayList<String> categoriesString = new ArrayList<>();
        for (Category category: categories) {
            categoriesString.add(category.getName());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1
        );
        arrayAdapter.addAll(categoriesString);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e("filter", categories.get(which).getId());
                newses = realm.where(News.class).equalTo("categoryId", categories.get(which).getId())
                        .findAll().sort("pubDate", Sort.DESCENDING);
                Toast.makeText(NewsListActivity.this, "count" + newses.size(), Toast.LENGTH_SHORT).show();
                adapter.clear();
                adapter.addAll(newses);
                adapter.notifyDataSetChanged();
                Toast.makeText(NewsListActivity.this, "which:"+which, Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }
}
