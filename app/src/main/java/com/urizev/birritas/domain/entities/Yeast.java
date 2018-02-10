package com.urizev.birritas.domain.entities;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Yeast {
    public abstract int id();
    public abstract String name();
    public abstract String description();

    public static Yeast create(int id, String name, String description) {
        return new AutoValue_Yeast(id, name, description);
    }
}
