package com.urizev.birritas.app.inject.modules;

import android.content.Context;

import com.urizev.birritas.app.App;
import com.urizev.birritas.app.providers.AndroidResourceProvider;
import com.urizev.birritas.app.providers.ResourceProvider;
import com.urizev.birritas.domain.usecases.UseCase;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

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
}
