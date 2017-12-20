package com.urizev.birritas.app.rx;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;

public class RxReceiver {
    private RxReceiver () {}

    public static Observable<Intent> register(@NonNull Context context, @NonNull IntentFilter filter) {
        return Observable.create(e -> {
            BroadcastReceiver receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (!e.isDisposed()) {
                        e.onNext(intent);
                    }
                }
            };

            e.setDisposable(new SimpleDisposable() {
                @Override
                protected void onDisposed() {
                    context.unregisterReceiver(receiver);
                }
            });

            context.registerReceiver(receiver, filter);
        });
    }

    public static Observable<Intent> register(@NonNull Context context, @NonNull String action) {
        return register(context, new IntentFilter(action))
                .filter(i -> action.equals(i.getAction()));
    }
}
