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
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Toast;

import com.afrikaburn.app.BuildConfig;
import com.afrikaburn.app.R;
import com.afrikaburn.app.databinding.ActivityDetailBinding;
import com.afrikaburn.app.model.Entry;
import com.afrikaburn.app.model.EntryFields;
import com.afrikaburn.app.ui.adapters.OnDetailEntryClickListener;
import com.afrikaburn.app.util.ColorUtils;
import com.afrikaburn.app.util.MapUtils;
import com.cocoahero.android.gmaps.addons.mapbox.MapBoxOfflineTileProvider;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class DetailActivity extends BaseActivity implements OnDetailEntryClickListener, OnMapReadyCallback {

    private static final String KEY_ID = BuildConfig.APPLICATION_ID +"."+ DetailActivity.class.getSimpleName()+".key_id";
    private static final int MAX_SNIPPET = 24;

    LatLng TANKWA_TOWN = new LatLng(-32.326651, 19.747868);
    LatLng TANKWA_TOWN_OVERLAY = new LatLng(-32.328554, 19.746236);
    String entryId;
    RealmResults<Entry> results;
    private GoogleMap map;
    private UiSettings uiSettings;
    private MapBoxOfflineTileProvider provider;
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
        setSupportActionBar(binding.toolbar);
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
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        int colorId = Entry.whatColorId(entry.what);
        int color = ContextCompat.getColor(this, colorId);
        binding.collapsingToolbar.setContentScrimColor(color);
        int colorIdDark = Entry.getDarkColorId(entry.what);
        int colorDark = ContextCompat.getColor(this, colorIdDark);
        binding.collapsingToolbar.setStatusBarScrimColor(colorDark);
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        mapFragment.getMapAsync(this);
        getSupportFragmentManager().beginTransaction().add(R.id.map_container, mapFragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        provider.close();
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
        map = googleMap;

        setupUI();

        if (!TextUtils.isEmpty(entryId) && RealmObject.isValid(entry)) {
            pointToPosition(new LatLng(entry.latitude, entry.longitude));
        } else {
            pointToPosition(TANKWA_TOWN);
        }

        showMarkers();
    }

    private void setupUI() {
        map.setMapType(GoogleMap.MAP_TYPE_NONE);
        uiSettings = map.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        //  uiSettings.setMyLocationButtonEnabled(true);
        //TODO request permission map.setMyLocationEnabled(true);

        TileOverlayOptions opts = new TileOverlayOptions();
        File myMBTiles = MapUtils.getMBTilesHandle(this);
        provider = new MapBoxOfflineTileProvider(myMBTiles);
        opts.tileProvider(provider);
        TileOverlay overlay = map.addTileOverlay(opts);


        GroundOverlayOptions tankwaMap = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.mipmap.overlay))
                .position(TANKWA_TOWN_OVERLAY, 2191f, 1534f);
        map.addGroundOverlay(tankwaMap);

    }

    private void showMarkers() {
        // map.addMarker(new MarkerOptions().position(TANKWA_TOWN).title("Tankwa Town"));
        if (!results.isEmpty()) {
            for (Entry e : results) {
                showMarkerForEntry(e);
            }
        }

    }

    private void showMarkerForEntry(Entry e) {
        LatLng latLng = new LatLng(e.latitude, e.longitude);
        String snippet = "";
        if (!TextUtils.isEmpty(e.blurb)) {
            snippet = e.blurb.substring(0, (e.blurb.length() > MAX_SNIPPET) ? MAX_SNIPPET : e.blurb.length());
        }
        @ColorRes
        int colorResId = Entry.whatColorId(e.what);
        float hue = ColorUtils.getHsvFromColorId(this, colorResId)[0];
        map.addMarker(new MarkerOptions()
                .position(latLng)
                .title(e.title)
                .snippet(snippet)
                .icon(BitmapDescriptorFactory.defaultMarker(hue)));
    }

    private void pointToPosition(LatLng position) {
        //Build camera position
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(position)
                .zoom(17).build();
        //Zoom in and animate the camera.
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        //  map.moveCamera(CameraUpdateFactory.newLatLng(TANKWA_TOWN));
    }

}
