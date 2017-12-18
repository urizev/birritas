package com.urizev.birritas.view.nearby;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.urizev.birritas.view.common.ViewState;

@AutoValue
public abstract class NearbyViewState<VS> implements ViewState {
    public abstract boolean loading();
    public abstract Throwable error();
    public abstract ImmutableList<VS> viewStates();

    public static <VS> NearbyViewState<VS> create(boolean loading, Throwable error, ImmutableList<VS> viewStates) {
        return new AutoValue_NearbyViewState<VS>(loading, error, viewStates);
    }
}
