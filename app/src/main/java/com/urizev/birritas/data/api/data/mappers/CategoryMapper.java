package com.urizev.birritas.data.api.data.mappers;

import com.urizev.birritas.app.rx.RxUtils;
import com.urizev.birritas.data.api.data.CategoryData;
import com.urizev.birritas.data.cache.EntityCache;
import com.urizev.birritas.domain.entities.Category;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class CategoryMapper {
    private final EntityCache entityCache;

    @Inject
    CategoryMapper(EntityCache entityCache) {
        this.entityCache = entityCache;
    }

    public Category map(CategoryData data) {
        RxUtils.assertComputationThread();

        if (data == null) {
            return null;
        }

        int id = data.id();
        Category category = entityCache.getCategory(id);
        if (category == null) {
            category = Category.create(data.id(), data.name());
            entityCache.putCategory(category);
        }

        return category;
    }
}
