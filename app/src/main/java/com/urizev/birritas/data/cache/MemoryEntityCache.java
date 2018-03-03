package com.urizev.birritas.data.cache;


import android.support.v4.util.ArrayMap;
import android.util.SparseArray;

import com.urizev.birritas.app.rx.RxUtils;
import com.urizev.birritas.domain.entities.Beer;
import com.urizev.birritas.domain.entities.Brewery;
import com.urizev.birritas.domain.entities.Category;
import com.urizev.birritas.domain.entities.Country;
import com.urizev.birritas.domain.entities.Glass;
import com.urizev.birritas.domain.entities.Hop;
import com.urizev.birritas.domain.entities.Malt;
import com.urizev.birritas.domain.entities.Place;
import com.urizev.birritas.domain.entities.SRM;
import com.urizev.birritas.domain.entities.Style;
import com.urizev.birritas.domain.entities.Yeast;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MemoryEntityCache implements EntityCache {
    private final ArrayMap<String,Beer> mBeers;
    private final ArrayMap<String,Brewery> mBreweries;
    private final ArrayMap<String,Place> mPlaces;
    private final ArrayMap<String,Country> mCountries;
    private final SparseArray<Category> mCategories;
    private final SparseArray<Glass> mGlasses;
    private final SparseArray<SRM> mSrms;
    private final SparseArray<Style> mStyles;
    private final SparseArray<Hop> mHops;
    private final SparseArray<Yeast> mYeasts;
    private final SparseArray<Malt> mMalts;

    @Inject
    MemoryEntityCache() {
        this.mBeers = new ArrayMap<>();
        this.mBreweries = new ArrayMap<>();
        this.mPlaces = new ArrayMap<>();
        this.mCountries = new ArrayMap<>();
        this.mCategories = new SparseArray<>();
        this.mGlasses = new SparseArray<>();
        this.mSrms = new SparseArray<>();
        this.mStyles = new SparseArray<>();
        this.mHops = new SparseArray<>();
        this.mYeasts = new SparseArray<>();
        this.mMalts = new SparseArray<>();
    }

    @Override
    public Beer getBeer(String id) {
        RxUtils.assertComputationThread();
        synchronized (mBeers) {
            return this.mBeers.get(id);
        }
    }

    @Override
    public void putBeer(Beer beer) {
        RxUtils.assertComputationThread();
        synchronized (mBeers) {
            mBeers.put(beer.id(), beer);
        }
    }

    @Override
    public Category getCategory(int id) {
        RxUtils.assertComputationThread();
        synchronized (mCategories) {
            return mCategories.get(id);
        }
    }

    @Override
    public void putCategory(Category category) {
        RxUtils.assertComputationThread();
        synchronized (mCategories) {
            mCategories.put(category.id(), category);
        }
    }

    @Override
    public Glass getGlass(int id) {
        RxUtils.assertComputationThread();
        synchronized (mGlasses) {
            return mGlasses.get(id);
        }
    }

    @Override
    public void putGlass(Glass glass) {
        RxUtils.assertComputationThread();
        synchronized (mGlasses) {
            mGlasses.put(glass.id(), glass);
        }
    }

    @Override
    public SRM getSRM(int id) {
        RxUtils.assertComputationThread();
        synchronized (mSrms) {
            return mSrms.get(id);
        }
    }

    @Override
    public void putSRM(SRM srm) {
        RxUtils.assertComputationThread();
        synchronized (mSrms) {
            mSrms.put(srm.id(), srm);
        }
    }

    @Override
    public Style getStyle(int id) {
        RxUtils.assertComputationThread();
        synchronized (mStyles) {
            return mStyles.get(id);
        }
    }

    @Override
    public void putStyle(Style style) {
        RxUtils.assertComputationThread();
        synchronized (mStyles) {
            mStyles.put(style.id(), style);
        }
    }

    @Override
    public Brewery getBrewery(String id) {
        RxUtils.assertComputationThread();
        synchronized (mBreweries) {
            return mBreweries.get(id);
        }
    }

    @Override
    public void putBrewery(Brewery brewery) {
        RxUtils.assertComputationThread();
        synchronized (mBreweries) {
            mBreweries.put(brewery.id(), brewery);
        }
    }

    @Override
    public Place getPlace(String id) {
        RxUtils.assertComputationThread();
        synchronized (mPlaces) {
            return mPlaces.get(id);
        }
    }

    @Override
    public void putPlace(Place place) {
        RxUtils.assertComputationThread();
        synchronized (mPlaces) {
            mPlaces.put(place.id(), place);
        }
    }

    @Override
    public Country getCountry(String id) {
        RxUtils.assertComputationThread();
        synchronized (mCountries) {
            return mCountries.get(id);
        }
    }

    @Override
    public void putCountry(Country country) {
        RxUtils.assertComputationThread();
        synchronized (mCountries) {
            mCountries.put(country.isoCode(), country);
        }
    }

    @Override
    public Hop getHop(int id) {
        RxUtils.assertComputationThread();
        synchronized (mHops) {
            return mHops.get(id);
        }
    }

    @Override
    public void putHop(Hop hop) {
        RxUtils.assertComputationThread();
        synchronized (mHops) {
            mHops.put(hop.id(), hop);
        }
    }

    @Override
    public Yeast getYeast(int id) {
        RxUtils.assertComputationThread();
        synchronized (mYeasts) {
            return mYeasts.get(id);
        }
    }

    @Override
    public void putYeast(Yeast yeast) {
        RxUtils.assertComputationThread();
        synchronized (mYeasts) {
            mYeasts.put(yeast.id(), yeast);
        }
    }

    @Override
    public Malt getMalt(int id) {
        RxUtils.assertComputationThread();
        synchronized (mMalts) {
            return mMalts.get(id);
        }
    }

    @Override
    public void putMalt(Malt malt) {
        RxUtils.assertComputationThread();
        synchronized (mMalts) {
            mMalts.put(malt.id(), malt);
        }
    }
}
