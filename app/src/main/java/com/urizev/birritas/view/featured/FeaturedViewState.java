package com.urizev.birritas.view.featured;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.urizev.birritas.view.common.ViewState;

@AutoValue
public abstract class FeaturedViewState implements ViewState {
    public abstract ImmutableList<FeaturedItemViewState> viewStates();
    @Nullable
    public abstract Throwable error();
    public abstract boolean loading();

    public static FeaturedViewState create(ImmutableList<FeaturedItemViewState> viewStates, boolean loading, Throwable error) {
        return new AutoValue_FeaturedViewState(viewStates, error, loading);
    }
}
