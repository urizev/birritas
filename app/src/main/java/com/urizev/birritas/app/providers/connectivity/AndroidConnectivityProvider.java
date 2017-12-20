package com.urizev.birritas.app.providers.connectivity;

import android.content.Context;
import android.net.ConnectivityManager;

import com.urizev.birritas.app.rx.RxReceiver;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AndroidConnectivityProvider extends ConnectivityProvider {
    private Disposable mDisposable;

    public AndroidConnectivityProvider(Context context) {
        mDisposable = RxReceiver.register(context, ConnectivityManager.CONNECTIVITY_ACTION)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .map(i -> !i.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false))
                .doOnNext(subject::onNext)
                .subscribe();
    }

    public void dispose() {
        if (mDisposable != null) {
            mDisposable.dispose();
            mDisposable = null;
        }
    }
}
