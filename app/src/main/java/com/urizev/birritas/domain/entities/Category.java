package com.urizev.birritas.domain.entities;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Category {
    public abstract int id();
    public abstract String name();

    public static Category create(int id, String name) {
        return new AutoValue_Category(id, name);
    }
}
