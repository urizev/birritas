package com.urizev.birritas.domain.usecases;

import com.google.common.collect.ImmutableSet;
import com.urizev.birritas.data.db.FDBService;

import javax.inject.Inject;

import io.reactivex.Observable;

public class FavoritesBeerIdsUseCase extends UseCase<Void,ImmutableSet<String>>{
    private final FDBService service;

    @Inject
    public FavoritesBeerIdsUseCase(FDBService service) {
        this.service = service;
    }

    @Override
    protected Observable<ImmutableSet<String>> createObservable(Void param) {
        return service.favoriteBeerIds();
    }
}
