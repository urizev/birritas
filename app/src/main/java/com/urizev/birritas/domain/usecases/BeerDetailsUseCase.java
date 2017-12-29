package com.urizev.birritas.domain.usecases;

import com.urizev.birritas.domain.entities.Beer;
import com.urizev.birritas.domain.repositories.BeerRepository;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class BeerDetailsUseCase extends UseCase<String,Beer> {
    private final BeerRepository beerRepository;

    @Inject
    public BeerDetailsUseCase(BeerRepository beerRepository) {
        this.beerRepository = beerRepository;
    }

    @Override
    protected Observable<Beer> createObservable(String beerId) {
        return beerRepository.getBeer(beerId, true)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation());
    }
}
