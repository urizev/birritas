package com.urizev.birritas.data.repositories;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.urizev.birritas.data.api.ApiService;
import com.urizev.birritas.data.api.data.BeerData;
import com.urizev.birritas.data.api.data.ResultData;
import com.urizev.birritas.data.api.data.mappers.BeerMapper;
import com.urizev.birritas.data.cache.EntityCache;
import com.urizev.birritas.domain.entities.Beer;
import com.urizev.birritas.domain.repositories.BeerRepository;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class DefaultBeerRepository implements BeerRepository {
    private static final Map<String, String> FEATURED_PARAMS = ImmutableMap.of(
            ApiService.HAS_LABELS, ApiService.YES,
            ApiService.ORDER, ApiService.ORDER_UPDATE_DATE,
            ApiService.SORT, ApiService.SORT_DESC
    );
    private final ApiService service;
    private final BeerMapper beerMapper;
    private final EntityCache entityCache;

    @Inject
    DefaultBeerRepository(ApiService service, BeerMapper beerMapper, EntityCache cache) {
        this.service = service;
        this.beerMapper = beerMapper;
        this.entityCache = cache;
    }

    @Override
    public Observable<ImmutableList<Beer>> getFeaturedBeers() {
        return service.getBeers(FEATURED_PARAMS)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map((Function<ResultData<ImmutableList<BeerData>>, ImmutableList<BeerData>>) data -> {
                    if (data.data() == null) {
                        return ImmutableList.of();
                    }
                    return data.data();
                })
                .flatMap(Observable::fromIterable)
                .map(beerMapper::map)
                .toList()
                .map(ImmutableList::copyOf)
                .toObservable();
    }

    @Override
    public Observable<ImmutableList<Beer>> getBeers(ImmutableSet<String> ids) {
        return Observable.fromIterable(ids)
                .flatMap(id -> this.getBeer(id, true).take(1))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .toList()
                .map(ImmutableList::copyOf)
                .toObservable();
    }

    @Override
    public Observable<Beer> getBeer(String id, boolean cached) {
        Observable<Beer> cache = cached ? this.getBeerFromCache(id) : Observable.empty();
        Observable<Beer> network = service.getBeer(id).flatMap(result -> {
            BeerData data = result.data();
            if (data == null) {
                return Observable.empty();
            }
            else {
                return Observable.just(data);
            }
        }).map(beerMapper::map);

        return Observable.concat(cache, network);
    }

    private Observable<Beer> getBeerFromCache(String id) {
        return Observable.defer(() -> {
            Beer beer = entityCache.getBeer(id);
            if (beer == null) {
                return Observable.empty();
            }
            else {
                return Observable.just(beer);
            }
        });
    }
}
