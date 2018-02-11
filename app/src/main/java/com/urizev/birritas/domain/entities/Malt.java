package com.urizev.birritas.domain.entities;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Malt {
    public abstract int id();
    public abstract String name();
    @Nullable
    public abstract String description();

    public static Malt create(int id, String name, String description) {
        return new AutoValue_Malt(id, name, description);
    }
}
