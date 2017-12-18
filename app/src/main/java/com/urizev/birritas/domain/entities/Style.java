package com.urizev.birritas.domain.entities;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Style {
    public abstract int id();
    public abstract String name();
    public abstract String shortName();
    public abstract String description();
    public abstract Category category();

    public static Style create(int id, String name, String shortName, String description, Category category) {
        return new AutoValue_Style(id, name, shortName, description, category);
    }
}
