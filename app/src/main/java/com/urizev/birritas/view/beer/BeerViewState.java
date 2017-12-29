package com.urizev.birritas.view.beer;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.urizev.birritas.view.common.ViewState;

@AutoValue
public abstract class BeerViewState implements ViewState {
    public abstract String name();
    @Nullable
    public abstract String imageUrl();
    public abstract String brewedBy();
    public abstract String style();
    public abstract String srm();
    public abstract String ibu();
    public abstract String abv();
    public abstract boolean favorite();

    public static BeerViewState create(String name,
                                       String imageUrl,
                                       String brewedBy,
                                       String style,
                                       String srm,
                                       String ibu,
                                       String abv, boolean favorite) {
        return new AutoValue_BeerViewState(name, imageUrl, brewedBy, style, srm, ibu, abv, favorite);
    }
}
