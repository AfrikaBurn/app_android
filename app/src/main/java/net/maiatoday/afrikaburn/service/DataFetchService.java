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

package net.maiatoday.afrikaburn.service;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.OneoffTask;
import com.google.android.gms.gcm.TaskParams;

import net.maiatoday.afrikaburn.model.Entry;
import net.maiatoday.afrikaburn.model.Home;

import java.util.Date;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;

public class DataFetchService extends GcmTaskService {
    private static final String TAG = "DataFetchService";

    private static final String TASK_TAG_FETCH_DATA = "AB_fetch_data";

    @Override
    public int onRunTask(TaskParams taskParams) {
        Log.d(TAG, "onRunTask: I fetch data NOW");
        //TODO fetch the data from the interwebz
        Realm realm = Realm.getDefaultInstance();
        // Put the data into Realm
        // Add fake data for now TODO remove
        generateFakeData(realm); //TODO remove
        realm.close();
        return GcmNetworkManager.RESULT_SUCCESS;
    }


    // static method to trigger the data fetch job
    public static void goFetchData(Context context) {
        Log.d(TAG, "goFetchData: I fetch data when I can.");
        OneoffTask task = new OneoffTask.Builder()
                .setService(DataFetchService.class)
                .setTag(TASK_TAG_FETCH_DATA)
                .setUpdateCurrent(true)
                .setExecutionWindow(0L, 30L)
               // .setRequiredNetwork(Task.NETWORK_STATE_ANY) //TODO set the required network once we really access the interwebz
                .build();

        GcmNetworkManager.getInstance(context.getApplicationContext()).schedule(task);
    }


    public static void generateFakeData(Realm realm) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // set data fetching flag to true
                Home home;
                RealmResults<Home> homeResults = realm.where(Home.class).findAll();
                if (homeResults.isEmpty()) {
                    home = realm.createObject(Home.class);
                } else {
                    home = homeResults.get(0); //Always only one
                }
                home.busyFetching = true;
                //First delete all the previous sample data
                realm.where(Entry.class).findAll().deleteAllFromRealm();

                // Theme camps
                oneEntry(realm, "Camp Lorem", "Fresh lorem in the morning, spiced ipsum at lunch, steaped dolor at midnight", Entry.THEME_CAMP);
                oneEntry(realm, "Camp Ipsum", "Spiced ipsum at lunch", Entry.THEME_CAMP);
                oneEntry(realm, "Camp Dolor", "Steaped dolor at midnight", Entry.THEME_CAMP);

                // Clan camps
                oneEntry(realm, "DMV", "Department of mutant Vehicles", Entry.CLAN);
                oneEntry(realm, "Sanctuary", "Help! I need somebody", Entry.CLAN);
                oneEntry(realm, "Medics", "Help I need somebody, Not just anybody", Entry.CLAN);
                oneEntry(realm, "Off centre camp", "Whoops", Entry.CLAN);

                // Mutant vehicles
                oneEntry(realm, "Huge Lorem snail", "Leaving slithering trails of LED light", Entry.MUTANT_VEHICLE);
                oneEntry(realm, "Noizy dolor Bus", "Big noisy bus, filled with music. Surrounded by people dancing and picking up MOOP", Entry.MUTANT_VEHICLE);

                // Performances
                oneEntry(realm, " et pretium tortor faucibus", "Quisque rhoncus diam quis ex pharetra fringilla. Proin placerat dui non mi interdum, et pretium tortor faucibus.", Entry.PERFORMANCE);

                // Artworks
                oneEntry(realm, "nulla maximus libero", "Etiam imperdiet nulla maximus libero ultricies ultricies consequat eget libero", Entry.ART_WORK);

                // Burns
                oneEntry(realm, "lacus", "lacus lacus lacus lacus", Entry.BURN);
                oneEntry(realm, "suscipit", "suscipit suscipitsuscipit suscipit", Entry.BURN);
                oneEntry(realm, "tempus", "tempustempustempus tempus tempustempustempus", Entry.BURN);

                home.busyFetching = false;
                home.lastDataFetch = new Date();
            }
        });

    }

    private static void oneEntry(Realm realm, String title, String blurb, @Entry.What int what) {
        String uuid = UUID.randomUUID().toString();
        Entry e = realm.createObject(Entry.class, uuid);
        e.title = title;
        e.blurb = blurb;
        e.what = what;
        e.latitude = -32.3003346;
        e.longitude = 19.9951357;
    }

}
