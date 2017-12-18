package com.urizev.birritas.data.mappers;

import com.urizev.birritas.app.rx.RxUtils;
import com.urizev.birritas.data.api.ApiService;
import com.urizev.birritas.data.cache.EntityCache;
import com.urizev.birritas.data.data.BeerData;
import com.urizev.birritas.domain.entities.Beer;

import javax.inject.Inject;

public class BeerMapper {
    private final EntityCache entityCache;
    private final BreweryMapper breweryMapper;
    private final SRMMapper srmMapper;
    private final GlassMapper glassMapper;
    private final StyleMapper styleMapper;

    @Inject
    BeerMapper(EntityCache entityCache, BreweryMapper breweryMapper, SRMMapper srmMapper, GlassMapper glassMapper, StyleMapper styleMapper) {
        this.entityCache = entityCache;
        this.breweryMapper = breweryMapper;
        this.srmMapper = srmMapper;
        this.glassMapper = glassMapper;
        this.styleMapper = styleMapper;
    }

    public Beer map(BeerData data) {
        RxUtils.assertComputationThread();

        Beer beer = entityCache.getBeer(data.id());
        if (beer == null) {
            beer = Beer.builder()
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
                    .labels(ImageSetMapper.map(data.labels()))
                    .build();
            entityCache.putBeer(beer);
        }

        return beer;
    }
}
