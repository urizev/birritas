package com.urizev.birritas.app.providers.image;

import android.content.Context;
import android.widget.ImageView;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import okhttp3.OkHttpClient;

public class PicassoImageLoader implements ImageLoader{
    private final Picasso picasso;

    @Inject
    PicassoImageLoader(Context context, OkHttpClient client) {
        this.picasso = new Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(client))
                .build();
    }


    @Override
    public void load(String url, ImageView target) {
        this.picasso.load(url)
                .into(target);
    }
}
