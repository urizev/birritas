package com.urizev.birritas.data.api.data.mappers;

import com.urizev.birritas.data.api.data.YeastData;
import com.urizev.birritas.data.cache.EntityCache;
import com.urizev.birritas.domain.entities.Yeast;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class YeastMapper {
    private final EntityCache cache;

    @Inject
    YeastMapper(EntityCache cache) {
        this.cache = cache;
    }

    public Yeast map(YeastData data) {
        int id = data.id();
        Yeast yeast = cache.getYeast(id);
        if (yeast == null) {
            yeast = Yeast.create(id, data.name(), data.description());
            cache.putYeast(yeast);
        }
        return yeast;
    }
}
