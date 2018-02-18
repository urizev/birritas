package com.urizev.birritas.view.brewery;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.urizev.birritas.domain.entities.Coordinate;
import com.urizev.birritas.view.common.ViewState;

@AutoValue
public abstract class BreweryViewState implements ViewState {
    public abstract String name();
    @Nullable
    public abstract String imageUrl();
    @Nullable
    public abstract String established();
    @Nullable
    public abstract Coordinate coordinate();
    @Nullable
    public abstract String address();

    public abstract ImmutableList<ViewState> mainViewStates();

    public static BreweryViewState create(String name,
                                          String imageUrl,
                                          String established,
                                          Coordinate coordinate,
                                          String address,
                                          ImmutableList<ViewState> mainViewStates) {
        return new AutoValue_BreweryViewState(name, imageUrl, established, coordinate, address, mainViewStates);
    }
}
