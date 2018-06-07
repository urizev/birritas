package com.urizev.birritas.app.providers.image;

import android.graphics.Bitmap;
import android.widget.ImageView;

import io.reactivex.Observable;


public interface ImageLoader {
    void load(String url, ImageView target);

    Observable<Bitmap> load(String imageUrl);
    Observable<Bitmap> loadCircle(String imageUrl);
}
