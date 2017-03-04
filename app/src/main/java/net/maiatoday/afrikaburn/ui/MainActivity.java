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

package net.maiatoday.afrikaburn.ui;

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

import net.maiatoday.afrikaburn.R;
import net.maiatoday.afrikaburn.databinding.ActivityMainBinding;
import net.maiatoday.afrikaburn.model.Entry;
import net.maiatoday.afrikaburn.model.Home;

import io.realm.RealmResults;

public class MainActivity extends BaseActivity implements OnEntryClickListener {

    Home home;
    RecyclerView recyclerView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_theme_camps:
                    recyclerView.setAdapter(new EntryRecyclerAdapter(MainActivity.this,
                            MainActivity.this,
                            realm.where(Entry.class).equalTo(Entry.WHAT, Entry.THEME_CAMP).findAll()));
                    return true;
                case R.id.navigation_artworks:
                    recyclerView.setAdapter(new EntryRecyclerAdapter(MainActivity.this,
                            MainActivity.this,
                            realm.where(Entry.class).equalTo(Entry.WHAT, Entry.ART_WORK).findAll()));
                    return true;
                case R.id.navigation_infrastructure:
                    recyclerView.setAdapter(new EntryRecyclerAdapter(MainActivity.this,
                            MainActivity.this,
                            realm.where(Entry.class).equalTo(Entry.WHAT, Entry.CLAN).findAll()));
                    return true;
//                case R.id.navigation_performance:
//                    mTextMessage.setText(R.string.title_performance);
//                    return true;
                case R.id.navigation_burn:

                    recyclerView.setAdapter(new EntryRecyclerAdapter(MainActivity.this,
                            MainActivity.this,
                            realm.where(Entry.class).equalTo(Entry.WHAT, Entry.BURN).findAll()));
                    return true;
//                case R.id.navigation_mutant_vehicles:
//
//                    recyclerView.setAdapter(new EntryRecyclerAdapter(MainActivity.this,
//                            MainActivity.this,
//                            realm.where(Entry.class).equalTo(Entry.WHAT, Entry.MUTANT_VEHICLE).findAll()));
//                    return true;
                case R.id.navigation_favourites:
                    recyclerView.setAdapter(new EntryRecyclerAdapter(MainActivity.this,
                            MainActivity.this,
                            realm.where(Entry.class).equalTo(Entry.FAVOURITE, true).findAll()));
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        RealmResults<Home> res = realm.where(Home.class).findAll();
        if (!res.isEmpty()) {
            home = res.get(0);
        }
        binding.setHome(home);
        recyclerView = binding.list;
        setupRecyclerView();
        //  mTextMessage = binding.message;
        BottomNavigationView navigation = binding.navigation;
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new EntryRecyclerAdapter(this, this, realm.where(Entry.class).findAll()));
        recyclerView.setHasFixedSize(true);
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
            case R.id.action_main:
                recyclerView.setAdapter(new EntryRecyclerAdapter(MainActivity.this,
                        MainActivity.this,
                        realm.where(Entry.class).findAll()));
                return true;
            case R.id.action_map:
                startActivity(MapsActivity.makeIntent(this, ""));
                return true;
            case R.id.action_search:
//TODO do search stuff
                Toast.makeText(this, "Implement search", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void openItem(Entry data) {
        startActivity(DetailActivity.makeIntent(this, data.id));
    }
}
