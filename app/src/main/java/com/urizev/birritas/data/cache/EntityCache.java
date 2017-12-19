package com.urizev.birritas.data.cache;

import com.urizev.birritas.domain.entities.Beer;
import com.urizev.birritas.domain.entities.Brewery;
import com.urizev.birritas.domain.entities.Category;
import com.urizev.birritas.domain.entities.Country;
import com.urizev.birritas.domain.entities.Glass;
import com.urizev.birritas.domain.entities.Place;
import com.urizev.birritas.domain.entities.SRM;
import com.urizev.birritas.domain.entities.Style;

public interface EntityCache {
    Beer getBeer(String id);

    void putBeer(Beer beer);

    Category getCategory(int id);

    void putCategory(Category category);

    Glass getGlass(int id);

    void putGlass(Glass glass);

    SRM getSRM(int id);

    void putSRM(SRM srm);

    Style getStyle(int id);

    void putStyle(Style style);

    Brewery getBrewery(String id);

    void putBrewery(Brewery brewery);

    Place getPlace(String id);

    void putPlace(Place place);

    Country getCountry(String id);

    void putCountry(Country country);
}
