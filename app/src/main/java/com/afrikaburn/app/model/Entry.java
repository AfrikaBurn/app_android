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

package com.afrikaburn.app.model;

import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.StringRes;

import com.afrikaburn.app.R;

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
    public static final int ALL = -1;
    public static final int THEME_CAMP = 0;
    public static final int ART_WORK = 1;
    public static final int BURN = 2;
    public static final int PERFORMANCE = 3;
    public static final int MUTANT_VEHICLE = 4;
    public static final int CLAN = 5;
    @PrimaryKey
    @Required
    public
    String id;
    @Required
    public String title;
    public String blurb;
    public String shortBlurb;
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
    public boolean favourite;

    @DrawableRes
    public static int whatToDrawableId(int what) {
        switch (what) {
            case THEME_CAMP:
                return R.drawable.ic_theme;
            case ART_WORK:
                return R.drawable.ic_artworks;
            case BURN:
                return R.drawable.ic_burns;
            case PERFORMANCE:
                return R.drawable.ic_performances;
            case MUTANT_VEHICLE:
                return R.drawable.ic_mutant_vehicles;
            case CLAN:
                return R.drawable.ic_infrastructure;
            default:
                return R.drawable.ic_theme;
        }
    }

    public static int getDarkColorId(int what) {
        switch (what) {
            case THEME_CAMP:
                return R.color.colour_dark_camp;
            case ART_WORK:
                return R.color.colour_dark_artwork;
            case BURN:
                return R.color.colour_dark_burn;
            case PERFORMANCE:
                return R.color.colour_dark_performance;
            case MUTANT_VEHICLE:
                return R.color.colour_dark_mutant_vehicle;
            case CLAN:
                return R.color.colour_dark_clan;
            default:
                return R.color.colour_burnback_dark;
        }
    }

    public static int whatColorId(int what) {
        switch (what) {
            case THEME_CAMP:
                return R.color.colour_camp;
            case ART_WORK:
                return R.color.colour_artwork;
            case BURN:
                return R.color.colour_burn;
            case PERFORMANCE:
                return R.color.colour_performance;
            case MUTANT_VEHICLE:
                return R.color.colour_mutant_vehicle;
            case CLAN:
                return R.color.colour_clan;
            default:
                return R.color.colorPrimary;
        }
    }

    public String getStartText() {
        //TODO format the start time if it exists
        return "Thursday 20 to 7";
    }

    public String latitudeText() {
        if (latitude != null) {
            return String.valueOf(latitude);
        }
        return "";
    }

    public String longitudeText() {
        if (longitude != null) {
            return String.valueOf(longitude);
        }
        return "";
    }

    public int whatDrawableId() {
        return whatToDrawableId(what);
    }

    @StringRes
    public int getWhatString() {
        switch (what) {
            case THEME_CAMP:
                return R.string.title_one_theme;
            case ART_WORK:
                return R.string.title_one_artworks;
            case BURN:
                return R.string.title_one_burns;
            case PERFORMANCE:
                return R.string.title_one_performance;
            case MUTANT_VEHICLE:
                return R.string.title_one_mutant_vehicles;
            case CLAN:
                return R.string.title_one_infrastructure;
            case ALL:
                return R.string.app_name;
            default:
                return R.string.title_one_theme;
        }
    }
    @Retention(SOURCE)
    @IntDef({ALL,
            THEME_CAMP,
            ART_WORK,
            BURN,
            PERFORMANCE,
            MUTANT_VEHICLE,
            CLAN})
    public @interface What {}

}
