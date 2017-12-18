package com.urizev.birritas.app.base;

import android.support.v7.app.AppCompatActivity;

import com.urizev.birritas.app.App;

public abstract class BaseActivity extends AppCompatActivity {
    public App getApp() {
        return (App) getApplication();
    }
}
