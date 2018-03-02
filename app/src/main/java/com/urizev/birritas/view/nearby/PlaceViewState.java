package com.urizev.birritas.view.nearby;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class PlaceViewState {
    public abstract String id();
    public abstract String name();
    @Nullable
    public abstract String imageUrl();
    @Nullable
    public abstract String address();
    public abstract double latitude();
    public abstract double longitude();

    public static PlaceViewState create(String id, String name, String imageUrl, String address, double latitude, double longitude) {
        return new AutoValue_PlaceViewState(id, name, imageUrl, address, latitude, longitude);
    }
}
