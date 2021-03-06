package com.urizev.birritas.data.api.data.mappers;

import com.urizev.birritas.app.rx.RxUtils;
import com.urizev.birritas.data.api.data.StyleData;
import com.urizev.birritas.data.cache.EntityCache;
import com.urizev.birritas.domain.entities.Style;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class StyleMapper {
    private final EntityCache entityCache;
    private final CategoryMapper categoryMapper;

    @Inject
    StyleMapper(EntityCache entityCache, CategoryMapper categoryMapper) {
        this.entityCache = entityCache;
        this.categoryMapper = categoryMapper;
    }

    Style map(StyleData data) {
        RxUtils.assertComputationThread();

        if (data == null) {
            return null;
        }

        int id = data.id();
        Style style = entityCache.getStyle(id);
        if (style == null) {
            style = Style.create(data.id(), data.name(), data.shortName(), data.description(), categoryMapper.map(data.category()));
            entityCache.putStyle(style);
        }

        return style;
    }
}
