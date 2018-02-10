package com.urizev.birritas.data.api.data.mappers;

import com.urizev.birritas.data.api.data.MaltData;
import com.urizev.birritas.data.cache.EntityCache;
import com.urizev.birritas.domain.entities.Malt;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MaltMapper {
    private final EntityCache cache;

    @Inject
    MaltMapper(EntityCache cache) {
        this.cache = cache;
    }

    public Malt map(MaltData data) {
        int id = data.id();
        Malt malt = cache.getMalt(id);
        if (malt == null) {
            malt = Malt.create(id, data.name(), data.description());
            cache.putMalt(malt);
        }
        return malt;
    }
}
