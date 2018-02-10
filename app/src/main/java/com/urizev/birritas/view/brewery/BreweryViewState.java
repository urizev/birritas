package com.urizev.birritas.view.brewery;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.urizev.birritas.domain.entities.Coordinate;
import com.urizev.birritas.view.common.ViewState;

@AutoValue
public abstract class BreweryViewState implements ViewState {
    public abstract String imageUrl();
    public abstract String established();
    public abstract Coordinate coordinate();
    public abstract String address();
    public abstract ImmutableList<ViewState> mainViewStates();

    public static BreweryViewState create(String imageUrl,
                                          String established,
                                          Coordinate coordinate,
                                          String address,
                                          ImmutableList<ViewState> mainViewStates) {
        return new AutoValue_BreweryViewState(imageUrl, established, coordinate, address, mainViewStates);
    }
}
