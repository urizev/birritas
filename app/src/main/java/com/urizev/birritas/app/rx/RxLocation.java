package com.urizev.birritas.app.rx;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

@Singleton
public class RxLocation extends LocationCallback {
    private static final String PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_INTERVAL = 10000;
    private static final int LOCATION_FASTEST_INTERVAL = 5000;

    private final BehaviorSubject<Boolean> mPermissionStatusSubject;
    private final BehaviorSubject<Location> mLastLocationSubject;
    private final Context mContext;
    private final Location mDefaultLocation;
    private final FusedLocationProviderClient mFusedLocationClient;
    private final LocationRequest mLocationRequest;
    private final Disposable mListenDisposable;

    @Inject
    RxLocation(Context context, RxForeground foreground) {
        this.mDefaultLocation = new Location("internal");
        this.mDefaultLocation.setLatitude(37.774929);
        this.mDefaultLocation.setLongitude(-122.419416);
        this.mDefaultLocation.setTime(0);
        mLocationRequest = new LocationRequest()
                .setInterval(LOCATION_INTERVAL)
                .setFastestInterval(LOCATION_FASTEST_INTERVAL)
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        this.mLastLocationSubject = BehaviorSubject.create();
        this.mPermissionStatusSubject = BehaviorSubject.createDefault(checkPermission());
        this.mContext = context.getApplicationContext();
        Observable<Boolean> fgObservable = foreground.observe()
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation());

        mListenDisposable = Observable.zip(fgObservable, mPermissionStatusSubject, (fg, perm) -> fg && perm)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .distinctUntilChanged()
                .doOnNext(this::listenLocationUpdates)
                .subscribe();
    }

    public void updatePermission() {
        mPermissionStatusSubject.onNext(checkPermission());
    }

    public Observable<Location> observe () {
        return mLastLocationSubject;
    }

    public Maybe<Location> observeLast (long minTimeMillis) {
        return mLastLocationSubject
                .filter(location -> location.getTime() > minTimeMillis)
                .firstElement();
    }

    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onLocationResult(LocationResult result) {
        mLastLocationSubject.onNext(result.getLastLocation());
    }

    private void listenLocationUpdates(boolean listen) {
        if (!checkPermission()) {
            return;
        }

        if (listen) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, this, null);
        }
        else {
            mFusedLocationClient.removeLocationUpdates(this);
        }
    }
}
