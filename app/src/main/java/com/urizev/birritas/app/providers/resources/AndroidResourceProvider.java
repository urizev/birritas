package com.urizev.birritas.app.providers.resources;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import javax.inject.Inject;

public class AndroidResourceProvider implements ResourceProvider {
    private final Context mContext;

    @Inject
    AndroidResourceProvider(Context context) {
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
