package com.urizev.birritas.domain.entities;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Place {
    public abstract String id();
    public abstract String name();
    @Nullable
    public abstract String streetAddress();
    @Nullable
    public abstract String extendedAddress();
    @Nullable
    public abstract String locality();
    @Nullable
    public abstract String region();
    @Nullable
    public abstract String postalCode();
    @Nullable
    public abstract String phone();
    @Nullable
    public abstract String website();
    public abstract Coordinate coordinate();
    public abstract boolean isPrimary();
    public abstract boolean isPlanning();
    public abstract boolean isClosed();
    public abstract boolean openToPublic();
    public abstract String locationType();
    @Nullable
    public abstract Integer yearOpened();
    public abstract int status();
    public abstract Country country();
    @Nullable
    public abstract Brewery brewery();

    public static Builder builder() {
        return new AutoValue_Place.Builder();
    }

    public Builder toBuilder() {
        return new AutoValue_Place.Builder()
                .id(id())
                .name(name())
                .streetAddress(streetAddress())
                .extendedAddress(extendedAddress())
                .locality(locality())
                .region(region())
                .postalCode(postalCode())
                .phone(phone())
                .website(website())
                .coordinate(coordinate())
                .isPlanning(isPlanning())
                .isPrimary(isPrimary())
                .isClosed(isClosed())
                .openToPublic(openToPublic())
                .locationType(locationType())
                .yearOpened(yearOpened())
                .status(status())
                .country(country())
                .brewery(brewery());
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
        public abstract Builder coordinate(Coordinate coordinate);
        public abstract Builder isPrimary(boolean isPrimary);
        public abstract Builder isPlanning(boolean isPlanning);
        public abstract Builder isClosed(boolean isClosed);
        public abstract Builder openToPublic(boolean openToPublic);
        public abstract Builder locationType(String locationType);
        public abstract Builder yearOpened(Integer year);
        public abstract Builder status(int status);
        public abstract Builder country(Country country);
        public abstract Builder brewery(Brewery brewery);

        public abstract Place build();
    }
}
