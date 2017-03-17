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

import com.afrikaburn.app.R;

import java.lang.annotation.Retention;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * A user created marker
 * Created by maia on 2017/03/17.
 */

public class UserMarker implements RealmModel {

    @PrimaryKey
    @Required
    public
    String id;
    @Required
    public String title;
    int iconId;
    /// latitude :)
    Double latitude;
    /// longtitude :D
    Double longitude;

    @DrawableRes
    public static int getDrawableId(int iconId) {
        switch (iconId) {
            default:
            case STAR:
                return R.drawable.ic_theme;
            case HEART:
                return R.drawable.ic_artworks;
            case BIKE:
                return R.drawable.ic_burns;
            case HOME:
                return R.drawable.ic_performances;
        }
    }

    @Retention(SOURCE)
    @IntDef({STAR,
            HEART,
            BIKE,
            HOME})
    public @interface IconType {
    }


    public static final int STAR = 0;
    public static final int HEART = 1;
    public static final int BIKE = 2;
    public static final int HOME = 3;
}
