package com.urizev.birritas.view.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.common.collect.ImmutableList;
import com.urizev.birritas.R;
import com.urizev.birritas.app.providers.image.ImageLoader;
import com.urizev.birritas.view.beer.BeerActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Creado por jcvallejo en 4/3/18.
 */

class BeerListWidgetAdapter implements RemoteViewsService.RemoteViewsFactory {
    private final ImmutableList<BeerListWidgetBeerViewState> mViewStates;
    private final Context mContext;
    private final ImageLoader mImageLoader;

    BeerListWidgetAdapter(Context context, ImmutableList<BeerListWidgetBeerViewState> viewStates, ImageLoader imageLoader) {
        this.mContext = context.getApplicationContext();
        this.mViewStates = viewStates;
        this.mImageLoader = imageLoader;
        Timber.d("Viewstates: %d", viewStates.size());
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
    public RemoteViews getViewAt(int pos) {

        BeerListWidgetBeerViewState vs = mViewStates.get(pos);

        Timber.d("Rendering viewstate %d: %s", pos, vs);

        RemoteViews remoteViews;
        remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.cell_beer_widget);
        remoteViews.setTextViewText(R.id.beer_title, vs.name());
        remoteViews.setTextViewText(R.id.beer_style, vs.style());
        remoteViews.setTextViewText(R.id.brewed_by, vs.brewedBy());
        remoteViews.setTextViewText(R.id.ibu_param_value, vs.ibu());
        remoteViews.setTextViewText(R.id.abv_param_value, vs.abv());
        Bitmap bitmap = mImageLoader.loadCircle(vs.imageUrl())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.trampoline())
                .blockingFirst();
        remoteViews.setImageViewBitmap(R.id.beer_label, bitmap);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(BeerActivity.EXTRA_BEER_ID, vs.id());
        remoteViews.setOnClickFillInIntent(R.id.beer_cell, fillInIntent);


        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
