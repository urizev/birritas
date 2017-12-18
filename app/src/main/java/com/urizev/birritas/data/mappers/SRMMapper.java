package com.urizev.birritas.data.mappers;

import android.graphics.Color;

import com.urizev.birritas.data.cache.EntityCache;
import com.urizev.birritas.data.data.SRMData;
import com.urizev.birritas.domain.entities.SRM;

import javax.inject.Inject;

import timber.log.Timber;

class SRMMapper {
    private static final String OVER_PREFIX = "Over ";

    private final EntityCache entityCache;

    @Inject
    SRMMapper(EntityCache entityCache) {
        this.entityCache = entityCache;
    }

    SRM map(SRMData data) {
        if (data == null) {
            return null;
        }

        int id = data.id();
        SRM srm = entityCache.getSRM(id);
        if (srm == null) {
            String name = data.name();
            boolean over = false;
            if (name.startsWith(OVER_PREFIX)) {
                over = true;
                name = name.replace(OVER_PREFIX, "");
            }
            int value = Integer.parseInt(name);
            Timber.d("Parsing color: %s", data.hex());
            srm = SRM.create(data.id(), value, over, Color.parseColor("#" + data.hex()));
            entityCache.putSRM(srm);
        }

        return srm;
    }
}
