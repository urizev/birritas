package com.urizev.birritas.data.repositories;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.urizev.birritas.data.api.ApiService;
import com.urizev.birritas.data.api.data.BeerData;
import com.urizev.birritas.data.api.data.BreweryData;
import com.urizev.birritas.data.api.data.mappers.BeerMapper;
import com.urizev.birritas.data.api.data.mappers.BreweryMapper;
import com.urizev.birritas.data.cache.EntityCache;
import com.urizev.birritas.domain.entities.Beer;
import com.urizev.birritas.domain.entities.Brewery;
import com.urizev.birritas.domain.repositories.BreweryRepository;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;


@Singleton
public class DefaultBreweryRepository implements BreweryRepository {
    private static final Map<String, String> BREWERY_PARAMS = ImmutableMap.of(
            ApiService.WITH_LOCATIONS, ApiService.YES,
            ApiService.WITH_SOCIAL_ACCOUNTS, ApiService.YES,
            ApiService.WITH_INGREDIENTS, ApiService.YES
    );

    private final ApiService mService;
    private final BreweryMapper mBreweryMapper;
    private final BeerMapper mBeerMapper;
    private final EntityCache mEntityCache;

    @Inject
    DefaultBreweryRepository(ApiService service, BreweryMapper breweryMapper, BeerMapper beerMapper, EntityCache cache) {
        this.mService = service;
        this.mBreweryMapper = breweryMapper;
        this.mBeerMapper = beerMapper;
        this.mEntityCache = cache;
    }

    @Override
    public Observable<Brewery> getBrewery(String id, boolean cached) {
        Observable<Brewery> cache = cached ? this.getBreweryFromCache(id) : Observable.empty();
        Observable<BreweryData> networkBrewery = mService.getBrewery(id, BREWERY_PARAMS)
                .observeOn(Schedulers.computation())
                .flatMap(result -> {
                    BreweryData data = result.data();
                    if (data == null) {
                        return Observable.empty();
                    } else {
                        return Observable.just(data);
                    }
                });

        Observable<ImmutableList<Beer>> networkBeers = mService.getBreweryBeers(id)
                .observeOn(Schedulers.computation())
                .map(result -> {
                    ImmutableList<BeerData> data = result.data();
                    return data != null ? data : ImmutableList.<BeerData>of();
                })
                .map(mBeerMapper::map);

        Observable<Brewery> network = Observable.zip(networkBrewery, networkBeers, new BiFunction<BreweryData, ImmutableList<Beer>, Brewery>() {
            @Override
            public Brewery apply(BreweryData breweryData, ImmutableList<Beer> beers) throws Exception {
                return mBreweryMapper.map(breweryData, beers);
            }
        });

        return Observable.concat(cache.subscribeOn(Schedulers.computation()), network)
                .subscribeOn(Schedulers.computation());
    }

    private Observable<Brewery> getBreweryFromCache(String id) {
        return Observable.defer(() -> {
            Brewery brewery = mEntityCache.getBrewery(id);
            if (brewery == null) {
                return Observable.empty();
            }
            else {
                return Observable.just(brewery);
            }
        });
    }
}
