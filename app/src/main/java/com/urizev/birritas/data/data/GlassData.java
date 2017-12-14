package com.urizev.birritas.data.data;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.urizev.birritas.data.data.AutoValue_GlassData;

@AutoValue
public abstract class GlassData {
    public abstract int id();
    public abstract String name();

    public static JsonAdapter<GlassData> jsonAdapter(Moshi moshi) {
        return new AutoValue_GlassData.MoshiJsonAdapter(moshi);
    }
}
