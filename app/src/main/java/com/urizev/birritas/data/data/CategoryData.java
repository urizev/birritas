package com.urizev.birritas.data.data;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.urizev.birritas.data.data.AutoValue_CategoryData;

@AutoValue
public abstract class CategoryData {
    public abstract int id();
    public abstract String name();

    public static JsonAdapter<CategoryData> jsonAdapter(Moshi moshi) {
        return new AutoValue_CategoryData.MoshiJsonAdapter(moshi);
    }
}
