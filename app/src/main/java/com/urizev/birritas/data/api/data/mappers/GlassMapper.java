package com.urizev.birritas.data.api.data.mappers;

import com.urizev.birritas.app.rx.RxUtils;
import com.urizev.birritas.data.api.data.GlassData;
import com.urizev.birritas.data.cache.EntityCache;
import com.urizev.birritas.domain.entities.Glass;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class GlassMapper {
    private final EntityCache entityCache;

    @Inject
    GlassMapper(EntityCache entityCache) {
        this.entityCache = entityCache;
    }

    Glass map(GlassData data) {
        RxUtils.assertComputationThread();

        if (data == null) {
            return null;
        }

        int id = data.id();
        Glass glass = entityCache.getGlass(id);
        if (glass == null) {
            glass = Glass.create(data.id(), data.name());
            entityCache.putGlass(glass);
        }

        return glass;
    }
}
