package com.urizev.birritas.domain.usecases;

import com.google.common.collect.ImmutableSet;
import com.urizev.birritas.data.db.FDBService;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
public class FavoritesBeerIdsUseCase extends UseCase<Void,ImmutableSet<String>>{
    private final FDBService service;

    @Inject
    FavoritesBeerIdsUseCase(FDBService service) {
        this.service = service;
    }

    @Override
    protected Observable<ImmutableSet<String>> createObservable(Void param) {
        return service.favoriteBeerIds();
    }
}
