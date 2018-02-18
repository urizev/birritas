package com.urizev.birritas.view.beer;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.urizev.birritas.view.common.ViewState;

@AutoValue
public abstract class BeerViewState implements ViewState {
    public abstract String name();

    public abstract String imageUrl();

    public abstract String abv();

    public abstract String ibu();

    public abstract String srm();

    public abstract int srmColor();

    public abstract ImmutableList<ViewState> mainViewStates();

    public static BeerViewState create(String name,
                                       String imageUrl,
                                       String abv,
                                       String ibu,
                                       String srm,
                                       int srmColor,
                                       ImmutableList<ViewState> mainViewStates) {
        return new AutoValue_BeerViewState(name, imageUrl, abv, ibu, srm, srmColor, mainViewStates);
    }
}
