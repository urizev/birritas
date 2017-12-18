package com.urizev.birritas.data.mappers;

import com.urizev.birritas.data.data.ImageSetData;
import com.urizev.birritas.domain.entities.ImageSet;

class ImageSetMapper {
    public static ImageSet map(ImageSetData data) {
        if (data == null) {
            return null;
        }

        return ImageSet.create(data.icon(), data.medium(), data.large(), data.squareMedium(), data.squareLarge());
    }
}
