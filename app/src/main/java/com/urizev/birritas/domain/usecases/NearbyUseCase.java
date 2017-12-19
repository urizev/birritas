package com.urizev.birritas.domain.usecases;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.urizev.birritas.data.api.ApiService;
import com.urizev.birritas.data.api.data.PlaceData;
import com.urizev.birritas.data.api.data.ResultData;
import com.urizev.birritas.data.api.data.mappers.PlaceMapper;
import com.urizev.birritas.domain.entities.Place;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class NearbyUseCase extends UseCase<NearbyUseCase.SearchSpec,ImmutableList<Place>>{
    private final ApiService apiService;
    private final PlaceMapper placeMapper;

    @Inject
    public NearbyUseCase(ApiService apiService, PlaceMapper placeMapper) {
        this.apiService = apiService;
        this.placeMapper = placeMapper;
    }

    @Override
    protected Observable<ImmutableList<Place>> createObservable(SearchSpec searchSpec) {
        Timber.d("Executing %s", searchSpec);
        return apiService.searchGeoPoint(mapParam(searchSpec))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map((Function<ResultData<ImmutableList<PlaceData>>, ImmutableList<PlaceData>>) data -> {
                    if (data.data() == null) {
                        return ImmutableList.of();
                    }
                    return data.data();
                })
                .map(placeMapper::map);
    }

    private Map<String, String> mapParam(SearchSpec searchSpec) {
        return ImmutableMap.of(
                ApiService.LATITUDE, String.valueOf(searchSpec.latitude()),
                ApiService.LONGITUDE, String.valueOf(searchSpec.longitude()),
                ApiService.RADIUS, String.valueOf(searchSpec.radius())
        );
    }

    @AutoValue
    public static abstract class SearchSpec {
        private static final int MAX_RADIUS = 100;

        public abstract double latitude();
        public abstract double longitude();
        public abstract int radius();

        public static SearchSpec create(double latitude, double longitude, int radius) {
            return new AutoValue_NearbyUseCase_SearchSpec(latitude, longitude, Math.min(radius, MAX_RADIUS));
        }
    }
}
