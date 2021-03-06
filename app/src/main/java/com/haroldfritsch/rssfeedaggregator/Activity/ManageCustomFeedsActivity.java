package com.haroldfritsch.rssfeedaggregator.Activity;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;
import com.google.gson.reflect.TypeToken;
import com.haroldfritsch.rssfeedaggregator.Adapter.CategoryAdapter;
import com.haroldfritsch.rssfeedaggregator.Adapter.SourceAdapter;
import com.haroldfritsch.rssfeedaggregator.Model.Category;
import com.haroldfritsch.rssfeedaggregator.Model.Source;
import com.haroldfritsch.rssfeedaggregator.Model.Token;
import com.haroldfritsch.rssfeedaggregator.R;
import com.haroldfritsch.rssfeedaggregator.Services.ApiHelper;
import com.haroldfritsch.rssfeedaggregator.Services.TokenHelper;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.List;

import io.realm.Realm;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ManageCustomFeedsActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemLongClickListener {

    private SourceAdapter adapter;
    private ListView lvFeeds;
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
        lvFeeds = (ListView)findViewById(R.id.lvFeeds);
        lvFeeds.setOnItemLongClickListener(this);
        adapter = new SourceAdapter(this, R.layout.row_sources);
        lvFeeds.setAdapter(adapter);
        Ion.with(this)
                .load(ApiHelper.BASE_URL + ApiHelper.USER_FEED_ENDPOINT)
                .setHeader("RSS-TOKEN", TokenHelper.getInstance().getToken(this).getAccessToken())
                .as(new TypeToken<List<Source>>(){})
                .setCallback(new FutureCallback<List<Source>>() {
                    @Override
                    public void onCompleted(Exception e, List<Source> result) {
                        if (e != null) {
                            e.printStackTrace();
                        } else {
                            adapter.addAll(result);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
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

    /**
     *  Here we are displaying two different dialogs in a sequential manner.
     *  The first dialog is used to ask the RSS feed URL.
     *  The second dialog is used to select the feed's category.
     **/
    private void showAddFeedDialog() {
        displayFirstDialog();
    }

    private void displayFirstDialog() {
        View view = getLayoutInflater().inflate(R.layout.alert_dialog_add_feed, null);
        final EditText etFlux = (EditText)view.findViewById(R.id.etFlux);
        AlertDialog alert = new AlertDialog.Builder(this)
                .setTitle(R.string.addFeedTitleDialog)
                .setView(view)
                .setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (etFlux.getText().toString().equals("")) {
                            displayFirstDialog();
                        } else {
                            displaySecondDialog(etFlux.getText().toString());
                        }
                    }
                })
                .setNegativeButton("Annuler", null)
                .create();
        alert.show();
    }

    private void displaySecondDialog(final String feedUrl) {
        View view = getLayoutInflater().inflate(R.layout.alert_dialog_add_feed_part2, null);
        final ListView lvCategories = (ListView)view.findViewById(R.id.lvCategories);
        final CategoryAdapter adapter = new CategoryAdapter(this, R.layout.row_categories);
        adapter.addAll(Realm.getDefaultInstance().where(Category.class).findAll());
        final AlertDialog alert = new AlertDialog.Builder(this)
                .setTitle(R.string.chooseACategory)
                .setView(view)
                .create();
        lvCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                createFeed(feedUrl, adapter.getItem(position));
                alert.dismiss();
            }
        });
        lvCategories.setAdapter(adapter);
           adapter.notifyDataSetChanged();
        alert.show();
    }

    private void createFeed(String feedUrl, Category category) {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Ajout du feed")
                .setMessage("Merci de patienter.")
                .setCancelable(false)
                .create();
        dialog.show();
        Ion.with(this)
                .load(ApiHelper.BASE_URL + ApiHelper.USER_FEED_ENDPOINT + "/" + category.getId())
                .setHeader("RSS-TOKEN", TokenHelper.getInstance().getToken(this).getAccessToken())
                .setBodyParameter("url", feedUrl)
                .as(new TypeToken<Source>(){})
                .setCallback(new FutureCallback<Source>() {
                    @Override
                    public void onCompleted(Exception e, final Source result) {
                        dialog.dismiss();
                        if (e != null) {
                            e.printStackTrace();
                        } else {
                            Realm realm = Realm.getDefaultInstance();
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.copyToRealm(result);
                                    Toast.makeText(ManageCustomFeedsActivity.this, "Feed RSS ajouté !", Toast.LENGTH_LONG).show();
                                    adapter.add(result);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }

                    }
                });
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        askDeleteConfirmation(adapter.getItem(position));
        return false;
    }

    private void askDeleteConfirmation(final Source source) {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Attention")
                .setMessage(R.string.deleteFeed)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Ion.with(ManageCustomFeedsActivity.this)
                                .load("DELETE", ApiHelper.BASE_URL + ApiHelper.USER_FEED_ENDPOINT + "/" + source.getId())
                                .setHeader("RSS-TOKEN", TokenHelper.getInstance().getToken(ManageCustomFeedsActivity.this).getAccessToken())
                                .asString()
                                .setCallback(new FutureCallback<String>() {
                                    @Override
                                    public void onCompleted(Exception e, String result) {
                                        if (e != null) {
                                            e.printStackTrace();
                                        } else {
                                            adapter.remove(source);
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                });
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
        dialog.show();
    }
}
