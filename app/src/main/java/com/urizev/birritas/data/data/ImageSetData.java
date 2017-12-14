package com.urizev.birritas.data.data;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.urizev.birritas.data.data.AutoValue_ImageSetData;

@AutoValue
public abstract class ImageSetData {
    public abstract String icon();
    public abstract String medium();
    public abstract String large();

    public static JsonAdapter<ImageSetData> jsonAdapter(Moshi moshi) {
        return new AutoValue_ImageSetData.MoshiJsonAdapter(moshi);
    }
}
