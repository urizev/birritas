package com.urizev.birritas.domain.repositories;

import com.urizev.birritas.domain.entities.Brewery;

import io.reactivex.Observable;

public interface BreweryRepository {
    Observable<Brewery> getBrewery(String id, boolean cached);
}
