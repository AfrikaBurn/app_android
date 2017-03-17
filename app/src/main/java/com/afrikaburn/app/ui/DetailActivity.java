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

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.afrikaburn.app.BuildConfig;
import com.afrikaburn.app.R;
import com.afrikaburn.app.databinding.ActivityDetailBinding;
import com.afrikaburn.app.model.Entry;
import com.afrikaburn.app.model.EntryFields;
import com.afrikaburn.app.ui.adapters.OnDetailEntryClickListener;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class DetailActivity extends BaseActivity implements OnDetailEntryClickListener {

    private static final String KEY_ID = BuildConfig.APPLICATION_ID +"."+ DetailActivity.class.getSimpleName()+".key_id";

    String entryId;
    RealmResults<Entry> results;
    private ActivityDetailBinding binding;
    private Entry entry;
    private RealmChangeListener<Entry> entryListener = new RealmChangeListener<Entry>() {
        @Override
        public void onChange(Entry entry) {
            //We could do this with two way data binding...
            if (!RealmObject.isValid(entry)) {
                Toast.makeText(DetailActivity.this, "Entry deleted.", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                binding.setData(entry);
            }
        }
    };

    public static Intent makeIntent(Context context, String id) {
        Intent i = new Intent(context, DetailActivity.class);
        i.putExtra(KEY_ID, id);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentInfo();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        results = realmForUi.where(Entry.class).equalTo(EntryFields.ID, entryId).findAll();
        if (results.isEmpty()) {
            Toast.makeText(this, "Woops, can't find the Entry.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            entry = realmForUi.where(Entry.class).equalTo(EntryFields.ID, entryId).findFirst();
        }
        binding.setData(entry);
        binding.setHandler(this);
        RealmObject.addChangeListener(entry, entryListener);
        getSupportActionBar().setTitle(getString(entry.getWhatString()));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        int colorId = Entry.whatColorId(entry.what);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, colorId)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_map:
                startActivity(MapsActivity.makeIntent(this, entryId, Entry.ALL, false));
                return true;
            case R.id.action_about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getIntentInfo() {
        Intent i = getIntent();
        if (i.hasExtra(KEY_ID)) {
            entryId = i.getStringExtra(KEY_ID);
        } else {
            finish(); // can't show an empty item
        }
    }

    @Override
    public void toggleFavourite(Entry data) {
        realmForUi.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Entry e = realm.where(Entry.class).equalTo(EntryFields.ID, entryId).findFirst();
                e.favourite = !e.favourite;
            }
        });
    }

}
