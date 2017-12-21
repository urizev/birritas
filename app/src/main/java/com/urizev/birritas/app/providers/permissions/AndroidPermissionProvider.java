package com.urizev.birritas.app.providers.permissions;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.urizev.birritas.app.base.BaseActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

@Singleton
public class AndroidPermissionProvider {
    private final Context mContext;
    private final ArrayMap<String,BehaviorSubject<Boolean>> subjects;

    @Inject
    AndroidPermissionProvider(@NonNull Context context) {
        this.mContext = context;
        subjects = new ArrayMap<>();
    }

    public Observable<Boolean> permission(@NonNull String permission) {
        return getSubject(permission);
    }

    public Observable<Boolean> request(String permission, BaseActivity activity) {
        BehaviorSubject<Boolean> subject = getSubject(permission);
        RxPermissions rx = new RxPermissions(activity);
        return rx.request(permission).doOnNext(subject::onNext).subscribeOn(Schedulers.computation());
    }

    private BehaviorSubject<Boolean> getSubject(String permission) {
        BehaviorSubject<Boolean> subject = subjects.get(permission);
        if (subject == null) {
            int ret = ContextCompat.checkSelfPermission(mContext, permission);
            boolean granted = ret == PackageManager.PERMISSION_GRANTED;
            subject = BehaviorSubject.createDefault(granted);
            subjects.put(permission, subject);
        }
        return subject;
    }
}
