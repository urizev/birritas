package com.urizev.birritas.app.rx;

import android.os.Looper;

import com.google.common.base.Preconditions;

import okhttp3.Interceptor;

public class RxUtils {
    private static final String PREFIX_IO = "RxCachedThreadScheduler";
    private static final String PREFIX_COMPUTATION = "RxComputationThreadPool";

    private RxUtils() {}

    public static void assertMainThread() {
        Preconditions.checkState(Looper.getMainLooper() == Looper.myLooper());
    }

    public static void assertNonMainThread() {
        Preconditions.checkState(Looper.getMainLooper() != Looper.myLooper());
    }

    public static void assertIOThread() {
        assertThreadPrefix(PREFIX_IO);
    }

    public static void assertComputationThread() {
        assertThreadPrefix(PREFIX_COMPUTATION);
    }

    private static void assertThreadPrefix(String prefix) {
        String name = Thread.currentThread().getName();
        Preconditions.checkState(name.startsWith(prefix), "Running on thread %s. Expected %s", name, prefix);
    }

    public static Interceptor interceptor() {
        return chain -> {
            RxUtils.assertIOThread();
            return chain.proceed(chain.request());
        };
    }
}
