package com.urizev.birritas.domain.entities;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Country {
    public abstract String isoCode();
    public abstract String name();
    public abstract String displayName();
    public abstract String isoThree();
    public abstract int numberCode();

    public static Country create(String isoCode, String name, String displayName, String isoThree, int numberCode) {
        return new AutoValue_Country(isoCode, name, displayName, isoThree, numberCode);
    }
}
