package com.urizev.birritas.view.nearby;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.urizev.birritas.domain.entities.Coordinate;

@AutoValue
public abstract class PlaceViewState {
    public abstract String id();
    public abstract String name();
    @Nullable
    public abstract String imageUrl();
    @Nullable
    public abstract String address();
    public abstract Coordinate coordinate();

    public static PlaceViewState create(String id, String name, String imageUrl, String address, Coordinate coordinate) {
        return new AutoValue_PlaceViewState(id, name, imageUrl, address, coordinate);
    }
}
