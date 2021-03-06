package com.urizev.birritas.domain.entities;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

@AutoValue
public abstract class Brewery {
    public abstract String id();
    public abstract String name();
    @Nullable
    public abstract String shortName();
    @Nullable
    public abstract String description();
    @Nullable
    public abstract String website();
    @Nullable
    public abstract Integer established();
    @Nullable
    public abstract ImageSet images();
    public abstract boolean isOrganic();
    public abstract int status();
    public abstract ImmutableList<Place> locations();
    public abstract ImmutableList<Beer> beers();

    public static Builder builder() {
        return new AutoValue_Brewery.Builder().beers(ImmutableList.of());
    }

    public Brewery.Builder toBuilder() {
        return Brewery.builder()
                .id(id())
                .name(name())
                .description(description())
                .shortName(shortName())
                .website(website())
                .isOrganic(isOrganic())
                .status(status())
                .established(established())
                .locations(locations())
                .beers(beers())
                .images(images());
    }

    public Place getMainPlace() {
        for (Place place : locations()) {
            if (place.isPrimary()) {
                return place;
            }
        }

        if (!locations().isEmpty()) {
            return locations().get(0);
        }

        return null;
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder id(String id);
        public abstract Builder name(String name);
        public abstract Builder shortName(String shortName);
        public abstract Builder description(String description);
        public abstract Builder website(String website);
        public abstract Builder established(Integer establised);
        public abstract Builder images(ImageSet images);
        public abstract Builder isOrganic(boolean organic);
        public abstract Builder status(int status);
        public abstract Builder locations(ImmutableList<Place> locations);
        public abstract Builder beers(ImmutableList<Beer> beers);

        public abstract Brewery build();
    }
}
