package com.urizev.birritas.app.rx;

import android.os.Looper;

import com.google.common.base.Preconditions;

public class RxUtils {
    private static final String PREFFIX_IO = "RxCachedThreadScheduler";
    private static final String PREFFIX_COMPUTATION = "RxComputationThreadPool";

    public void assertMainThread() {
        Preconditions.checkState(Looper.getMainLooper() == Looper.myLooper());
    }

    public void assertNonMainThread() {
        Preconditions.checkState(Looper.getMainLooper() != Looper.myLooper());
    }

    public void assertIOThread() {
        Preconditions.checkState(Thread.currentThread().getName().startsWith(PREFFIX_IO));
    }

    public void assertComputationThread() {
        Preconditions.checkState(Thread.currentThread().getName().startsWith(PREFFIX_COMPUTATION));
    }

    public static Interceptor interceptor() {
        return chain -> {
            return chain.proceed(chain.request());
        };
    }
}
