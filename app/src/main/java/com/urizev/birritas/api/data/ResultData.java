package com.urizev.birritas.api.data;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.lang.reflect.Type;

@AutoValue
public abstract class ResultData<T> {
    public abstract String status();
    public abstract T data();
    @Nullable
    public abstract Integer currentPage();
    @Nullable
    public abstract Integer numberOfPages();
    @Nullable
    public abstract Integer totalResults();

    public static <T> JsonAdapter<ResultData<T>> jsonAdapter(Moshi moshi, Type[] types) {
        //noinspection unchecked
        return new AutoValue_ResultData.MoshiJsonAdapter(moshi, types);
    }
}
