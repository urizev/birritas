package com.urizev.birritas.app.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.urizev.birritas.app.App;
import com.urizev.birritas.app.rx.RxForeground;

import javax.inject.Inject;

public abstract class BaseActivity extends AppCompatActivity {
    @Inject
    RxForeground foreground;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApp().getNetComponent().inject(this);
    }

    @Override
    protected void onPause() {
        foreground.update(false);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        foreground.update(true);
    }

    public App getApp() {
        return (App) getApplication();
    }
}
