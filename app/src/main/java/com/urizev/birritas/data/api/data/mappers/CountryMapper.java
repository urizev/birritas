package com.urizev.birritas.data.api.data.mappers;


import com.urizev.birritas.app.rx.RxUtils;
import com.urizev.birritas.data.api.data.CountryData;
import com.urizev.birritas.data.cache.EntityCache;
import com.urizev.birritas.domain.entities.Country;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class CountryMapper {
    public final EntityCache entityCache;

    @Inject
    CountryMapper(EntityCache entityCache) {
        this.entityCache = entityCache;
    }

    public Country map(CountryData data) {
        RxUtils.assertComputationThread();

        if (data == null) {
            return null;
        }

        String isoCode = data.isoCode();
        Country country = entityCache.getCountry(isoCode);
        if (country == null) {
            country = Country.create(isoCode, data.name(), data.displayName(), data.isoThree(), data.numberCode());
            entityCache.putCountry(country);
        }

        return country;
    }
}
