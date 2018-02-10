package com.urizev.birritas.view.brewery;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.urizev.birritas.domain.entities.Coordinate;
import com.urizev.birritas.view.common.ViewState;

import javax.annotation.Nullable;

@AutoValue
public abstract class BreweryPresenterViewState implements ViewState {
    public abstract String name();
    public abstract String imageUrl();
    public abstract String established();
    @Nullable
    public abstract Coordinate coordinate();
    public abstract String address();
    public abstract String description();
    public abstract ImmutableList<BreweryBeerPresenterViewState> beers();

    public static BreweryPresenterViewState create(String name, String imageUrl, String established, Coordinate coordinate, String address, String description, ImmutableList<BreweryBeerPresenterViewState> beers) {
        return new AutoValue_BreweryPresenterViewState(name, imageUrl, established, coordinate, address, description, beers);
    }
}
