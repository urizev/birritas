package com.urizev.birritas.app.providers.location;

import android.location.Location;

import com.urizev.birritas.app.rx.RxUtils;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public abstract class LocationProvider {
    private final BehaviorSubject<Location> subject;
    private int subscriberCount;

    LocationProvider() {
        subject = BehaviorSubject.create();
        this.subscriberCount = 0;
    }

    public Observable<Location> locations() {
        return subject
                .doOnSubscribe(d -> this.onSubscriberAdded())
                .doOnDispose(this::onSubscriberRemoved)
                .subscribeOn(Schedulers.computation());
    }

    private void onSubscriberRemoved() {
        RxUtils.assertComputationThread();
        if (--subscriberCount <= 0) {
            stopMonitoringLocation();
            subscriberCount = 0;
        }
    }

    private void onSubscriberAdded() {
        RxUtils.assertComputationThread();
        RxUtils.assertComputationThread();
        if (subscriberCount++ == 0) {
            startMonitoringLocation();
        }
    }

    protected abstract void stopMonitoringLocation();
    protected abstract void startMonitoringLocation();
}
