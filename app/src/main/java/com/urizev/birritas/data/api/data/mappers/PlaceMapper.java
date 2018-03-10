package com.urizev.birritas.data.api.data.mappers;

import com.google.common.collect.ImmutableList;
import com.urizev.birritas.app.rx.RxUtils;
import com.urizev.birritas.data.api.ApiService;
import com.urizev.birritas.data.api.data.PlaceData;
import com.urizev.birritas.data.cache.EntityCache;
import com.urizev.birritas.domain.entities.Brewery;
import com.urizev.birritas.domain.entities.Coordinate;
import com.urizev.birritas.domain.entities.Country;
import com.urizev.birritas.domain.entities.Place;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Lazy;

@Singleton
public class PlaceMapper {
    private final EntityCache entityCache;
    private final CountryMapper countryMapper;
    private final Lazy<BreweryMapper> breweryMapper;

    @Inject
    PlaceMapper(EntityCache entityCache, CountryMapper countryMapper, Lazy<BreweryMapper> breweryMapper) {
        this.entityCache = entityCache;
        this.countryMapper = countryMapper;
        this.breweryMapper = breweryMapper;
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
        RxUtils.assertComputationThread();

        if (data == null) {
            return null;
        }


        String id = data.id();
        boolean saveToCache = false;
        Place place = entityCache.getPlace(id);
        if (place == null) {
            Brewery brewery = breweryMapper.get().map(data.brewery());
            if (brewery == null) {
                return null;
            }

            saveToCache = true;
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
                    .coordinate(Coordinate.create(data.latitude(), data.longitude()))
                    .locationType(data.locationType())
                    .phone(data.phone())
                    .status(CommonMapper.mapStatus(data.status()))
                    .website(data.website())
                    .yearOpened(CommonMapper.mapInteger(data.yearOpened()))
                    .brewery(brewery)
                    .build();
        }
        else {
            Place.Builder builder = place.toBuilder();
            if (!place.name().equals(data.name())) {
                saveToCache = true;
                builder = builder.name(data.name());
            }
            String newStreetAddress = data.streetAddress();
            if (newStreetAddress != null && !newStreetAddress.equals(place.streetAddress())) {
                saveToCache = true;
                builder = builder.streetAddress(newStreetAddress);
            }
            String newExtendedAddress = data.extendedAddress();
            if (newExtendedAddress != null && !newExtendedAddress.equals(place.extendedAddress())) {
                saveToCache = true;
                builder = builder.extendedAddress(newExtendedAddress);
            }
            String newPostalCode = data.postalCode();
            if (newPostalCode != null && !newPostalCode.equals(place.postalCode())) {
                saveToCache = true;
                builder = builder.postalCode(newPostalCode);
            }
            String newLocality = data.locality();
            if (newLocality != null && !newLocality.equals(place.locality())) {
                saveToCache = true;
                builder = builder.locality(newLocality);
            }
            String newRegion = data.region();
            if (newRegion != null && !newRegion.equals(place.region())) {
                saveToCache = true;
                builder = builder.region(newRegion);
            }
            Country country = place.country();
            Country newCountry = countryMapper.map(data.country());
            if (!country.equals(newCountry)) {
                saveToCache = true;
                builder = builder.country(newCountry);
            }
            if (data.isPlanning() != null && place.isPlanning() != ApiService.YES.equals(data.isPlanning())) {
                saveToCache = true;
                builder = builder.isPlanning(!place.isPlanning());
            }
            if (data.isPlanning() != null && place.isPrimary() != ApiService.YES.equals(data.isPrimary())) {
                saveToCache = true;
                builder = builder.isPrimary(!place.isPrimary());
            }
            if (data.isClosed() != null && place.isClosed() != ApiService.YES.equals(data.isClosed())) {
                saveToCache = true;
                builder = builder.isClosed(!place.isClosed());
            }
            if (data.openToPublic() != null && place.openToPublic() != ApiService.YES.equals(data.openToPublic())) {
                saveToCache = true;
                builder = builder.openToPublic(!place.openToPublic());
            }
            Coordinate coordinate = place.coordinate();
            Coordinate newCoordinate = Coordinate.create(data.latitude(), data.longitude());
            if (!coordinate.equals(newCoordinate)) {
                saveToCache = true;
                builder = builder.coordinate(newCoordinate);
            }
            String newLocationType = data.locationType();
            if (newLocationType != null && !newLocationType.equals(place.locationType())) {
                saveToCache = true;
                builder = builder.locationType(newLocationType);
            }
            String newPhone = data.phone();
            if (newPhone != null && !newPhone.equals(place.phone())) {
                saveToCache = true;
                builder = builder.phone(newPhone);
            }
            int newStatus = CommonMapper.mapStatus(data.status());
            if (newStatus != place.status()) {
                saveToCache = true;
                builder = builder.status(newStatus);
            }
            String newWebsite = place.website();
            if (newWebsite != null && !newWebsite.equals(place.website())) {
                saveToCache = true;
                builder = builder.website(newWebsite);
            }
            Integer yearOpened = place.yearOpened();
            Integer newYearOpened = CommonMapper.mapInteger(data.yearOpened());
            if (newYearOpened != null && !newYearOpened.equals(yearOpened)) {
                saveToCache = true;
                builder = builder.yearOpened(newYearOpened);
            }
            Brewery brewery = place.brewery();
            Brewery newBrewery = breweryMapper.get().map(data.brewery());
            if (newBrewery != null && !newBrewery.equals(brewery)) {
                builder = builder.brewery(newBrewery);
            }
            if (saveToCache) {
                place = builder.build();
            }
        }

        if (saveToCache) {
            entityCache.putPlace(place);
        }

        return place;
    }
}
