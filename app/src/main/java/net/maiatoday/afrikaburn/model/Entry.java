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

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.util.Date;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.Required;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Generic entry class for all the different things at Afrika Burn,
 * Created by maia on 2017/03/02.
 */

@RealmClass
public class Entry implements RealmModel {
    @PrimaryKey
    @Required
    String id;
    @Required
    public String title;
    public String blurb;
    /// Free-form description of activities and vague indication of time, will be searchable
    public String categories;
    /// If not null there is a specific start time, sortof
    public Date startTime;
    /// categorisation to allow filter from the UI
    @What
    public int what;
    /// latitude :)
    public Double latitude;
    /// longitude :D
    public Double longitude;
    /// Favourite - only stored locally
    boolean favourite;

    String getStartText() {
        //TODO format the start time if it exists
        return "Thursday 20 to 7";
    }

    @Retention(SOURCE)
    @IntDef({THEME_CAMP,
            ART_WORK,
            BURN,
            PERFORMANCE,
            MUTANT_VEHICLE,
            CLAN})
    public @interface What {}
    public static final int THEME_CAMP = 0;
    public static final int ART_WORK = 1;
    public static final int BURN = 2;
    public static final int PERFORMANCE = 3;
    public static final int MUTANT_VEHICLE = 4;
    public static final int CLAN = 5;
}
