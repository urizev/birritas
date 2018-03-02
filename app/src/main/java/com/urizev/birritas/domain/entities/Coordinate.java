package com.urizev.birritas.domain.entities;

import com.google.auto.value.AutoValue;

import java.util.Locale;

@AutoValue
public abstract class Coordinate {
    public abstract double latitude();
    public abstract double longitude();

    public static Coordinate create(double latitude, double longitude) {
        return new AutoValue_Coordinate(latitude, longitude);
    }

    public String toString() {
        return String.format(Locale.US,"%f,%f", latitude(), longitude());
    }
}
