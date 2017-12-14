package com.urizev.birritas.domain.usecases;

import io.reactivex.Observable;

public abstract class UseCase<P,R> {
    public Observable<R> execute(P param) {
        return this.createObservable(param);
    }

    protected abstract Observable<R> createObservable(P param);
}
