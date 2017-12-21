package com.urizev.birritas.domain.usecases;


import com.google.auto.value.AutoValue;
import com.urizev.birritas.data.db.FDBService;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;

@Singleton
public class UpdateFavoriteBeerUseCase extends UseCase<UpdateFavoriteBeerUseCase.FavoriteEvent,Void>{
    private final FDBService service;

    @Inject
    public UpdateFavoriteBeerUseCase(FDBService service) {
        this.service = service;
    }

    @Override
    protected Observable<Void> createObservable(FavoriteEvent param) {
        Completable completable = param.isFavorite()
                ? service.markBeerAsFavorite(param.id())
                : service.unmarkBeerAsFavorite(param.id());

        return completable.toObservable();
    }

    @AutoValue
    public static abstract class FavoriteEvent {
        public abstract String id();
        public abstract boolean isFavorite();

        public static FavoriteEvent create(String id, boolean favorite) {
            return new AutoValue_UpdateFavoriteBeerUseCase_FavoriteEvent(id, favorite);
        }
    }
}
