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

package net.maiatoday.afrikaburn.model;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.opencsv.CSVReader;

import net.maiatoday.afrikaburn.BuildConfig;
import net.maiatoday.afrikaburn.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Retention;
import java.net.URL;
import java.net.URLConnection;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmResults;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Created by maia on 2017/03/11.
 */

public class CSVDataLoader implements DataLoader {
    static final int NID = 0;
    static final int TITLE = 1;
    static final int CATEGORY = 2;
    static final int BLURB = 3;
    static final int ACTIVITY = 4;
    static final int GUIDE = 5;
    static final int TYPE = 6;
    static final int MAX_COLUMNS = 7;
    private static final String TAG = "CSVDataLoader";
    static Random random = new Random();
    private final Context context;

    public CSVDataLoader(Context context) {
        this.context = context;
    }

    private static Entry oneEntry(String[] row) throws InvalidParameterException {
        if (row.length < MAX_COLUMNS) {
            throw new InvalidParameterException("Expected 7 fields but got " + row.length);
        }
        Entry e = new Entry();
        e.title = row[TITLE];
        e.blurb = row[BLURB];
        e.id = row[NID];
        e.latitude = -32.326651 + random.nextDouble() / 1000;
        e.longitude = 19.747868 + random.nextDouble() / 1000;
        e.categories = row[CATEGORY];
        e.shortBlurb = row[GUIDE];
        e.what = whatFromType(row[TYPE]);
        return e;
    }

    private static int whatFromType(String s) {
        if (s.contains("Theme Camp")) return Entry.THEME_CAMP;
        if (s.contains("Mutant")) return Entry.MUTANT_VEHICLE;
        if (s.contains("Artwork")) return Entry.ART_WORK;
        if (s.contains("Performance")) return Entry.PERFORMANCE;
        if (s.contains("Burn")) return Entry.BURN;
        if (s.contains("Infrastructure")) return Entry.CLAN;
        return Entry.THEME_CAMP;
    }

    @Override
    public void addDefaultData() {
        startUpdate();
        List<Entry> entries = new ArrayList<>(); //TODO fix? we load everything into memory

        InputStream is = context.getResources().openRawResource(R.raw.entries);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

        CSVReader reader = new CSVReader(bufferedReader);
        try {
            List<String[]> myEntries = reader.readAll();
            for (String[] rowData : myEntries) {
                if (rowData[NID].equals("Nid")) {
                    //This is the first line so skip
                    continue;
                }
                try {
                    entries.add(oneEntry(rowData));
                } catch (InvalidParameterException e) {
                    Log.d(TAG, "addDefaultData: Bad row");
                }
            }
        } catch (IOException ex) {
            Log.e(TAG, "addDefaultData: bad read from entries file", ex);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Log.e(TAG, "addDefaultData: Could not close input stream.", e);
            }
        }

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(entries);
        realm.commitTransaction();
        realm.close();
        stopUpdate();

    }

    @WorkerThread
    @Override
    public void fetchDataFromNetwork() {
        startUpdate();
        List<Entry> entries = new ArrayList<>(); //TODO fix? we load everything into memory
        try {
            URL url = new URL(BuildConfig.DATA_URL);
            URLConnection connection = url.openConnection();
            connection.connect();
            InputStream is = new BufferedInputStream(url.openStream());

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

            CSVReader reader = new CSVReader(bufferedReader);
            List<String[]> myEntries = reader.readAll();
            for (String[] rowData : myEntries) {
                if (rowData[NID].equals("Nid")) {
                    //This is the first line so skip
                    continue;
                }
                try {
                    entries.add(oneEntry(rowData));
                } catch (InvalidParameterException e) {
                    Log.d(TAG, "addDefaultData: Bad row");
                }
            }
            is.close();
        } catch (IOException ex) {
            Log.e(TAG, "addDefaultData: bad read from entries file", ex);
        }

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(entries);
        realm.commitTransaction();
        realm.close();
        stopUpdate();
    }

    private void startUpdate() {
        Realm r = Realm.getDefaultInstance();
        r.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Home> homeResults = realm.where(Home.class).findAll();
                Home home = homeResults.first();
                home.busyFetching = true;
            }
        });
        r.close();
    }

    private void stopUpdate() {
        Realm r = Realm.getDefaultInstance();
        r.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Home> homeResults = realm.where(Home.class).findAll();
                Home home = homeResults.first();
                home.busyFetching = true;
                home.lastDataFetch = new Date();
            }
        });
        r.close();
    }

    @Retention(SOURCE)
    @IntDef({NID,
            TITLE,
            CATEGORY,
            BLURB,
            ACTIVITY,
            GUIDE,
            TYPE})
    public @interface ColumnId {
    }
}
