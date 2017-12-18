package com.urizev.birritas.domain.entities;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Glass {
    public abstract int id();
    public abstract String name();

    public static Glass create(int id, String name) {
        return new AutoValue_Glass(id, name);
    }
}
