package com.urizev.birritas.app.rx;

import android.os.Looper;

import com.google.common.base.Preconditions;

import okhttp3.Interceptor;

public class RxUtils {
    private static final String PREFFIX_IO = "RxCachedThreadScheduler";
    private static final String PREFFIX_COMPUTATION = "RxComputationThreadPool";

    public static void assertMainThread() {
        Preconditions.checkState(Looper.getMainLooper() == Looper.myLooper());
    }

    public static void assertNonMainThread() {
        Preconditions.checkState(Looper.getMainLooper() != Looper.myLooper());
    }

    public static void assertIOThread() {
        Preconditions.checkState(Thread.currentThread().getName().startsWith(PREFFIX_IO));
    }

    public static void assertComputationThread() {
        Preconditions.checkState(Thread.currentThread().getName().startsWith(PREFFIX_COMPUTATION));
    }

    public static Interceptor interceptor() {
        return chain -> chain.proceed(chain.request());
    }
}
