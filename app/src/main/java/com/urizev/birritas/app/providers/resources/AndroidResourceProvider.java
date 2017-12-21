package com.urizev.birritas.app.providers.resources;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.annotations.NonNull;

@Singleton
public class AndroidResourceProvider implements ResourceProvider {
    private final Context mContext;

    @Inject
    AndroidResourceProvider(@NonNull Context context) {
        this.mContext = context;
    }

    @Override
    public String getString(int res) {
        return mContext.getResources().getString(res);
    }

    @Override
    public int getColor(int res) {
        return ContextCompat.getColor(mContext, res);
    }
}
