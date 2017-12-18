package com.urizev.birritas.app;

import android.app.Application;

import com.urizev.birritas.BuildConfig;
import com.urizev.birritas.app.inject.components.DaggerNetComponent;
import com.urizev.birritas.app.inject.components.NetComponent;
import com.urizev.birritas.app.inject.modules.AppModule;
import com.urizev.birritas.app.inject.modules.NetModule;

import timber.log.Timber;

public class App extends Application {
    private NetComponent mNetComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        mNetComponent = DaggerNetComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule())
                .build();
    }

    public NetComponent getNetComponent() {
        return mNetComponent;
    }
}
