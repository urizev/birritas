package com.urizev.birritas.view.favorites;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.urizev.birritas.view.common.ViewState;

@AutoValue
public abstract class FavoriteBeersViewState implements ViewState{
    public abstract ImmutableList<FavoriteBeersItemViewState> viewStates();
    public abstract boolean loading();

    public static FavoriteBeersViewState create(ImmutableList<FavoriteBeersItemViewState> viewStates, boolean loading) {
        return new AutoValue_FavoriteBeersViewState(viewStates, loading);
    }
}
