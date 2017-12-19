package com.urizev.birritas.data.api.data;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class BreweryData {
    public abstract String id();
    public abstract String name();
    @Nullable
    public abstract String shortNameDisplay();
    @Nullable
    public abstract String description();
    @Nullable
    public abstract String website();
    public abstract String isOrganic();
    @Nullable
    public abstract ImageSetData images();
    @Nullable
    public abstract String established();
    public abstract String status();
    @Nullable
    public abstract ImmutableList<PlaceData> locations();

    public static JsonAdapter<BreweryData> jsonAdapter(Moshi moshi) {
        return new AutoValue_BreweryData.MoshiJsonAdapter(moshi);
    }
}
