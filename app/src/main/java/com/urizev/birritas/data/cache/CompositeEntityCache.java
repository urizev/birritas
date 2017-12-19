package com.urizev.birritas.data.cache;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.urizev.birritas.domain.entities.Beer;
import com.urizev.birritas.domain.entities.Brewery;
import com.urizev.birritas.domain.entities.Category;
import com.urizev.birritas.domain.entities.Country;
import com.urizev.birritas.domain.entities.Glass;
import com.urizev.birritas.domain.entities.Place;
import com.urizev.birritas.domain.entities.SRM;
import com.urizev.birritas.domain.entities.Style;

import java.util.LinkedList;
import java.util.List;

public class CompositeEntityCache implements EntityCache {
    private final ImmutableList<EntityCache> caches;

    public CompositeEntityCache(EntityCache ... caches) {
        Preconditions.checkArgument(caches.length > 0);
        this.caches = ImmutableList.copyOf(caches);
    }

    @Override
    public Beer getBeer(String id) {
        List<EntityCache> toAdd = new LinkedList<>();
        Beer beer = null;
        for (EntityCache cache : caches) {
            beer = cache.getBeer(id);
            if (beer != null) {
                break;
            }
            toAdd.add(cache);
        }

        if (beer != null) {
            for (EntityCache cache : toAdd) {
                cache.putBeer(beer);
            }
        }

        return beer;
    }

    @Override
    public void putBeer(Beer beer) {
        for (EntityCache cache : caches) {
            cache.putBeer(beer);
        }
    }

    @Override
    public Category getCategory(int id) {
        List<EntityCache> toAdd = new LinkedList<>();
        Category category = null;
        for (EntityCache cache : caches) {
            category = cache.getCategory(id);
            if (category != null) {
                break;
            }
            toAdd.add(cache);
        }

        if (category != null) {
            for (EntityCache cache : toAdd) {
                cache.putCategory(category);
            }
        }

        return category;
    }

    @Override
    public void putCategory(Category category) {
        for (EntityCache cache : caches) {
            cache.putCategory(category);
        }
    }

    @Override
    public Glass getGlass(int id) {
        List<EntityCache> toAdd = new LinkedList<>();
        Glass glass = null;
        for (EntityCache cache : caches) {
            glass = cache.getGlass(id);
            if (glass != null) {
                break;
            }
            toAdd.add(cache);
        }

        if (glass != null) {
            for (EntityCache cache : toAdd) {
                cache.putGlass(glass);
            }
        }

        return glass;
    }

    @Override
    public void putGlass(Glass glass) {
        for (EntityCache cache : caches) {
            cache.putGlass(glass);
        }
    }

    @Override
    public SRM getSRM(int id) {
        List<EntityCache> toAdd = new LinkedList<>();
        SRM srm = null;
        for (EntityCache cache : caches) {
            srm = cache.getSRM(id);
            if (srm != null) {
                break;
            }
            toAdd.add(cache);
        }

        if (srm != null) {
            for (EntityCache cache : toAdd) {
                cache.putSRM(srm);
            }
        }

        return srm;
    }

    @Override
    public void putSRM(SRM srm) {
        for (EntityCache cache : caches) {
            cache.putSRM(srm);
        }
    }

    @Override
    public Style getStyle(int id) {
        List<EntityCache> toAdd = new LinkedList<>();
        Style style = null;
        for (EntityCache cache : caches) {
            style = cache.getStyle(id);
            if (style != null) {
                break;
            }
            toAdd.add(cache);
        }

        if (style != null) {
            for (EntityCache cache : toAdd) {
                cache.putStyle(style);
            }
        }

        return style;
    }

    @Override
    public void putStyle(Style style) {
        for (EntityCache cache : caches) {
            cache.putStyle(style);
        }
    }

    @Override
    public Brewery getBrewery(String id) {
        List<EntityCache> toAdd = new LinkedList<>();
        Brewery brewery = null;
        for (EntityCache cache : caches) {
            brewery = cache.getBrewery(id);
            if (brewery != null) {
                break;
            }
            toAdd.add(cache);
        }

        if (brewery != null) {
            for (EntityCache cache : toAdd) {
                cache.putBrewery(brewery);
            }
        }

        return brewery;
    }

    @Override
    public void putBrewery(Brewery brewery) {
        for (EntityCache cache : caches) {
            cache.putBrewery(brewery);
        }
    }

    @Override
    public Place getPlace(String id) {
        List<EntityCache> toAdd = new LinkedList<>();
        Place place = null;
        for (EntityCache cache : caches) {
            place = cache.getPlace(id);
            if (place != null) {
                break;
            }
            toAdd.add(cache);
        }

        if (place != null) {
            for (EntityCache cache : toAdd) {
                cache.putPlace(place);
            }
        }

        return place;
    }

    @Override
    public void putPlace(Place place) {
        for (EntityCache cache : caches) {
            cache.putPlace(place);
        }
    }

    @Override
    public Country getCountry(String id) {
        List<EntityCache> toAdd = new LinkedList<>();
        Country country = null;
        for (EntityCache cache : caches) {
            country = cache.getCountry(id);
            if (country != null) {
                break;
            }
            toAdd.add(cache);
        }

        if (country != null) {
            for (EntityCache cache : toAdd) {
                cache.putCountry(country);
            }
        }

        return country;
    }

    @Override
    public void putCountry(Country country) {
        for (EntityCache cache : caches) {
            cache.putCountry(country);
        }
    }
}
