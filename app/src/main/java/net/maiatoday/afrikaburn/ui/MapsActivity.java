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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import net.maiatoday.afrikaburn.BuildConfig;
import net.maiatoday.afrikaburn.R;
import net.maiatoday.afrikaburn.model.Entry;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback {
    private static final String KEY_ID = BuildConfig.APPLICATION_ID + "." + MapsActivity.class.getSimpleName() + ".key_id";
    private static final String KEY_WHAT = BuildConfig.APPLICATION_ID + "." + MapsActivity.class.getSimpleName() + ".key_what";
    private static final String KEY_FAVOURITES = BuildConfig.APPLICATION_ID + "." + MapsActivity.class.getSimpleName() + ".key_favourites";

    private GoogleMap mMap;
    // parameters that determine which points to show and what to query initially, the query can change with search
    private String entryId;

    @Entry.What
    private int what;
    private boolean showFavourites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        getIntentInfo();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_main:
                finish();
                return true;
            case R.id.action_search:
//TODO do search stuff
                return true;
            case R.id.action_about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }


    public static Intent makeIntent(Context context, String entryId,
                                    @Entry.What int what, boolean favourites) {
        Intent i = new Intent(context, MapsActivity.class);
        i.putExtra(KEY_ID, entryId);
        i.putExtra(KEY_WHAT, what);
        i.putExtra(KEY_FAVOURITES, favourites);
        return i;
    }

    private void getIntentInfo() {
        Intent i = getIntent();
        if (i.hasExtra(KEY_ID)) {
            entryId = i.getStringExtra(KEY_ID);
        } else {
            entryId = "";
        }
        //noinspection ResourceType
        what = i.getIntExtra(KEY_WHAT, Entry.ALL);
        showFavourites = i.getBooleanExtra(KEY_FAVOURITES, false);
    }
}
