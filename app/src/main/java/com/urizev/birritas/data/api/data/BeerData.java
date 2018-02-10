package com.urizev.birritas.data.api.data;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class BeerData {
    public abstract String id();
    public abstract String name();
    @Nullable
    public abstract String description();
    @Nullable
    public abstract String abv();
    @Nullable
    public abstract String ibu();
    public abstract String isOrganic();
    public abstract String status();
    @Nullable
    public abstract SRMData srm();
    @Nullable
    public abstract GlassData glass();
    @Nullable
    public abstract StyleData style();
    @Nullable
    public abstract ImageSetData labels();
    @Nullable
    public abstract ImmutableList<BreweryData> breweries();
    @Nullable
    public abstract IngredientsData ingredients();

    public static JsonAdapter<BeerData> jsonAdapter(Moshi moshi) {
        return new AutoValue_BeerData.MoshiJsonAdapter(moshi);
    }

}
