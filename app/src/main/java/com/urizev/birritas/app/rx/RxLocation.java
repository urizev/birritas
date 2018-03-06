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
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import timber.log.Timber;

@Singleton
public class RxLocation extends LocationCallback {
    private static final String PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_INTERVAL = 10000;
    private static final int LOCATION_FASTEST_INTERVAL = 5000;
    public static final Location DEFAULT_LOCATION = new Location ("internal");

    static {
        DEFAULT_LOCATION.setLatitude(37.774929);
        DEFAULT_LOCATION.setLongitude(-122.419416);
        DEFAULT_LOCATION.setTime(0);
    }

    private final BehaviorSubject<Boolean> mPermissionStatusSubject;
    private final BehaviorSubject<Location> mLastLocationSubject;
    private final Context mContext;
    private final FusedLocationProviderClient mFusedLocationClient;
    private final LocationRequest mLocationRequest;
    private final Disposable mListenDisposable;

    @Inject
    RxLocation(Context context, RxForeground foreground) {
        this.mContext = context.getApplicationContext();

        mLocationRequest = new LocationRequest()
                .setInterval(LOCATION_INTERVAL)
                .setFastestInterval(LOCATION_FASTEST_INTERVAL)
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        this.mLastLocationSubject = BehaviorSubject.create();
        this.mPermissionStatusSubject = BehaviorSubject.createDefault(checkPermission());

        Observable<Boolean> fgObservable = foreground.observe()
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation());


        ObservableSource<Boolean> permObservable = mPermissionStatusSubject
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation()).doOnNext(p -> Timber.d("Location Permission: %b", p));

        mListenDisposable = Observable.zip(fgObservable, permObservable, (fg, perm) -> fg && perm)
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
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
        Location location = result.getLastLocation();
        Timber.d("Last location result: %f,%f", location.getLatitude(), location.getLongitude());
        mLastLocationSubject.onNext(location);
    }

    private void listenLocationUpdates(boolean listen) {
        RxUtils.assertMainThread();
        if (!checkPermission()) {
            return;
        }

        if (listen) {
            Timber.d("Start to listen location updates");
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, this, null);
        }
        else {
            Timber.d("Stop listening location updates");
            mFusedLocationClient.removeLocationUpdates(this);
        }
    }

    public Observable<Boolean> observePermission() {
        return Observable.fromCallable(this::checkPermission);
    }
}
