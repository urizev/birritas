package com.urizev.birritas.app.rx;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import timber.log.Timber;

@Singleton
public class RxForeground {
    private static final long DEBOUNCE_MILLIS = 300;

    private final BehaviorSubject<Boolean> foregroundSubject;
    @Inject
    RxForeground() {
        this.foregroundSubject = BehaviorSubject.createDefault(false);
    }

    Observable<Boolean> observe() {
        return foregroundSubject
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .debounce(DEBOUNCE_MILLIS, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .doOnNext(fg -> Timber.d("Broadcasting foreground: %b", fg));
    }

    public void update(boolean fg) {
        Timber.d("Received foreground: %b", fg);
        foregroundSubject.onNext(fg);
    }
}
