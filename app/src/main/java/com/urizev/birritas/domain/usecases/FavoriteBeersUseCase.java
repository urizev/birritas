package com.urizev.birritas.domain.usecases;

import com.google.common.collect.ImmutableList;
import com.urizev.birritas.data.db.FDBService;
import com.urizev.birritas.domain.entities.Beer;
import com.urizev.birritas.domain.repositories.BeerRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class FavoriteBeersUseCase extends UseCase <Void,ImmutableList<Beer>> {
    private final FDBService service;
    private final BeerRepository repository;

    @Inject
    FavoriteBeersUseCase(FDBService service, BeerRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @Override
    protected Observable<ImmutableList<Beer>> createObservable(Void param) {
        return service.favoriteBeerIds()
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.io())
                .flatMap(repository::getBeers);
    }
}
