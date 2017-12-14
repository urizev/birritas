package com.urizev.birritas.domain.repositories;

import com.google.common.collect.ImmutableList;
import com.urizev.birritas.domain.entities.Beer;

import io.reactivex.Observable;

public interface BeerRepository {
    Observable<ImmutableList<Beer>> getFeaturedBeers();
}
