package com.urizev.birritas.view.favorites;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.urizev.birritas.domain.entities.Beer;

@AutoValue
public abstract class FavoriteBeersModel {
    public abstract ImmutableList<Beer> beers();
    public abstract boolean loading();
    @Nullable
    public abstract Throwable throwable();

    FavoriteBeersModel withBeers(ImmutableList<Beer> beers) {
        return FavoriteBeersModel.builder()
                .throwable(null)
                .beers(beers)
                .loading(false)
                .build();
    }

    public static Builder builder () {
        return new AutoValue_FavoriteBeersModel.Builder();
    }

    public FavoriteBeersModel withThrowable(Throwable throwable) {
        return FavoriteBeersModel.builder()
                .throwable(throwable)
                .beers(beers())
                .loading(false)
                .build();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder beers(ImmutableList<Beer> beers);
        public abstract Builder loading(boolean loading);
        public abstract Builder throwable(Throwable throwable);

        public abstract FavoriteBeersModel build();
    }
}
