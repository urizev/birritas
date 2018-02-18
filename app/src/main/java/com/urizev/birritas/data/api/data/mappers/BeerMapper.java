package com.urizev.birritas.data.api.data.mappers;

import com.google.common.collect.ImmutableList;
import com.urizev.birritas.app.rx.RxUtils;
import com.urizev.birritas.data.api.ApiService;
import com.urizev.birritas.data.api.data.BeerData;
import com.urizev.birritas.data.cache.EntityCache;
import com.urizev.birritas.domain.entities.Beer;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BeerMapper {
    private final EntityCache entityCache;
    private final BreweryMapper breweryMapper;
    private final SRMMapper srmMapper;
    private final GlassMapper glassMapper;
    private final StyleMapper styleMapper;
    private final IngredientsMapper ingredientsMapper;

    @Inject
    BeerMapper(EntityCache entityCache,
               BreweryMapper breweryMapper,
               SRMMapper srmMapper,
               GlassMapper glassMapper,
               StyleMapper styleMapper,
               IngredientsMapper ingredientsMapper) {
        this.entityCache = entityCache;
        this.breweryMapper = breweryMapper;
        this.srmMapper = srmMapper;
        this.glassMapper = glassMapper;
        this.styleMapper = styleMapper;
        this.ingredientsMapper = ingredientsMapper;
    }


    public ImmutableList<Beer> map(ImmutableList<BeerData> data) {
        RxUtils.assertComputationThread();

        if (data == null) {
            return null;
        }

        ImmutableList.Builder<Beer> builder = new ImmutableList.Builder<>();

        for (BeerData datum : data) {
            Beer beer = this.map(datum);
            if (beer != null) {
                builder = builder.add(beer);
            }
        }

        return builder.build();
    }

    public Beer map(BeerData data) {
        RxUtils.assertComputationThread();

        Beer beer = entityCache.getBeer(data.id());
        Beer.Builder builder;
        boolean saveToCache = false;
        if (beer == null) {
            saveToCache = true;
            builder = Beer.builder()
                    .id(data.id())
                    .name(data.name())
                    .description(data.description())
                    .isOrganic(data.isOrganic().equals(ApiService.YES))
                    .srm(srmMapper.map(data.srm()))
                    .abv(CommonMapper.mapFloat(data.abv()))
                    .ibu(CommonMapper.mapFloat(data.ibu()))
                    .status(CommonMapper.mapStatus(data.status()))
                    .glass(glassMapper.map(data.glass()))
                    .style(styleMapper.map(data.style()))
                    .breweries(breweryMapper.map(data.breweries()))
                    .ingredients(ingredientsMapper.map(data.ingredients()))
                    .labels(ImageSetMapper.map(data.labels()));
        } else {
            builder = beer.toBuilder();
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
            if (data.srm() != null) {
                saveToCache = true;
                builder = builder.srm(srmMapper.map(data.srm()));
            }
            if (data.abv() != null) {
                saveToCache = true;
                builder = builder.abv(CommonMapper.mapFloat(data.abv()));
            }
            if (data.ibu() != null) {
                saveToCache = true;
                builder = builder.ibu(CommonMapper.mapFloat(data.ibu()));
            }
            if (data.status() != null) {
                saveToCache = true;
                builder = builder.status(CommonMapper.mapStatus(data.status()));
            }
            if (data.glass() != null) {
                saveToCache = true;
                builder = builder.glass(glassMapper.map(data.glass()));
            }
            if (data.style() != null) {
                saveToCache = true;
                builder = builder.style(styleMapper.map(data.style()));
            }
            if (data.breweries() != null) {
                saveToCache = true;
                builder = builder.breweries(breweryMapper.map(data.breweries()));
            }
            if (data.labels() != null) {
                saveToCache = true;
                builder = builder.labels(ImageSetMapper.map(data.labels()));
            }
            if (data.ingredients() != null) {
                saveToCache = true;
                builder = builder.ingredients(ingredientsMapper.map(data.ingredients()));
            }
        }

        if (saveToCache) {
            beer = builder.build();
            entityCache.putBeer(beer);
        }

        return beer;
    }
}
