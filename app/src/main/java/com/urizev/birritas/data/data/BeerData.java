package com.urizev.birritas.data.data;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.urizev.birritas.data.data.AutoValue_BeerData;

@AutoValue
public abstract class BeerData {
    public abstract String id();
    public abstract String name();
    @Nullable
    public abstract String description();
    @Nullable
    public abstract String abv();
    public abstract String isOrganic();
    public abstract String status();
    @Nullable
    public abstract SRMData srm();
    @Nullable
    public abstract GlassData glass();
    public abstract StyleData style();
    @Nullable
    public abstract ImageSetData labels();

    public static JsonAdapter<BeerData> jsonAdapter(Moshi moshi) {
        return new AutoValue_BeerData.MoshiJsonAdapter(moshi);
    }

}
