package com.urizev.birritas.app.rx;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class RxMap {

    public static Observable<LatLngBounds> cameraIdleBounds(GoogleMap map) {
        return Observable.create(e -> {
            GoogleMap.OnCameraIdleListener listener = () -> {
                if (!e.isDisposed()) {
                    e.onNext(map.getProjection().getVisibleRegion().latLngBounds);
                }
            };

            map.setOnCameraIdleListener(listener);

            e.setDisposable(new Disposable() {
                boolean mDisposed;

                @Override
                public void dispose() {
                    mDisposed = true;
                    map.setOnCameraIdleListener(null);
                }

                @Override
                public boolean isDisposed() {
                    return mDisposed;
                }
            });
        });
    }

    public static Observable<Boolean> mapIsIdle(GoogleMap map) {
        return Observable.<Boolean>create(e -> {
            GoogleMap.OnCameraIdleListener idleListener = () -> {
                if (e.isDisposed()) {
                    return;
                }
                e.onNext(true);
            };

            GoogleMap.OnCameraMoveStartedListener moveListener = i -> {
                if (e.isDisposed()) {
                    return;
                }

                e.onNext(false);
            };

            e.setDisposable(new SimpleDisposable() {
                @Override
                protected void onDisposed() {
                    map.setOnCameraMoveStartedListener(null);
                    map.setOnCameraIdleListener(null);
                }
            });

            map.setOnCameraIdleListener(idleListener);
            map.setOnCameraMoveStartedListener(moveListener);
        }).unsubscribeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Marker> markerClicks(GoogleMap map) {
        return Observable.<Marker>create(e -> {
            GoogleMap.OnMarkerClickListener listener = marker -> {
                if (e.isDisposed()) {
                    return false;
                }

                e.onNext(marker);

                return true;
            };

            e.setDisposable(new SimpleDisposable() {
                @Override
                protected void onDisposed() {
                    map.setOnMarkerClickListener(null);
                }
            });
            map.setOnMarkerClickListener(listener);
        }).unsubscribeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<LatLng> mapClicks(GoogleMap map) {
        return Observable.<LatLng>create(e -> {
            GoogleMap.OnMapClickListener listener = latLng -> {
                if (e.isDisposed()) {
                    return;
                }

                e.onNext(latLng);
            };

            e.setDisposable(new SimpleDisposable() {
                @Override
                protected void onDisposed() {
                    map.setOnMapClickListener(null);
                }
            });
            map.setOnMapClickListener(listener);
        }).unsubscribeOn(AndroidSchedulers.mainThread());
    }
}
