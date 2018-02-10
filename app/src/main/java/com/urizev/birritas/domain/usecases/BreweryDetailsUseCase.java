package com.urizev.birritas.domain.usecases;

import com.urizev.birritas.domain.entities.Brewery;
import com.urizev.birritas.domain.repositories.BreweryRepository;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class BreweryDetailsUseCase extends UseCase<String,Brewery> {
    private final BreweryRepository breweryRepository;

    @Inject
    public BreweryDetailsUseCase(BreweryRepository breweryRepository) {
        this.breweryRepository = breweryRepository;
    }

    @Override
    protected Observable<Brewery> createObservable(String breweryId) {
        return breweryRepository.getBrewery(breweryId, true)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation());    }
}
