package com.urizev.birritas.view.widget;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViewsService;

import com.google.common.collect.ImmutableList;
import com.urizev.birritas.app.App;
import com.urizev.birritas.app.providers.image.ImageLoader;

import javax.inject.Inject;

public class BeerListWidgetRemoveViewService extends RemoteViewsService {
    public static final String VIEWSTATES = "viewStates";
    @Inject ImageLoader mImageLoader;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        App app = (App) getApplication();
        app.getNetComponent().inject(this);
        Bundle[] bundles = (Bundle[]) intent.getParcelableArrayExtra(VIEWSTATES);
        ImmutableList.Builder<BeerListWidgetBeerViewState> builder = new ImmutableList.Builder<>();
        for (Bundle bundle : bundles) {
            builder = builder.add(BeerListWidgetBeerViewState.fromBundle(bundle));
        }
        return new BeerListWidgetAdapter(getApplicationContext(), builder.build(), mImageLoader);
    }
}
