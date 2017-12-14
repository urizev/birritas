package com.urizev.birritas.data.data;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.urizev.birritas.data.data.AutoValue_GeopointData;

@AutoValue
public abstract class GeopointData {
    public abstract String id();
    public abstract String name();
    @Nullable
    public abstract String streetAddress();
    public abstract String locality();
    @Nullable
    public abstract String postalCode();
    @Nullable
    public abstract String phone();
    public abstract double latitude();
    public abstract double longitude();
    public abstract String status();
    public abstract BreweryData brewery();

    public static JsonAdapter<GeopointData> jsonAdapter(Moshi moshi) {
        return new AutoValue_GeopointData.MoshiJsonAdapter(moshi);
    }

}
