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
    @Nullable
    public abstract Throwable error();
    public abstract ImmutableList<VS> viewStates();
    @Nullable
    public abstract PlaceViewState selectedPlaceViewState();
    public abstract boolean requestPermission();

    public static <VS> NearbyViewState<VS> create(Coordinate coordinate, boolean shouldMove, Throwable error, ImmutableList<VS> viewStates, PlaceViewState selectedViewState, boolean requestPermission) {
        return new AutoValue_NearbyViewState<VS>(coordinate, shouldMove, error, viewStates, selectedViewState, requestPermission);
    }
}
