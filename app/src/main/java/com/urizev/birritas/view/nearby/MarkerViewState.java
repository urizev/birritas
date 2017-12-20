package com.urizev.birritas.view.nearby;

import com.google.android.gms.maps.model.MarkerOptions;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class MarkerViewState {
    public abstract String id();
    public abstract MarkerOptions markerOptions();

    public static MarkerViewState create(String id, MarkerOptions markerOptions) {
        return new AutoValue_MarkerViewState(id, markerOptions);
    }
}
