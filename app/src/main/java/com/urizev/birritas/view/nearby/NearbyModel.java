package com.urizev.birritas.view.nearby;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.urizev.birritas.domain.entities.Coordinate;
import com.urizev.birritas.domain.entities.Place;

@AutoValue
public abstract class NearbyModel {
    public abstract ImmutableSet<Place> places();
    @Nullable
    public abstract String selectedPlaceId();
    public abstract boolean mapReady();
    public abstract boolean waitingUserCoordinate();
    @Nullable
    public abstract Coordinate mapCoordinate();
    public abstract boolean shouldMoveMap();
    @Nullable
    public abstract Throwable error();
    public abstract boolean requestPermission();

    NearbyModel mergePlaces(ImmutableList<Place> places) {
        ImmutableSet.Builder<Place> builder = new ImmutableSet.Builder<>();
        builder = builder.addAll(places());
        builder = builder.addAll(places);

        return this.toBuilder()
                .selectedPlaceId(selectedPlaceId())
                .places(builder.build())
                .error(null)
                .build();
    }

    private Builder toBuilder() {
        return NearbyModel.builder()
                .selectedPlaceId(selectedPlaceId())
                .mapReady(mapReady())
                .waitingUserCoordinate(waitingUserCoordinate())
                .mapCoordinate(mapCoordinate())
                .shouldMoveMap(shouldMoveMap())
                .error(error())
                .places(places());
    }

    public static Builder builder() {
        return new AutoValue_NearbyModel.Builder()
                .mapReady(false)
                .requestPermission(false);
    }

    NearbyModel withError(Throwable throwable) {
        return toBuilder().error(throwable).build();
    }

    NearbyModel withIdleLocation(Coordinate coordinate) {
        return toBuilder()
                .requestPermission(requestPermission())
                .mapCoordinate(mapReady() ? coordinate : mapCoordinate())
                .mapReady(true)
                .waitingUserCoordinate(false)
                .shouldMoveMap(!mapReady() && shouldMoveMap())
                .build();
    }

    NearbyModel withUserCoordinate(Coordinate coordinate) {
        return toBuilder()
                .mapCoordinate(waitingUserCoordinate() ? coordinate : mapCoordinate())
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

    NearbyModel withRequestPermission(boolean requestPermission) {
        return toBuilder()
                .requestPermission(requestPermission)
                .build();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder places(ImmutableSet<Place> places);
        public abstract Builder selectedPlaceId(String id);
        public abstract Builder mapReady(boolean mapReady);
        public abstract Builder waitingUserCoordinate(boolean waitingUserCoordinate);
        public abstract Builder mapCoordinate(Coordinate coordinate);
        public abstract Builder shouldMoveMap(boolean shouldMoveMap);
        public abstract Builder error(Throwable error);
        public abstract Builder requestPermission(boolean requestPermission);

        public abstract NearbyModel build();
    }
}
