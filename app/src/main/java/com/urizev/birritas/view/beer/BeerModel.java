package com.urizev.birritas.view.beer;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.urizev.birritas.domain.entities.Beer;

@AutoValue
public abstract class BeerModel {
    @Nullable
    public abstract Beer beer();

    public static Builder builder() {
        return new AutoValue_BeerModel.Builder();
    }

    BeerModel withBeer(Beer beer) {
        return builder()
                .beer(beer)
                .build();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder beer(Beer beer);

        public abstract BeerModel build();
    }
}
