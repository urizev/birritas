package com.urizev.birritas.api.data;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class BreweryData {
    public abstract String id();
    public abstract String name();
    @Nullable
    public abstract String website();
    @Nullable
    public abstract ImageSetData images();
    @Nullable
    public abstract String established();
    public abstract String status();

    public static JsonAdapter<BreweryData> jsonAdapter(Moshi moshi) {
        return new AutoValue_BreweryData.MoshiJsonAdapter(moshi);
    }

}
