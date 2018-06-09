package com.urizev.birritas.view.brewery;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.urizev.birritas.domain.entities.Coordinate;
import com.urizev.birritas.view.common.ViewState;

@AutoValue
public abstract class BreweryPresenterViewState implements ViewState {
    public abstract boolean loading();
    @Nullable
    public abstract Throwable throwable();

    public abstract String name();
    @Nullable
    public abstract String imageUrl();
    @Nullable
    public abstract String established();
    @Nullable
    public abstract Coordinate coordinate();
    @Nullable
    public abstract String address();
    @Nullable
    public abstract String description();
    public abstract ImmutableList<BreweryBeerPresenterViewState> beers();

    public static BreweryPresenterViewState create(boolean loading, Throwable throwable, String name, String imageUrl, String established, Coordinate coordinate, String address, String description, ImmutableList<BreweryBeerPresenterViewState> beers) {
        return new AutoValue_BreweryPresenterViewState(loading, throwable, name, imageUrl, established, coordinate, address, description, beers);
    }
}
