package com.urizev.birritas.view.favorites;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.urizev.birritas.view.common.ViewState;

@AutoValue
public abstract class FavoriteBeersViewState implements ViewState{
    public abstract ImmutableList<FavoriteBeersItemViewState> viewStates();
    public abstract boolean loading();
    @Nullable
    public abstract Throwable error();

    public static FavoriteBeersViewState create(ImmutableList<FavoriteBeersItemViewState> viewStates, boolean loading, Throwable error) {
        return new AutoValue_FavoriteBeersViewState(viewStates, loading, error);
    }
}
