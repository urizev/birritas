package com.urizev.birritas.domain.entities;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class SRM {
    public abstract int id();
    public abstract int value();
    public abstract boolean over();
    public abstract int color();

    public static SRM create(int id, int value, boolean over, int color) {
        return new AutoValue_SRM(id, value, over, color);
    }
}
