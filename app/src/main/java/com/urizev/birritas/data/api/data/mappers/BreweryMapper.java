package com.urizev.birritas.data.api.data.mappers;

import com.google.common.collect.ImmutableList;
import com.urizev.birritas.app.rx.RxUtils;
import com.urizev.birritas.data.api.ApiService;
import com.urizev.birritas.data.api.data.BreweryData;
import com.urizev.birritas.data.cache.EntityCache;
import com.urizev.birritas.domain.entities.Beer;
import com.urizev.birritas.domain.entities.Brewery;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BreweryMapper {
    private final EntityCache mEntityCache;
    private final PlaceMapper mPlaceMapper;

    @Inject
    BreweryMapper(EntityCache entityCache, PlaceMapper placeMapper) {
        this.mEntityCache = entityCache;
        this.mPlaceMapper = placeMapper;
    }

    public ImmutableList<Brewery> map(ImmutableList<BreweryData> data) {
        RxUtils.assertComputationThread();

        if (data == null) {
            return null;
        }

        ImmutableList.Builder<Brewery> builder = new ImmutableList.Builder<>();

        for (BreweryData datum : data) {
            Brewery brewery = this.map(datum);
            if (brewery != null) {
                builder = builder.add(brewery);
            }
        }

        return builder.build();
    }
    public Brewery map(BreweryData data) {
        return this.map(data, ImmutableList.of());
    }

    public Brewery map(BreweryData data, ImmutableList<Beer> beers) {
        RxUtils.assertComputationThread();

        if (data == null) {
            return null;
        }

        Brewery brewery = mEntityCache.getBrewery(data.id());
        Brewery.Builder builder;
        boolean saveToCache = false;
        if (brewery == null) {
            saveToCache = true;
            builder = Brewery.builder()
                    .id(data.id())
                    .name(data.name())
                    .shortName(data.shortNameDisplay())
                    .description(data.description())
                    .website(data.website())
                    .established(CommonMapper.mapInteger(data.established()))
                    .images(ImageSetMapper.map(data.images()))
                    .isOrganic(data.isOrganic().equals(ApiService.YES))
                    .status(CommonMapper.mapStatus(data.status()))
                    .locations(mPlaceMapper.map(data.locations() != null ? data.locations() : ImmutableList.of()));
            if (beers != null) {
                builder = builder.beers(beers);
            }
        } else {
            builder = brewery.toBuilder();
            if (data.name() != null) {
                saveToCache = true;
                builder = builder.name(data.name());
            }
            if (data.description() != null) {
                saveToCache = true;
                builder = builder.description(data.description());
            }
            if (data.isOrganic() != null) {
                saveToCache = true;
                builder = builder.isOrganic(data.isOrganic().equals(ApiService.YES));
            }
            if (data.shortNameDisplay() != null) {
                saveToCache = true;
                builder = builder.shortName(data.shortNameDisplay());
            }
            if (data.website() != null) {
                saveToCache = true;
                builder = builder.website(data.website());
            }
            if (data.established() != null) {
                saveToCache = true;
                builder = builder.established(CommonMapper.mapInteger(data.established()));
            }
            if (data.status() != null) {
                saveToCache = true;
                builder = builder.status(CommonMapper.mapStatus(data.status()));
            }
            if (data.images() != null) {
                saveToCache = true;
                builder = builder.images(ImageSetMapper.map(data.images()));
            }
            if (data.locations() != null) {
                saveToCache = true;
                builder = builder.locations(mPlaceMapper.map(data.locations()));
            }
            if (beers != null) {
                saveToCache = true;
                builder = builder.beers(beers);
            }
        }

        if (saveToCache) {
            brewery = builder.build();
            mEntityCache.putBrewery(brewery);
        }

        return brewery;
    }
}
