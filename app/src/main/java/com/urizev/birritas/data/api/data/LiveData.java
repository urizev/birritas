package com.urizev.birritas.data.api.data;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

@AutoValue
public abstract class LiveData {
    public static final String TYPE_BEER = "beer";
    public static final String TYPE_BREWERY = "brewery";

    public abstract ImmutableList<BeerData> beers();
    public abstract ImmutableList<BreweryData> breweries();

    public static LiveData create(ImmutableList<BeerData> beers, ImmutableList<BreweryData> breweries) {
        return new AutoValue_LiveData(beers, breweries);
    }
}
