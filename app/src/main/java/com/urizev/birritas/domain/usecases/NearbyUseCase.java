package com.urizev.birritas.domain.usecases;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.urizev.birritas.data.api.ApiService;
import com.urizev.birritas.data.data.ResultData;
import com.urizev.birritas.data.mappers.PlaceMapper;
import com.urizev.birritas.domain.entities.Place;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

public class NearbyUseCase extends UseCase<NearbyUseCase.Param,ImmutableList<Place>>{
    private final ApiService apiService;
    private final PlaceMapper placeMapper;

    @Inject
    public NearbyUseCase(ApiService apiService, PlaceMapper placeMapper) {
        this.apiService = apiService;
        this.placeMapper = placeMapper;
    }

    @Override
    protected Observable<ImmutableList<Place>> createObservable(Param param) {
        return apiService.searchGeoPoint(mapParam(param))
                .map(ResultData::data)
                .map(placeMapper::map);
    }

    private Map<String, String> mapParam(Param param) {
        return ImmutableMap.of(
                ApiService.LATITUDE, String.valueOf(param.latitude()),
                ApiService.LONGITUDE, String.valueOf(param.longitude()),
                ApiService.RADIUS, String.valueOf(param.radius())
        );
    }

    @AutoValue
    public static abstract class Param {
        public abstract double latitude();
        public abstract double longitude();
        public abstract int radius();

        public static Param create(double latitude, double longitude, int radius) {
            return new AutoValue_NearbyUseCase_Param(latitude, longitude, radius);
        }
    }
}
