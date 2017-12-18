package com.urizev.birritas.view.nearby;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class PlaceViewState {
    public abstract String id();
    public abstract String name();
    public abstract double latitude();
    public abstract double longitude();

    public static PlaceViewState create(String id, String name, double latitude, double longitude) {
        return new AutoValue_PlaceViewState(id, name, latitude, longitude);
    }
}
