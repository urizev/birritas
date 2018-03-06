package com.urizev.birritas.view.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.common.collect.ImmutableList;
import com.urizev.birritas.R;
import com.urizev.birritas.app.providers.image.ImageLoader;

/**
 * Creado por jcvallejo en 4/3/18.
 */

class BeerListWidgetAdapter implements RemoteViewsService.RemoteViewsFactory {
    private final ImmutableList<BeerListWidgetBeerViewState> mViewStates;
    private final Context mContext;
    private final ImageLoader mImageLoader;
    private RemoteViews remoteView;

    public BeerListWidgetAdapter(Context context, ImmutableList<BeerListWidgetBeerViewState> viewStates, ImageLoader imageLoader) {
        this.mContext = context.getApplicationContext();
        this.mViewStates = viewStates;
        this.mImageLoader = imageLoader;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return mViewStates.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        BeerListWidgetBeerViewState vs = mViewStates.get(i);

        RemoteViews remoteViews;
        remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.cell_beer_widget);
        remoteViews.setTextViewText(R.id.beer_title, vs.name());
        remoteViews.setTextViewText(R.id.beer_style, vs.style());
        remoteViews.setTextViewText(R.id.brewed_by, vs.brewedBy());
        remoteViews.setTextViewText(R.id.ibu_param_value, vs.ibu());
        remoteViews.setTextViewText(R.id.abv_param_value, vs.abv());
        Bitmap bitmap = mImageLoader.load(vs.imageUrl()).blockingFirst();
        remoteViews.setImageViewBitmap(R.id.beer_label, bitmap);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
