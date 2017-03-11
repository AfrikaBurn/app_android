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

import net.maiatoday.afrikaburn.model.CSVDataLoader;
import net.maiatoday.afrikaburn.model.DataLoader;

public class DataFetchService extends GcmTaskService {
    private static final String TAG = "DataFetchService";

    private static final String TASK_TAG_FETCH_DATA = "AB_fetch_data";

    // static method to trigger the data fetch job
    public static void goFetchData(Context context) {
        Log.d(TAG, "goFetchData: I fetch data when I can.");
        OneoffTask task = new OneoffTask.Builder()
                .setService(DataFetchService.class)
                .setTag(TASK_TAG_FETCH_DATA)
                .setUpdateCurrent(true) // If there is a pending job, do this one
                .setExecutionWindow(0L, 30L) // within the next 30 seconds when convenient
               // .setRequiredNetwork(Task.NETWORK_STATE_ANY) //TODO set the required network once we really access the interwebz
                .build();

        GcmNetworkManager.getInstance(context.getApplicationContext()).schedule(task);
    }

    public static void goFetchDataNow(Context context) {
        //TODO fetch the data from the interwebz
        DataLoader loader = new CSVDataLoader(context);
        // Put the data into Realm
        // Add fake data for now TODO remove
        loader.addData(); //TODO remove
    }

    @Override
    public int onRunTask(TaskParams taskParams) {
        Log.d(TAG, "onRunTask: I fetch data NOW");
        //TODO fetch the data from the interwebz
        DataLoader loader = new CSVDataLoader(this);
        // Put the data into Realm
        // Add fake data for now TODO remove
        loader.addData(); //TODO remove
        return GcmNetworkManager.RESULT_SUCCESS;
    }

}
