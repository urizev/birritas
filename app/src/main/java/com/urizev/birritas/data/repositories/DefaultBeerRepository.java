package com.urizev.birritas.data.repositories;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.urizev.birritas.data.api.ApiService;
import com.urizev.birritas.data.data.ResultData;
import com.urizev.birritas.data.mappers.BeerMapper;
import com.urizev.birritas.domain.entities.Beer;
import com.urizev.birritas.domain.repositories.BeerRepository;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;


public class DefaultBeerRepository implements BeerRepository {
    private static final Map<String, String> FEATURED_PARAMS = ImmutableMap.of(
            ApiService.HAS_LABELS, ApiService.YES,
            ApiService.ORDER, ApiService.ORDER_UPDATE_DATE,
            ApiService.SORT, ApiService.SORT_DESC
    );
    private final ApiService service;
    private final BeerMapper beerMapper;

    @Inject
    DefaultBeerRepository(ApiService service, BeerMapper beerMapper) {
        this.service = service;
        this.beerMapper = beerMapper;
    }

    @Override
    public Observable<ImmutableList<Beer>> getFeaturedBeers() {
        return service.getBeers(FEATURED_PARAMS)
                .map(ResultData::data)
                .flatMap(Observable::fromIterable)
                .map(beerMapper::map)
                .toList()
                .map(ImmutableList::copyOf)
                .toObservable();
    }
}