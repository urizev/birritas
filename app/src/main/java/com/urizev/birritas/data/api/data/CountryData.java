package com.urizev.birritas.data.api.data;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class CountryData {
    public abstract String isoCode();
    public abstract String name();
    public abstract String displayName();
    public abstract String isoThree();
    public abstract int numberCode();

    public static JsonAdapter<CountryData> jsonAdapter(Moshi moshi) {
        return new AutoValue_CountryData.MoshiJsonAdapter(moshi);
    }
}
