package com.urizev.birritas.data.api.data;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class HopData {
    public abstract int id();
    public abstract String name();
    @Nullable
    public abstract String description();
    @Nullable
    public abstract CountryData country();

    public static JsonAdapter<HopData> jsonAdapter(Moshi moshi) {
        return new AutoValue_HopData.MoshiJsonAdapter(moshi);
    }
}
