package com.urizev.birritas.data.api.data.mappers;

import com.google.common.collect.ImmutableList;
import com.urizev.birritas.data.api.data.BeerData;
import com.urizev.birritas.data.api.data.BreweryData;
import com.urizev.birritas.data.api.data.LiveData;
import com.urizev.birritas.data.api.data.ResultData;
import com.urizev.birritas.domain.entities.Beer;
import com.urizev.birritas.domain.entities.Brewery;
import com.urizev.birritas.domain.entities.SearchResult;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Lazy;

@Singleton
public class SearchMapper {
    private final BeerMapper mBeerMapper;
    private final BreweryMapper mBreweryMapper;

    @Inject
    SearchMapper(Lazy<BeerMapper> beerMapper, Lazy<BreweryMapper> breweryMapper) {
        this.mBeerMapper = beerMapper.get();
        this.mBreweryMapper = breweryMapper.get();
    }

    public SearchResult map(ResultData<LiveData> data) {
        ImmutableList.Builder<Beer> beers = new ImmutableList.Builder<>();
        ImmutableList.Builder<Brewery> breweries = new ImmutableList.Builder<>();

        LiveData liveData = data.data();
        if (liveData != null) {
            for (BeerData beerData : liveData.beers()) {
                beers = beers.add(mBeerMapper.map(beerData));
            }
            for (BreweryData breweryData : liveData.breweries()) {
                breweries = breweries.add(mBreweryMapper.map(breweryData));
            }
        }

        return SearchResult.create(beers.build(), breweries.build());
    }
}
