package com.urizev.birritas.view.widget;

import android.os.Bundle;

import com.google.auto.value.AutoValue;
import com.urizev.birritas.view.common.ViewState;

@AutoValue
public abstract class BeerListWidgetBeerViewState implements ViewState {
    public abstract String id();
    public abstract String name();
    public abstract String imageUrl();
    public abstract String brewedBy();
    public abstract String style();
    public abstract String abv();
    public abstract String ibu();

    public static BeerListWidgetBeerViewState create(String id, String name, String imageUrl, String brewedBy,
                                                     String style, String abv, String ibu) {
        return new AutoValue_BeerListWidgetBeerViewState(id, name, imageUrl, brewedBy, style, abv, ibu);
    }

    public static BeerListWidgetBeerViewState fromBundle(Bundle bundle) {
        return new AutoValue_BeerListWidgetBeerViewState(bundle.getString("id"), bundle.getString("name"), bundle.getString("image_url"), bundle.getString("brewed_by"), bundle.getString("style"), bundle.getString("abv"), bundle.getString("ibu"));
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("id", id());
        bundle.putString("name", name());
        bundle.putString("image_url", imageUrl());
        bundle.putString("brewed_by", brewedBy());
        bundle.putString("style", style());
        bundle.putString("abv", abv());
        bundle.putString("ibu", ibu());
        return bundle;
    }
}
