package com.urizev.birritas.view.featured;

import com.google.auto.value.AutoValue;
import com.urizev.birritas.view.common.ViewState;

@AutoValue
abstract class FeaturedItemViewState implements ViewState {
    public abstract String title();
    public abstract String imageUrl();
    public abstract String style();
    public abstract String brewedBy();
    public abstract int srmColor();
    public abstract String srmValue();
    public abstract String ibuValue();
    public abstract String abvValue();
    public abstract boolean favorite();

    public static FeaturedItemViewState create(String title, String imageUrl, String style, String brewedBy, int srmColor, String srmValue, String ibuValue, String abvValue, boolean favorite) {
        return new AutoValue_FeaturedItemViewState(title, imageUrl, style, brewedBy, srmColor, srmValue, ibuValue, abvValue, favorite);
    }
}
