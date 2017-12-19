package com.urizev.birritas.data.api.data.mappers;

import com.google.common.collect.ImmutableList;
import com.urizev.birritas.app.rx.RxUtils;
import com.urizev.birritas.data.api.ApiService;
import com.urizev.birritas.data.cache.EntityCache;
import com.urizev.birritas.data.api.data.PlaceData;
import com.urizev.birritas.domain.entities.Place;

import javax.inject.Inject;

public class PlaceMapper {
    private final EntityCache entityCache;
    private final CountryMapper countryMapper;

    @Inject
    PlaceMapper(EntityCache entityCache, CountryMapper countryMapper) {
        this.entityCache = entityCache;
        this.countryMapper = countryMapper;
    }

    public ImmutableList<Place> map(ImmutableList<PlaceData> data) {
        RxUtils.assertComputationThread();

        if (data == null) {
            return null;
        }

        ImmutableList.Builder<Place> builder = new ImmutableList.Builder<>();

        for (PlaceData datum : data) {
            Place place = this.map(datum);
            if (place != null) {
                builder = builder.add(place);
            }
        }

        return builder.build();
    }

    public Place map(PlaceData data) {
        if (data == null) {
            return null;
        }

        String id = data.id();
        Place place = entityCache.getPlace(id);
        if (place == null) {
            place = Place.builder()
                    .id(data.id())
                    .name(data.name())
                    .streetAddress(data.streetAddress())
                    .extendedAddress(data.extendedAddress())
                    .postalCode(data.postalCode())
                    .locality(data.locality())
                    .region(data.region())
                    .country(countryMapper.map(data.country()))
                    .isClosed(data.isClosed().equals(ApiService.YES))
                    .isPlanning(ApiService.YES.equals(data.isPlanning()))
                    .openToPublic(data.openToPublic().equals(ApiService.YES))
                    .isPrimary(data.isPrimary().equals(ApiService.YES))
                    .latitude(data.latitude())
                    .longitude(data.longitude())
                    .locationType(data.locationType())
                    .phone(data.phone())
                    .status(CommonMapper.mapStatus(data.status()))
                    .website(data.website())
                    .yearOpened(CommonMapper.mapInteger(data.yearOpened()))
                    .website(data.website())
                    .build();
            entityCache.putPlace(place);
        }

        return place;
    }
}
