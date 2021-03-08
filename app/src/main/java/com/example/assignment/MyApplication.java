package com.example.assignment;

import android.app.Application;

import androidx.databinding.DataBindingUtil;

import com.example.assignment.databinding.AppDataBindingComponent;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DataBindingUtil.setDefaultComponent(new AppDataBindingComponent());
        Realm.init(getApplicationContext());
        RealmConfiguration config =
                new RealmConfiguration.Builder()
                        .name("android_assignment.db")
                        .schemaVersion(1)
                        .deleteRealmIfMigrationNeeded()
                        .build();
        Realm.setDefaultConfiguration(config);
    }
}
