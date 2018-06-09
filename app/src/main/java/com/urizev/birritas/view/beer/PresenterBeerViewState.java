package com.urizev.birritas.view.beer;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.urizev.birritas.view.common.ViewState;

@AutoValue
public abstract class PresenterBeerViewState implements ViewState {
    public abstract boolean loading();
    @Nullable
    public abstract Throwable throwable();

    public abstract String name();
    @Nullable
    public abstract String imageUrl();
    public abstract String brewedBy();
    @Nullable
    public abstract String brewedById();
    public abstract String style();
    public abstract String srm();
    public abstract int srmColor();
    public abstract String ibu();
    public abstract String abv();
    public abstract ImmutableList<String> ingredients();
    public abstract boolean favorite();

    public static PresenterBeerViewState create(boolean loading,
                                                Throwable throwable,
                                                String name,
                                                String imageUrl,
                                                String brewedBy,
                                                String brewedById,
                                                String style,
                                                String srm,
                                                int srmColor,
                                                String ibu,
                                                String abv,
                                                ImmutableList<String> ingredients,
                                                boolean favorite) {
        return new AutoValue_PresenterBeerViewState(loading, throwable, name, imageUrl, brewedBy, brewedById, style, srm, srmColor, ibu, abv, ingredients, favorite);
    }
}
