package com.urizev.birritas.view.beer;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.urizev.birritas.view.common.ViewState;

@AutoValue
public abstract class BeerViewState implements ViewState {
    public abstract String name();

    @Nullable
    public abstract String imageUrl();

    public abstract String abv();

    public abstract String ibu();

    public abstract String srm();

    public abstract int srmColor();

    public abstract ImmutableList<ViewState> mainViewStates();
    public abstract ImmutableList<ViewState> sideViewStates();

    public abstract boolean favorite();

    public static BeerViewState create(String name,
                                       String imageUrl,
                                       String abv,
                                       String ibu,
                                       String srm,
                                       int srmColor,
                                       ImmutableList<ViewState> mainViewStates,
                                       ImmutableList<ViewState> sideViewStates,
                                       boolean favorite) {
        return new AutoValue_BeerViewState(name, imageUrl, abv, ibu, srm, srmColor, mainViewStates, sideViewStates, favorite);
    }
}
