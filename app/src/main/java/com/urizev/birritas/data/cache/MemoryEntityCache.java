package com.urizev.birritas.data.cache;


import android.support.v4.util.ArrayMap;
import android.util.SparseArray;

import com.urizev.birritas.app.rx.RxUtils;
import com.urizev.birritas.domain.entities.Beer;
import com.urizev.birritas.domain.entities.Brewery;
import com.urizev.birritas.domain.entities.Category;
import com.urizev.birritas.domain.entities.Country;
import com.urizev.birritas.domain.entities.Glass;
import com.urizev.birritas.domain.entities.Place;
import com.urizev.birritas.domain.entities.SRM;
import com.urizev.birritas.domain.entities.Style;

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
    }

    @Override
    public Beer getBeer(String id) {
        RxUtils.assertComputationThread();

        return this.mBeers.get(id);
    }

    @Override
    public void putBeer(Beer beer) {
        RxUtils.assertComputationThread();
        mBeers.put(beer.id(), beer);
    }

    @Override
    public Category getCategory(int id) {
        RxUtils.assertComputationThread();
        return mCategories.get(id);
    }

    @Override
    public void putCategory(Category category) {
        RxUtils.assertComputationThread();
        mCategories.put(category.id(), category);
    }

    @Override
    public Glass getGlass(int id) {
        RxUtils.assertComputationThread();
        return mGlasses.get(id);
    }

    @Override
    public void putGlass(Glass glass) {
        RxUtils.assertComputationThread();
        mGlasses.put(glass.id(), glass);
    }

    @Override
    public SRM getSRM(int id) {
        RxUtils.assertComputationThread();
        return mSrms.get(id);
    }

    @Override
    public void putSRM(SRM srm) {
        RxUtils.assertComputationThread();
        mSrms.put(srm.id(), srm);
    }

    @Override
    public Style getStyle(int id) {
        RxUtils.assertComputationThread();
        return mStyles.get(id);
    }

    @Override
    public void putStyle(Style style) {
        RxUtils.assertComputationThread();
        mStyles.put(style.id(), style);
    }

    @Override
    public Brewery getBrewery(String id) {
        RxUtils.assertComputationThread();
        return mBreweries.get(id);
    }

    @Override
    public void putBrewery(Brewery brewery) {
        RxUtils.assertComputationThread();
        mBreweries.put(brewery.id(), brewery);
    }

    @Override
    public Place getPlace(String id) {
        RxUtils.assertComputationThread();
        return mPlaces.get(id);
    }

    @Override
    public void putPlace(Place place) {
        RxUtils.assertComputationThread();
        mPlaces.put(place.id(), place);
    }

    @Override
    public Country getCountry(String id) {
        RxUtils.assertComputationThread();
        return mCountries.get(id);
    }

    @Override
    public void putCountry(Country country) {
        RxUtils.assertComputationThread();
        mCountries.put(country.isoCode(), country);
    }
}
