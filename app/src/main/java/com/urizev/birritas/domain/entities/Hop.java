package com.urizev.birritas.domain.entities;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Hop {
    public abstract int id();
    public abstract String name();
    public abstract String description();

    public static Hop create(int id, String name, String description) {
        return new AutoValue_Hop(id, name, description);
    }
}
