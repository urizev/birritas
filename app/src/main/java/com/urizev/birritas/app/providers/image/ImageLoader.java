package com.urizev.birritas.app.providers.image;

import android.widget.ImageView;

public interface ImageLoader {
    void load(String url, ImageView target);
}
