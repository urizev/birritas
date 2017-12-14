package com.urizev.birritas.api;

import android.support.annotation.NonNull;

import com.urizev.birritas.BuildConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

class ApiKeyInterceptor implements Interceptor {
    private static final String PARAM_NAME = "key";

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request original = chain.request();

        return chain.proceed(original.newBuilder()
                .url(original.url()
                        .newBuilder()
                        .addQueryParameter(PARAM_NAME, BuildConfig.BREWERYDB_API_KEY)
                        .build())
                .build());
    }
}
