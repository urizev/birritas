package com.urizev.birritas.view.nearby;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.urizev.birritas.domain.entities.Place;

@AutoValue
public abstract class NearbyModel {
    public abstract ImmutableSet<Place> places();
    public abstract boolean loading();
    @Nullable
    public abstract Throwable error();

    NearbyModel mergePlaces(ImmutableList<Place> places) {
        ImmutableSet.Builder<Place> builder = new ImmutableSet.Builder<>();
        builder = builder.addAll(places());
        builder = builder.addAll(places);

        return this.toBuilder()
                .places(builder.build())
                .error(null)
                .loading(false)
                .build();
    }

    private Builder toBuilder() {
        return NearbyModel.builder()
                .error(error())
                .loading(loading())
                .places(places());
    }

    public static Builder builder() {
        return new AutoValue_NearbyModel.Builder();
    }

    public NearbyModel withError(Throwable throwable) {
        return toBuilder().error(throwable).build();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder places(ImmutableSet<Place> places);
        public abstract Builder error(Throwable error);
        public abstract Builder loading(boolean loading);

        public abstract NearbyModel build();
    }
}
