package com.urizev.birritas.data.api.data;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class MaltData {
    public abstract int id();
    public abstract String name();
    @Nullable
    public abstract String description();

    public static JsonAdapter<MaltData> jsonAdapter(Moshi moshi) {
        return new AutoValue_MaltData.MoshiJsonAdapter(moshi);
    }
}
