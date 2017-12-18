package com.urizev.birritas.domain.entities;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ImageSet {
    public abstract String icon();
    public abstract String medium();
    public abstract String large();
    @Nullable
    public abstract String squareMedium();
    @Nullable
    public abstract String squareLarge();

    public static ImageSet create(String icon, String medium, String large, String squareMedium, String squareLarge) {
        return new AutoValue_ImageSet(icon, medium, large, squareMedium, squareLarge);
    }
}
