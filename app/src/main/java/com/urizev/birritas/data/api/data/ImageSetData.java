package com.urizev.birritas.data.api.data;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class ImageSetData {
    public abstract String icon();
    public abstract String medium();
    public abstract String large();
    @Nullable
    public abstract String squareMedium();
    @Nullable
    public abstract String squareLarge();

    public static JsonAdapter<ImageSetData> jsonAdapter(Moshi moshi) {
        return new AutoValue_ImageSetData.MoshiJsonAdapter(moshi);
    }
}
