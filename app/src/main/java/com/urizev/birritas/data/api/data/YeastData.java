package com.urizev.birritas.data.api.data;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class YeastData {
    public abstract int id();
    public abstract String name();
    public abstract String description();

    public static JsonAdapter<YeastData> jsonAdapter(Moshi moshi) {
        return new AutoValue_YeastData.MoshiJsonAdapter(moshi);
    }
}
