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
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

import net.maiatoday.afrikaburn.BuildConfig;
import net.maiatoday.afrikaburn.R;

import io.realm.Realm;

public class DetailActivity extends BaseActivity {

    private static final String KEY_ID = BuildConfig.APPLICATION_ID +"."+ DetailActivity.class.getSimpleName()+".key_id";

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getIntentInfo();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static Intent makeIntent(Context context, String id) {
        Intent i = new Intent(context, DetailActivity.class);
        i.putExtra(KEY_ID, id);
        return i;
    }
    private void getIntentInfo() {
        Intent i = getIntent();
        if (i.hasExtra(KEY_ID)) {
            id = i.getStringExtra(KEY_ID);
        } else {
            id = "";
        }
    }
}
