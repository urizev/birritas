package com.urizev.birritas.data.api.data;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class IngredientsData {
    @Nullable
    public abstract ImmutableList<HopData> hops();
    @Nullable
    public abstract ImmutableList<YeastData> yeast();
    @Nullable
    public abstract ImmutableList<MaltData> malt();

    public static JsonAdapter<IngredientsData> jsonAdapter(Moshi moshi) {
        return new AutoValue_IngredientsData.MoshiJsonAdapter(moshi);
    }
}
