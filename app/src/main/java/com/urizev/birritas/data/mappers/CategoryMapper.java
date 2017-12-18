package com.urizev.birritas.data.mappers;

import com.urizev.birritas.data.cache.EntityCache;
import com.urizev.birritas.data.data.CategoryData;
import com.urizev.birritas.domain.entities.Category;

import javax.inject.Inject;

class CategoryMapper {
    private final EntityCache entityCache;

    @Inject
    CategoryMapper(EntityCache entityCache) {
        this.entityCache = entityCache;
    }

    public Category map(CategoryData data) {
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
