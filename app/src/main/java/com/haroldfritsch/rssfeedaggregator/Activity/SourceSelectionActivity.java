package com.haroldfritsch.rssfeedaggregator.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.haroldfritsch.rssfeedaggregator.Adapter.CategoryAdapter;
import com.haroldfritsch.rssfeedaggregator.Adapter.SourceAdapter;
import com.haroldfritsch.rssfeedaggregator.Model.Category;
import com.haroldfritsch.rssfeedaggregator.Model.Source;
import com.haroldfritsch.rssfeedaggregator.R;
import com.haroldfritsch.rssfeedaggregator.Services.ApiHelper;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.socketio.SocketIOException;
import com.koushikdutta.ion.Ion;

import java.util.List;

public class SourceSelectionActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView lvSources;
    private List<Source> sources;
    private SourceAdapter adapter;
    private String selectedCategoryId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_selection);
        lvSources = (ListView)findViewById(R.id.lvSources);
        adapter = new SourceAdapter(this, R.layout.row_categories);
        lvSources.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lvSources.setOnItemClickListener(this);
        if (getIntent().hasExtra("categoryId")) {
            selectedCategoryId = getIntent().getStringExtra("categoryId");
        } else {
            finish();
        }
        Ion.with(this).load(ApiHelper.BASE_URL + ApiHelper.SOURCE_ENDPOINT + "/" + selectedCategoryId)
                .as(new TypeToken<List<Source>>(){})
                .setCallback(new FutureCallback<List<Source>>() {
                    @Override
                    public void onCompleted(Exception e, List<Source> result) {
                        if (e != null) {
                            e.printStackTrace();
                            return;
                        }
                        sources = result;
                        adapter.addAll(sources);
                    }
                });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, adapter.getItem(position).getName(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, NewsListActivity.class);
        intent.putExtra("sourceId", adapter.getItem(position).getId());
        startActivity(intent);
    }
}
