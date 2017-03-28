/*
 * MIT License
 *
 * Copyright (c) 2017 Maia Grotepass
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.afrikaburn.app.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.afrikaburn.app.R;
import com.afrikaburn.app.databinding.ActivityMainBinding;
import com.afrikaburn.app.model.DataHistory;
import com.afrikaburn.app.model.Entry;
import com.afrikaburn.app.model.EntryFields;
import com.afrikaburn.app.service.DataFetchService;
import com.afrikaburn.app.ui.adapters.EntryRecyclerAdapter;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class MainActivity extends BaseActivity implements com.afrikaburn.app.ui.adapters.OnEntryClickListener {

    DataHistory dataHistory;
    RecyclerView recyclerView;
    @Entry.What
    int what = Entry.ALL;
    private boolean showFavourites = false;

    private ActivityMainBinding binding;

    private RealmChangeListener<DataHistory> dataHistoryListener = new RealmChangeListener<DataHistory>() {
        @Override
        public void onChange(DataHistory element) {
            binding.setDataHistory(dataHistory);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        RealmResults<DataHistory> res = realmForUi.where(DataHistory.class).findAll();
        if (!res.isEmpty()) {
            dataHistory = res.get(0);
            RealmObject.addChangeListener(dataHistory, dataHistoryListener);
        }
        binding.setDataHistory(dataHistory);
        recyclerView = binding.list;
        setupRecyclerView();

        BottomNavigationView bottomNavigationView = binding.bottomNavigation;

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.navigation_browse:
                                showFavourites = false;
                                what = Entry.ALL;
                                recyclerView.setAdapter(new EntryRecyclerAdapter(MainActivity.this,
                                        MainActivity.this,
                                        realmForUi.where(Entry.class).findAll()));
                                getSupportActionBar().setTitle(getString(R.string.title_main));
                                break;

                            case R.id.navigation_now:
                                showFavourites = false;
                                what = Entry.PERFORMANCE; //TODO pass Burn to the map too or pass the date search
                                recyclerView.setAdapter(new EntryRecyclerAdapter(MainActivity.this,
                                        MainActivity.this,
                                        realmForUi.where(Entry.class).equalTo(EntryFields.WHAT, Entry.PERFORMANCE)
                                                .or()
                                                .equalTo(EntryFields.WHAT, Entry.BURN)
                                                .findAll()));
                                getSupportActionBar().setTitle(getString(R.string.title_performance));
                                //TODO
                                Toast.makeText(MainActivity.this, "Implement filter for events that happen today", Toast.LENGTH_SHORT).show();
                                break;

                            case R.id.navigation_favourites:
                                what = Entry.ALL;
                                showFavourites = true;
                                recyclerView.setAdapter(new EntryRecyclerAdapter(MainActivity.this,
                                        MainActivity.this,
                                        realmForUi.where(Entry.class).equalTo(EntryFields.FAVOURITE, true).findAll()));
                                getSupportActionBar().setTitle(getString(R.string.title_favorite));
                                break;

                        }
                        return true;
                    }
                });
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new EntryRecyclerAdapter(this, this, realmForUi.where(Entry.class).findAll()));
        recyclerView.setHasFixedSize(false);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_map:
                startActivity(MapsActivity.makeIntent(this, "", what, showFavourites));
                return true;
            case R.id.action_search:
//TODO do search stuff
                Toast.makeText(this, "Implement search", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            case R.id.action_refresh:
                DataFetchService.goFetchData(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void openItem(Entry data) {
        startActivity(DetailActivity.makeIntent(this, data.id));
    }

    @Override
    public void toggleFavourite(Entry data) {
        final String id = data.id;
        realmForUi.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Entry e = realm.where(Entry.class).equalTo(EntryFields.ID, id).findFirst();
                e.favourite = !e.favourite;
            }
        });
    }
}
