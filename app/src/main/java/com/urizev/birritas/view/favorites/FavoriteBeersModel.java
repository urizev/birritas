package com.urizev.birritas.view.favorites;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.urizev.birritas.domain.entities.Beer;

@AutoValue
public abstract class FavoriteBeersModel {
    public abstract ImmutableList<Beer> beers();
    public abstract boolean loading();

    FavoriteBeersModel withBeers(ImmutableList<Beer> beers) {
        return FavoriteBeersModel.builder()
                .beers(beers)
                .loading(false)
                .build();
    }

    public static Builder builder () {
        return new AutoValue_FavoriteBeersModel.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder beers(ImmutableList<Beer> beers);
        public abstract Builder loading(boolean loading);

        public abstract FavoriteBeersModel build();
    }
}
