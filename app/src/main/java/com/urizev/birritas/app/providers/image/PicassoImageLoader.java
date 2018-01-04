package com.urizev.birritas.app.providers.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.urizev.birritas.R;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.OkHttpClient;

@Singleton
public class PicassoImageLoader implements ImageLoader{
    private static final Transformation CROP_WHITE_PADDING_TRANSFORMER = new CropWhitePaddingTransformer();
    private final Picasso picasso;

    @Inject
    PicassoImageLoader(Context context, OkHttpClient client) {
        // TODO - Uses OkHttpClient
        this.picasso = new Picasso.Builder(context)
                .build();
    }


    @Override
    public void load(String url, ImageView target) {
        this.picasso
                .load(url)
                .transform(CROP_WHITE_PADDING_TRANSFORMER)
                .placeholder(R.color.colorImagePlaceholder)
                .error(android.R.color.black)
                .into(target);
    }

    private static class CropWhitePaddingTransformer implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int minX = Integer.MAX_VALUE;
            int maxX = 0;
            int minY = Integer.MAX_VALUE;
            int maxY = 0;

            for (int x = 0; x < source.getWidth(); x++) {
                for (int y = 0; y < source.getHeight(); y++) {
                    if (source.getPixel(x, y) != Color.WHITE) {
                        if (x < minX) {
                            minX = x;
                        }
                        if (x > maxX) {
                            maxX = x;
                        }
                        if (y < minY) {
                            minY = y;
                        }
                        if (y > maxY) {
                            maxY = y;
                        }
                    }
                }
            }
            Bitmap croppedBitmap = Bitmap.createBitmap(source, minX, minY, maxX - minX + 1, maxY - minY + 1);

            if (source != croppedBitmap) {
                source.recycle();
            }
            return croppedBitmap;
        }

        @Override
        public String key() {
            return "crop-white-padding";
        }
    }
}
