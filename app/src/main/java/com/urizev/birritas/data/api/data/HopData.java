package com.urizev.birritas.data.api.data;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class HopData {
    public abstract int id();
    public abstract String name();
    public abstract String description();
    public abstract CountryData country();

    public static JsonAdapter<HopData> jsonAdapter(Moshi moshi) {
        return new AutoValue_HopData.MoshiJsonAdapter(moshi);
    }
}
