package com.urizev.birritas.view.nearby;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.urizev.birritas.domain.entities.Coordinate;
import com.urizev.birritas.domain.entities.Place;

@AutoValue
public abstract class NearbyModel {
    public abstract boolean loading();
    @Nullable
    public abstract Throwable throwable();
    public abstract ImmutableSet<Place> places();
    @Nullable
    public abstract String selectedPlaceId();
    public abstract boolean mapReady();
    public abstract boolean waitingUserCoordinate();
    @Nullable
    public abstract Coordinate mapCoordinate();
    public abstract boolean shouldMoveMap();
    public abstract boolean requestLocationPermission();
    public abstract boolean hasLocationPermission();

    private Builder toBuilder() {
        return NearbyModel.builder()
                .selectedPlaceId(selectedPlaceId())
                .mapReady(mapReady())
                .waitingUserCoordinate(waitingUserCoordinate())
                .mapCoordinate(mapCoordinate())
                .shouldMoveMap(shouldMoveMap())
                .requestLocationPermission(requestLocationPermission())
                .hasLocationPermission(hasLocationPermission())
                .loading(loading())
                .throwable(throwable())
                .places(places());
    }

    public static Builder builder() {
        return new AutoValue_NearbyModel.Builder()
                .mapReady(false)
                .hasLocationPermission(false)
                .requestLocationPermission(false);
    }

    NearbyModel mergePlaces(ImmutableList<Place> places) {
        ImmutableSet.Builder<Place> builder = new ImmutableSet.Builder<>();
        builder = builder.addAll(places());
        builder = builder.addAll(places);

        return this.toBuilder()
                .selectedPlaceId(selectedPlaceId())
                .places(builder.build())
                .loading(false)
                .throwable(null)
                .build();
    }

    NearbyModel withThrowable(Throwable throwable) {
        return toBuilder()
                .loading(throwable == null)
                .throwable(throwable)
                .build();
    }

    NearbyModel withIdleLocation(Coordinate coordinate) {
        return toBuilder()
                .requestLocationPermission(requestLocationPermission())
                .mapCoordinate(mapReady() ? coordinate : mapCoordinate())
                .mapReady(true)
                .waitingUserCoordinate(false)
                .shouldMoveMap(!mapReady() && shouldMoveMap())
                .build();
    }

    NearbyModel withUserCoordinate(Coordinate coordinate) {
        return toBuilder()
                .mapCoordinate(coordinate)
                .waitingUserCoordinate(false)
                .shouldMoveMap(true)
                .build();
    }

    NearbyModel withMapMoving() {
        return toBuilder()
                .shouldMoveMap(false)
                .build();
    }

    NearbyModel withSelection(String id, Coordinate coordinate) {
        return toBuilder()
                .selectedPlaceId(id)
                .mapCoordinate(coordinate != null ? coordinate : mapCoordinate())
                .shouldMoveMap(true)
                .build();
    }

    NearbyModel withRequestPermission(boolean requestLocationPermission) {
        return toBuilder()
                .hasLocationPermission(!requestLocationPermission && hasLocationPermission())
                .requestLocationPermission(requestLocationPermission)
                .build();
    }



    NearbyModel withHasPermission() {
        return toBuilder()
                .hasLocationPermission(true)
                .build();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder loading(boolean loading);
        public abstract Builder places(ImmutableSet<Place> places);
        public abstract Builder selectedPlaceId(String id);
        public abstract Builder mapReady(boolean mapReady);
        public abstract Builder waitingUserCoordinate(boolean waitingUserCoordinate);
        public abstract Builder mapCoordinate(Coordinate coordinate);
        public abstract Builder shouldMoveMap(boolean shouldMoveMap);
        public abstract Builder throwable(Throwable throwable);
        public abstract Builder requestLocationPermission(boolean requestLocationPermission);
        public abstract Builder hasLocationPermission(boolean hasLocationPermission);

        public abstract NearbyModel build();
    }
}
