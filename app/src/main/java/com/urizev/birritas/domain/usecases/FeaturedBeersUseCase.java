package com.urizev.birritas.domain.usecases;

import com.google.common.collect.ImmutableList;
import com.urizev.birritas.domain.entities.Beer;
import com.urizev.birritas.domain.repositories.BeerRepository;

import javax.inject.Inject;

import io.reactivex.Observable;

public class FeaturedBeersUseCase extends UseCase<Void,ImmutableList<Beer>> {
    private final BeerRepository repository;

    @Inject
    protected FeaturedBeersUseCase(BeerRepository repository) {
        this.repository = repository;
    }

    @Override
    protected Observable<ImmutableList<Beer>> createObservable(Void param) {
        return repository.getFeaturedBeers();
    }
}
