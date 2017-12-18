package com.urizev.birritas.app.rx;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLngBounds;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class RxMap {

    public static Observable<LatLngBounds> cameraIdle(GoogleMap map) {
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
}
