package com.urizev.birritas.data.data;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.urizev.birritas.data.data.AutoValue_StyleData;

@AutoValue
public abstract class StyleData {
    public abstract int id();
    public abstract String name();
    public abstract String shortName();
    public abstract String description();
    public abstract CategoryData category();

    public static JsonAdapter<StyleData> jsonAdapter(Moshi moshi) {
        return new AutoValue_StyleData.MoshiJsonAdapter(moshi);
    }
}
