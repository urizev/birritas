package com.urizev.birritas.data.api.data.mappers;

import com.urizev.birritas.app.rx.RxUtils;
import com.urizev.birritas.data.api.data.ImageSetData;
import com.urizev.birritas.domain.entities.ImageSet;

class ImageSetMapper {
    public static ImageSet map(ImageSetData data) {
        RxUtils.assertComputationThread();

        if (data == null) {
            return null;
        }

        return ImageSet.create(data.icon(), data.medium(), data.large(), data.squareMedium(), data.squareLarge());
    }
}
