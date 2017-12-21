package com.urizev.birritas.data.api.data;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class PlaceData {
    public abstract String id();
    public abstract String name();
    @Nullable
    public abstract String streetAddress();
    @Nullable
    public abstract String extendedAddress();
    @Nullable
    public abstract String locality();
    @Nullable
    public abstract String postalCode();
    @Nullable
    public abstract String region();
    @Nullable
    public abstract String phone();
    @Nullable
    public abstract String website();
    public abstract double latitude();
    public abstract double longitude();
    public abstract String isPrimary();
    @Nullable
    public abstract String isPlanning();
    public abstract String isClosed();
    public abstract String openToPublic();
    public abstract String locationType();
    public abstract String countryIsoCode();
    @Nullable
    public abstract String yearOpened();
    public abstract String status();
    public abstract CountryData country();
    @Nullable
    public abstract BreweryData brewery();

    public static JsonAdapter<PlaceData> jsonAdapter(Moshi moshi) {
        return new AutoValue_PlaceData.MoshiJsonAdapter(moshi);
    }

}
