package com.urizev.birritas.view.beer;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.urizev.birritas.domain.entities.Beer;

@AutoValue
public abstract class BeerModel {
    @Nullable
    public abstract Beer beer();
    @Nullable
    public abstract Throwable throwable();
    public abstract boolean loading();

    public static Builder builder() {
        return new AutoValue_BeerModel.Builder();
    }

    BeerModel withLoading(boolean loading) {
        return builder()
                .loading(loading)
                .beer(beer())
                .throwable(null)
                .build();
    }

    BeerModel withBeer(Beer beer) {
        return builder()
                .loading(false)
                .beer(beer)
                .throwable(null)
                .build();
    }

    BeerModel withThrowable(Throwable throwable) {
        return builder()
                .loading(false)
                .throwable(throwable)
                .beer(beer())
                .build();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder beer(Beer beer);
        public abstract Builder throwable(Throwable throwable);
        public abstract Builder loading (boolean loading);

        public abstract BeerModel build();
    }
}
