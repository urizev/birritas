package com.urizev.birritas.view.brewery;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.urizev.birritas.domain.entities.Brewery;

@AutoValue
public abstract class BreweryModel {
    @Nullable
    public abstract Brewery brewery();
    @Nullable
    public abstract Throwable throwable();
    public abstract boolean loading();

    public static Builder builder() {
        return new AutoValue_BreweryModel.Builder();
    }

    BreweryModel withLoading(boolean loading) {
        return builder()
                .loading (loading)
                .brewery(brewery())
                .throwable(null)
                .build();
    }

    BreweryModel withBrewery(Brewery brewery) {
        return builder()
                .loading (false)
                .brewery(brewery)
                .throwable(null)
                .build();
    }

    public BreweryModel withThrowable(Throwable throwable) {
        return builder()
                .loading (false)
                .throwable(throwable)
                .brewery(brewery())
                .build();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder brewery(Brewery brewery);
        public abstract Builder throwable(Throwable throwable);
        public abstract Builder loading(boolean loading);

        public abstract BreweryModel build();
    }
}
