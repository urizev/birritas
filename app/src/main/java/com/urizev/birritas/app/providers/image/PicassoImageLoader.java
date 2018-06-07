package com.urizev.birritas.app.providers.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;
import com.urizev.birritas.R;
import com.urizev.birritas.app.rx.SimpleDisposable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;

@Singleton
public class PicassoImageLoader implements ImageLoader{
    private static final Transformation CROP_WHITE_PADDING_TRANSFORMER = new CropWhitePaddingTransformer();
    private final Picasso picasso;
    private Transformation circleTransformer;

    @Inject
    PicassoImageLoader(Context context, OkHttpClient client) {
        // TODO - Uses OkHttpClient
        this.picasso = new Picasso.Builder(context).build();
        this.circleTransformer = new CircleImageTransformer();
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

    @Override
    public Observable<Bitmap> load(String imageUrl) {
        return Observable.create(e -> {
            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    if (e.isDisposed()) {
                        return;
                    }

                    e.onNext(bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    if (e.isDisposed()) {
                        return;
                    }
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            e.setDisposable(new SimpleDisposable() {
                @Override
                protected void onDisposed() {}
            });
            picasso.load(imageUrl).into(target);
        });
    }

    @Override
    public Observable<Bitmap> loadCircle(String imageUrl) {
        return Observable.create(e -> {
            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    if (e.isDisposed()) {
                        return;
                    }

                    e.onNext(bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    if (e.isDisposed()) {
                        return;
                    }
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            e.setDisposable(new SimpleDisposable() {
                @Override
                protected void onDisposed() {}
            });
            picasso.load(imageUrl)
                    .transform(circleTransformer)
                    .into(target);
        });    }

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

    public static class CircleImageTransformer implements Transformation {
        @Inject
        public CircleImageTransformer() {}

        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }
}
