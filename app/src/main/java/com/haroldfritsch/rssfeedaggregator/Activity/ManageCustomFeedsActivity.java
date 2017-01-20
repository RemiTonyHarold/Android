package com.haroldfritsch.rssfeedaggregator.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.haroldfritsch.rssfeedaggregator.Adapter.CategoryAdapter;
import com.haroldfritsch.rssfeedaggregator.Model.Category;
import com.haroldfritsch.rssfeedaggregator.R;
import com.haroldfritsch.rssfeedaggregator.Services.TokenHelper;

import io.realm.Realm;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ManageCustomFeedsActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton fab;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_custom_feeds);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.feed_manage_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                TokenHelper.getInstance().logout(this);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                showAddFeedDialog();
                break;
        }
    }

    private void showAddFeedDialog() {
        View view = getLayoutInflater().inflate(R.layout.alert_dialog_add_feed, null);
        ListView lvCategories = (ListView)view.findViewById(R.id.lvCategories);
        EditText etFlux = (EditText)view.findViewById(R.id.etFlux);
        CategoryAdapter adapter = new CategoryAdapter(this, R.layout.row_categories);
        adapter.addAll(Realm.getDefaultInstance().where(Category.class).findAll());
        lvCategories.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        AlertDialog alert = new AlertDialog.Builder(this)
                .setTitle(R.string.addFeedTitleDialog)
                .setView(view)
                .setPositiveButton("Ajouter", null)
                .setNegativeButton("Annuler", null)
                .create();
        alert.show();
    }
}
