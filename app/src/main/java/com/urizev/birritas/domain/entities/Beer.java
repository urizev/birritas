package com.urizev.birritas.domain.entities;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

@AutoValue
public abstract class Beer {
    public abstract String id();
    public abstract String name();
    @Nullable
    public abstract String description();
    @Nullable
    public abstract Float abv();
    @Nullable
    public abstract Float ibu();
    public abstract boolean isOrganic();
    public abstract int status();
    @Nullable
    public abstract SRM srm();
    @Nullable
    public abstract Glass glass();
    @Nullable
    public abstract Style style();
    @Nullable
    public abstract ImageSet labels();
    @Nullable
    public abstract ImmutableList<Brewery> breweries();
    public abstract Ingredients ingredients();

    public static Builder builder() {
        return new AutoValue_Beer.Builder();
    }

    public Builder toBuilder() {
        return Beer.builder()
                .id(id())
                .name(name())
                .description(description())
                .abv(abv())
                .ibu(ibu())
                .isOrganic(isOrganic())
                .status(status())
                .srm(srm())
                .glass(glass())
                .style(style())
                .labels(labels())
                .ingredients(ingredients())
                .breweries(breweries());
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder id(String id);
        public abstract Builder name(String name);
        public abstract Builder description(String description);
        public abstract Builder abv(Float abv);
        public abstract Builder ibu(Float ibu);
        public abstract Builder isOrganic(boolean isOrganic);
        public abstract Builder status(int status);
        public abstract Builder srm(SRM srm);
        public abstract Builder glass(Glass glass);
        public abstract Builder style(Style style);
        public abstract Builder labels(ImageSet labels);
        public abstract Builder breweries(ImmutableList<Brewery> breweries);
        public abstract Builder ingredients(Ingredients ingredients);

        public abstract Beer build();
    }
}
