package com.urizev.birritas.view.beer;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.urizev.birritas.domain.entities.Beer;

@AutoValue
public abstract class BeerModel {
    public abstract String id();
    @Nullable
    public abstract Beer beer();

    public static Builder builder(String beerId) {
        return new AutoValue_BeerModel.Builder().id(beerId);
    }

    private Builder toBuilder() {
        return builder(id())
                .beer(beer());
    }

    BeerModel withBeer(Beer beer) {
        return builder(id())
                .beer(beer)
                .build();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder id(String id);
        public abstract Builder beer(Beer beer);

        public abstract BeerModel build();
    }
}
