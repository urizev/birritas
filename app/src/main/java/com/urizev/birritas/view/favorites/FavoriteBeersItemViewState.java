package com.urizev.birritas.view.favorites;


import com.google.auto.value.AutoValue;
import com.urizev.birritas.view.common.ViewState;

@AutoValue
public abstract class FavoriteBeersItemViewState implements ViewState {
    public abstract String id();
    public abstract String title();
    public abstract String imageUrl();
    public abstract String style();
    public abstract String brewedBy();
    public abstract String ibuValue();
    public abstract String abvValue();

    public static FavoriteBeersItemViewState create(String id, String title, String imageUrl, String style, String brewedBy, String ibuValue, String abvValue) {
        return new AutoValue_FavoriteBeersItemViewState(id, title, imageUrl, style, brewedBy, ibuValue, abvValue);
    }
}
