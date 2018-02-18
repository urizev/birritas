package com.urizev.birritas.app.rx;

import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

@Singleton
public class RxForeground implements ComponentCallbacks2 {
    private final BehaviorSubject<Boolean> foregroundSubject;
    @Inject
    public RxForeground(Context context) {
        this.foregroundSubject = BehaviorSubject.createDefault(false);
        context.registerComponentCallbacks(this);
    }

    public Observable<Boolean> observe() {
        return foregroundSubject.distinctUntilChanged();
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {

    }

    @Override
    public void onLowMemory() {

    }

    @Override
    public void onTrimMemory(int type) {
        switch (type) {
            case TRIM_MEMORY_RUNNING_MODERATE:
            case TRIM_MEMORY_RUNNING_CRITICAL:
            case TRIM_MEMORY_RUNNING_LOW:
                foregroundSubject.onNext(true);
                break;
            case TRIM_MEMORY_COMPLETE:
            case TRIM_MEMORY_MODERATE:
            case TRIM_MEMORY_BACKGROUND:
            case TRIM_MEMORY_UI_HIDDEN:
                foregroundSubject.onNext(false);
                break;
        }
    }
}
