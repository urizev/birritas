package com.urizev.birritas.domain.usecases;

import com.google.common.collect.ImmutableList;
import com.urizev.birritas.domain.entities.Beer;
import com.urizev.birritas.domain.repositories.BeerRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
public class FeaturedBeersUseCase extends UseCase<Void,ImmutableList<Beer>> {
    private final BeerRepository repository;

    @Inject
    FeaturedBeersUseCase(BeerRepository repository) {
        this.repository = repository;
    }

    @Override
    protected Observable<ImmutableList<Beer>> createObservable(Void param) {
        return repository.getFeaturedBeers();
    }
}
