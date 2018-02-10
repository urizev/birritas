package com.urizev.birritas.view.brewery;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.urizev.birritas.domain.entities.Brewery;

@AutoValue
public abstract class BreweryModel {
    @Nullable
    public abstract Brewery brewery();

    public static Builder builder() {
        return new AutoValue_BreweryModel.Builder();
    }

    BreweryModel withBrewery(Brewery brewery) {
        return builder()
                .brewery(brewery)
                .build();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder brewery(Brewery brewery);

        public abstract BreweryModel build();
    }
}
