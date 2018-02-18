package com.urizev.birritas.view.brewery;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.urizev.birritas.view.common.ViewState;

@AutoValue
abstract class BreweryBeerPresenterViewState implements ViewState {
    public abstract String id();
    public abstract String title();
    @Nullable
    public abstract String imageUrl();
    public abstract String style();
    public abstract String brewedBy();
    public abstract int srmColor();
    public abstract String srmValue();
    public abstract String ibuValue();
    public abstract String abvValue();

    public static BreweryBeerPresenterViewState create(String id, String title, String imageUrl, String style, String brewedBy, int srmColor, String srmValue, String ibuValue, String abvValue) {
        return new AutoValue_BreweryBeerPresenterViewState(id, title, imageUrl, style, brewedBy, srmColor, srmValue, ibuValue, abvValue);
    }
}
