package com.urizev.birritas.app.rx;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLngBounds;

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
}
