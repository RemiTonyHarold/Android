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
import com.haroldfritsch.rssfeedaggregator.Model.Category;
import com.haroldfritsch.rssfeedaggregator.R;
import com.haroldfritsch.rssfeedaggregator.Services.ApiHelper;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.List;

public class CategorySelectionActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView lvCategories;
    private List<Category> categories;
    private CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvCategories = (ListView)findViewById(R.id.lvCategories);
        adapter = new CategoryAdapter(this, R.layout.row_categories);
        lvCategories.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lvCategories.setOnItemClickListener(this);
        Ion.with(this).load(ApiHelper.BASE_URL + ApiHelper.CATEGORIES_ENDPOINT)
                .as(new TypeToken<List<Category>>(){})
                .setCallback(new FutureCallback<List<Category>>() {
                    @Override
                    public void onCompleted(Exception e, List<Category> result) {
                        if (e != null) {
                            e.printStackTrace();
                            return;
                        }
                        categories = result;
                        adapter.addAll(categories);
                        Toast.makeText(CategorySelectionActivity.this, "Got " + categories.size() + " categories", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, adapter.getItem(position).getName(), Toast.LENGTH_SHORT).show();
        Intent intent  = new Intent(this, SourceSelectionActivity.class);
        intent.putExtra("categoryId", adapter.getItem(position).getId());
        startActivity(intent);
    }
}
