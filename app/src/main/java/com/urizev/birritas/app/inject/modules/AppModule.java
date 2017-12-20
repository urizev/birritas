package com.urizev.birritas.app.inject.modules;

import android.content.Context;

import com.urizev.birritas.app.App;
import com.urizev.birritas.app.providers.auth.Auth;
import com.urizev.birritas.app.providers.auth.FireAuth;
import com.urizev.birritas.app.providers.resources.AndroidResourceProvider;
import com.urizev.birritas.app.providers.resources.ResourceProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private final App mApp;

    public AppModule(App app) {
        this.mApp = app;
    }

    @Provides
    @Singleton
    App providesApplication() {
        return mApp;
    }

    @Provides
    @Singleton
    Context providesContext() {
        return mApp;
    }

    @Provides
    @Singleton
    ResourceProvider providesResourceProvider(AndroidResourceProvider resourceProvider) {
        return resourceProvider;
    }

    @Provides
    @Singleton
    Auth providesAuth(FireAuth auth) {
        return auth;
    }
}
