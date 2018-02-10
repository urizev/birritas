package com.urizev.birritas.data.api.data.mappers;

import com.urizev.birritas.data.api.data.HopData;
import com.urizev.birritas.data.cache.EntityCache;
import com.urizev.birritas.domain.entities.Hop;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HopMapper {
    private final EntityCache cache;

    @Inject
    HopMapper(EntityCache cache) {
        this.cache = cache;
    }

    public Hop map(HopData data) {
        int id = data.id();
        Hop hop = cache.getHop(id);
        if (hop == null) {
            hop = Hop.create(id, data.name(), data.description());
            cache.putHop(hop);
        }
        return hop;
    }
}
