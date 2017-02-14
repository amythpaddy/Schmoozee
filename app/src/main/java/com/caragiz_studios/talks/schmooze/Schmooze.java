package com.caragiz_studios.talks.schmooze;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by caragiz on 13/2/17.
 */

public class Schmooze extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
