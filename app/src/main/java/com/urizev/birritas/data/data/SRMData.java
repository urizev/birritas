package com.urizev.birritas.data.data;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class SRMData {
    public abstract int id();
    public abstract String name();
    public abstract String hex();

    public static JsonAdapter<SRMData> jsonAdapter(Moshi moshi) {
        return new AutoValue_SRMData.MoshiJsonAdapter(moshi);
    }
}
