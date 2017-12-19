package com.urizev.birritas.domain.repositories;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.urizev.birritas.domain.entities.Beer;

import io.reactivex.Observable;

public interface BeerRepository {
    Observable<ImmutableList<Beer>> getFeaturedBeers();
    Observable<ImmutableList<Beer>> getBeers(ImmutableSet<String> ids);

    Observable<Beer> getBeer(String id, boolean cached);
}
