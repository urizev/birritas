package com.urizev.birritas.data.data;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.urizev.birritas.data.data.AutoValue_SRMData;

@AutoValue
public abstract class SRMData {
    public abstract int id();
    public abstract int name();
    public abstract String hex();

    public static JsonAdapter<SRMData> jsonAdapter(Moshi moshi) {
        return new AutoValue_SRMData.MoshiJsonAdapter(moshi);
    }
}
