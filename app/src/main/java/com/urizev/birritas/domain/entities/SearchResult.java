package com.urizev.birritas.domain.entities;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

@AutoValue
public abstract class SearchResult {
    public abstract ImmutableList<Beer> beers();
    public abstract ImmutableList<Brewery> breweries();

    public static SearchResult create(ImmutableList<Beer> beers, ImmutableList<Brewery> breweries) {
        return new AutoValue_SearchResult(beers, breweries);
    }
}
