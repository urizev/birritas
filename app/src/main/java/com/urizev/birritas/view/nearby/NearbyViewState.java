package com.urizev.birritas.view.nearby;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.urizev.birritas.domain.entities.Coordinate;
import com.urizev.birritas.view.common.ViewState;

@AutoValue
public abstract class NearbyViewState<VS> implements ViewState {
    @Nullable
    public abstract Coordinate coordinate();
    public abstract boolean shouldMove();
    public abstract boolean loading();
    @Nullable
    public abstract Throwable throwable();
    public abstract ImmutableList<VS> viewStates();
    @Nullable
    public abstract PlaceViewState selectedPlaceViewState();
    public abstract boolean requestLocationPermission();
    public abstract boolean hasLocationPermission();

    public static <VS> NearbyViewState<VS> create(Coordinate coordinate, boolean shouldMove, boolean loading, Throwable throwable, ImmutableList<VS> viewStates, PlaceViewState selectedViewState, boolean requestLocationPermission, boolean hasLocationPermission) {
        return new AutoValue_NearbyViewState<>(coordinate, shouldMove, loading, throwable, viewStates, selectedViewState, requestLocationPermission, hasLocationPermission);
    }
}
