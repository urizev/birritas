package com.urizev.birritas.data.mappers;

import com.google.common.collect.ImmutableList;
import com.urizev.birritas.app.rx.RxUtils;
import com.urizev.birritas.data.api.ApiService;
import com.urizev.birritas.data.cache.EntityCache;
import com.urizev.birritas.data.data.BreweryData;
import com.urizev.birritas.domain.entities.Brewery;

import javax.inject.Inject;

class BreweryMapper {
    private final EntityCache entityCache;
    private final PlaceMapper placeMapper;

    @Inject
    BreweryMapper(EntityCache entityCache, PlaceMapper placeMapper) {
        this.entityCache = entityCache;
        this.placeMapper = placeMapper;
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
        if (data == null) {
            return null;
        }

        String id = data.id();
        Brewery brewery = entityCache.getBrewery(id);
        if (brewery == null) {
            brewery = Brewery.builder()
                    .id(data.id())
                    .name(data.name())
                    .shortName(data.shortNameDisplay())
                    .description(data.description())
                    .website(data.website())
                    .established(CommonMapper.mapInteger(data.established()))
                    .images(ImageSetMapper.map(data.images()))
                    .isOrganic(data.isOrganic().equals(ApiService.YES))
                    .status(CommonMapper.mapStatus(data.status()))
                    .locations(placeMapper.map(data.locations()))
                    .build();
            entityCache.putBrewery(brewery);
        }

        return brewery;
    }
}
