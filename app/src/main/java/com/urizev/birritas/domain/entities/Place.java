package com.urizev.birritas.domain.entities;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Place {
    public abstract String id();
    public abstract String name();
    public abstract String streetAddress();
    public abstract String extendedAddress();
    public abstract String locality();
    public abstract String region();
    public abstract String postalCode();
    public abstract String phone();
    public abstract String website();
    public abstract double latitude();
    public abstract double longitude();
    public abstract boolean isPrimary();
    public abstract boolean isPlanning();
    public abstract boolean isClosed();
    public abstract boolean openToPublic();
    public abstract String locationType();
    public abstract Integer yearOpened();
    public abstract int status();
    public abstract Country country();

    public static Builder builder() {
        return new AutoValue_Place.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder id(String id);
        public abstract Builder name(String name);
        public abstract Builder streetAddress(String streetAddress);
        public abstract Builder extendedAddress(String extendedAddress);
        public abstract Builder locality(String locality);
        public abstract Builder region(String region);
        public abstract Builder postalCode(String postalCode);
        public abstract Builder phone(String phone);
        public abstract Builder website(String website);
        public abstract Builder latitude(double latitude);
        public abstract Builder longitude(double longitude);
        public abstract Builder isPrimary(boolean isPrimary);
        public abstract Builder isPlanning(boolean isPlanning);
        public abstract Builder isClosed(boolean isClosed);
        public abstract Builder openToPublic(boolean openToPublic);
        public abstract Builder locationType(String locationType);
        public abstract Builder yearOpened(Integer year);
        public abstract Builder status(int status);
        public abstract Builder country(Country country);

        public abstract Place build();
    }
}
