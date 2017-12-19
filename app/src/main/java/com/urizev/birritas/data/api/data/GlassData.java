package com.urizev.birritas.data.api.data;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class GlassData {
    public abstract int id();
    public abstract String name();

    public static JsonAdapter<GlassData> jsonAdapter(Moshi moshi) {
        return new AutoValue_GlassData.MoshiJsonAdapter(moshi);
    }
}
