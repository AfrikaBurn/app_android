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

import java.util.Date;
import java.util.Random;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A class to generate fake data
 * Created by maia on 2017/03/04.
 */

public class FakeDataLoader implements DataLoader {
    static Random random = new Random();

    private static void oneEntry(Realm realm, String title, String blurb, @Entry.What int what) {
        String uuid = UUID.randomUUID().toString();
        Entry e = realm.createObject(Entry.class, uuid);
        e.title = title;
        e.blurb = blurb;
        e.what = what;
        e.latitude = -32.326651 + random.nextDouble() / 1000;
        e.longitude = 19.747868 + random.nextDouble() / 1000;
        e.favourite = random.nextBoolean();
        e.categories = "children/aliens/late night/early morning/ yoga";
    }

    @Override
    public void addData() {
        Realm realm = Realm.getDefaultInstance();
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
                oneEntry(realm, "Camp Lorem", "Fresh lorem in the morning ... Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut vel arcu molestie, blandit nisi eu, elementum lectus. Suspendisse blandit nulla quis ligula consectetur, non tempor nisl pharetra. Nunc placerat metus nisl, eget malesuada ipsum aliquam non. Aenean a nunc mollis augue elementum maximus vitae ut mi. Vivamus ac neque a mauris molestie rhoncus. Pellentesque aliquam diam non accumsan accumsan. Mauris at euismod felis, vitae porttitor velit. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Quisque vel pellentesque nisi. Suspendisse pellentesque sagittis velit, at efficitur lacus hendrerit ut. Nulla ut magna ut tortor tincidunt viverra. Vivamus eleifend lacus vitae enim facilisis convallis. Integer et dui in diam sodales feugiat et sit amet erat. Cras congue pretium ex, id pretium arcu.", Entry.THEME_CAMP);
                oneEntry(realm, "Camp Ipsum", "Spiced ipsum at lunch, Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut vel arcu molestie, blandit nisi eu, elementum lectus. Suspendisse blandit nulla quis ligula consectetur, non tempor nisl pharetra. Nunc placerat metus nisl, eget malesuada ipsum aliquam non. Aenean a nunc mollis augue elementum maximus vitae ut mi. Vivamus ac neque a mauris molestie rhoncus. Pellentesque aliquam diam non accumsan accumsan. Mauris at euismod felis, vitae porttitor velit. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Quisque vel pellentesque nisi. Suspendisse pellentesque sagittis velit, at efficitur lacus hendrerit ut. Nulla ut magna ut tortor tincidunt viverra. Vivamus eleifend lacus vitae enim facilisis convallis. Integer et dui in diam sodales feugiat et sit amet erat. Cras congue pretium ex, id pretium arcu.", Entry.THEME_CAMP);
                oneEntry(realm, "Camp Dolor", "Steeped dolor at midnight, Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut vel arcu molestie, blandit nisi eu, elementum lectus. Suspendisse blandit nulla quis ligula consectetur, non tempor nisl pharetra. Nunc placerat metus nisl, eget malesuada ipsum aliquam non. Aenean a nunc mollis augue elementum maximus vitae ut mi. Vivamus ac neque a mauris molestie rhoncus. Pellentesque aliquam diam non accumsan accumsan. Mauris at euismod felis, vitae porttitor velit. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Quisque vel pellentesque nisi. Suspendisse pellentesque sagittis velit, at efficitur lacus hendrerit ut. Nulla ut magna ut tortor tincidunt viverra. Vivamus eleifend lacus vitae enim facilisis convallis. Integer et dui in diam sodales feugiat et sit amet erat. Cras congue pretium ex, id pretium arcu.", Entry.THEME_CAMP);

                // Clan camps
                oneEntry(realm, "DMV", "Department of mutant Vehicles.", Entry.CLAN);
                oneEntry(realm, "Sanctuary", "Help! I need somebody", Entry.CLAN);
                oneEntry(realm, "Medics", "Help I need somebody, Not just anybody", Entry.CLAN);
                oneEntry(realm, "Off centre camp", "Whoops, steady... steady", Entry.CLAN);

                // Mutant vehicles
                oneEntry(realm, "Huge Lorem snail", "Leaving slithering trails of LED light, Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut vel arcu molestie, blandit nisi eu, elementum lectus. Suspendisse blandit nulla quis ligula consectetur, non tempor nisl pharetra. Nunc placerat metus nisl, eget malesuada ipsum aliquam non. Aenean a nunc mollis augue elementum maximus vitae ut mi. Vivamus ac neque a mauris molestie rhoncus. Pellentesque aliquam diam non accumsan accumsan. Mauris at euismod felis, vitae porttitor velit. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Quisque vel pellentesque nisi. Suspendisse pellentesque sagittis velit, at efficitur lacus hendrerit ut. Nulla ut magna ut tortor tincidunt viverra. Vivamus eleifend lacus vitae enim facilisis convallis. Integer et dui in diam sodales feugiat et sit amet erat. Cras congue pretium ex, id pretium arcu.Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut vel arcu molestie, blandit nisi eu, elementum lectus. Suspendisse blandit nulla quis ligula consectetur, non tempor nisl pharetra. Nunc placerat metus nisl, eget malesuada ipsum aliquam non. Aenean a nunc mollis augue elementum maximus vitae ut mi. Vivamus ac neque a mauris molestie rhoncus. Pellentesque aliquam diam non accumsan accumsan. Mauris at euismod felis, vitae porttitor velit. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Quisque vel pellentesque nisi. Suspendisse pellentesque sagittis velit, at efficitur lacus hendrerit ut. Nulla ut magna ut tortor tincidunt viverra. Vivamus eleifend lacus vitae enim facilisis convallis. Integer et dui in diam sodales feugiat et sit amet erat. Cras congue pretium ex, id pretium arcu. acus hendrerit ut. Nulla ut magna ut tortor tincidunt viverra. Vivamus eleifend lacus vitae enim facilisis convallis. Integer et dui in diam sodales feugiat et sit amet erat. Cras congue pretium ex, id pretium arcu.Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut vel arcu molestie, blandit nisi eu, elementum lectus. Suspendisse blandit nulla quis ligula consectetur, non tempor nisl pharetra. Nunc placerat metus nisl, eget malesuada ipsum aliquam non. Aenean a nunc mollis augue elementum maximus vitae ut mi. Vivamus ac neque a mauris molestie rhoncus. Pellentesque aliquam diam non accumsan accumsan. Mauris at euismod felis, vitae porttitor velit. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Quisque vel pellentesque nisi. Suspendisse pellentesque sagittis velit, at efficitur lacus hendrerit ut. Nulla ut magna ut tortor tincidunt viverra. Vivamus eleifend lacus vitae enim facilisis convallis. Integer et dui in diam sodales feugiat et sit amet erat. Cras congue pretium ex, id pretium arcu. acus hendrerit ut. Nulla ut magna ut tortor tincidunt viverra. Vivamus eleifend lacus vitae enim facilisis convallis. Integer et dui in diam sodales feugiat et sit amet erat. Cras congue pretium ex, id pretium arcu.Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut vel arcu molestie, blandit nisi eu, elementum lectus. Suspendisse blandit nulla quis ligula consectetur, non tempor nisl pharetra. Nunc placerat metus nisl, eget malesuada ipsum aliquam non. Aenean a nunc mollis augue elementum maximus vitae ut mi. Vivamus ac neque a mauris molestie rhoncus. Pellentesque aliquam diam non accumsan accumsan. Mauris at euismod felis, vitae porttitor velit. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Quisque vel pellentesque nisi. Suspendisse pellentesque sagittis velit, at efficitur lacus hendrerit ut. Nulla ut magna ut tortor tincidunt viverra. Vivamus eleifend lacus vitae enim facilisis convallis. Integer et dui in diam sodales feugiat et sit amet erat. Cras congue pretium ex, id pretium arcu", Entry.MUTANT_VEHICLE);
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

        //   FirebaseCrash.log("Fake data generated");

        realm.close();

    }
}
