package com.urizev.birritas.data.api.data;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class StyleData {
    public abstract int id();
    public abstract String name();
    public abstract String shortName();
    @Nullable
    public abstract String description();
    public abstract CategoryData category();

    public static JsonAdapter<StyleData> jsonAdapter(Moshi moshi) {
        return new AutoValue_StyleData.MoshiJsonAdapter(moshi);
    }
}
