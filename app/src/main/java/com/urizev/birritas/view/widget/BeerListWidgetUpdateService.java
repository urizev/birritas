package com.urizev.birritas.view.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.google.common.collect.ImmutableList;
import com.urizev.birritas.R;
import com.urizev.birritas.app.App;
import com.urizev.birritas.app.providers.resources.ResourceProvider;
import com.urizev.birritas.domain.entities.Beer;
import com.urizev.birritas.domain.entities.Brewery;
import com.urizev.birritas.domain.entities.ImageSet;
import com.urizev.birritas.domain.entities.Style;
import com.urizev.birritas.domain.usecases.FavoriteBeersUseCase;
import com.urizev.birritas.domain.usecases.FeaturedBeersUseCase;
import com.urizev.birritas.view.beer.BeerActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.Observable;

import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;
import static android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID;

public class BeerListWidgetUpdateService extends IntentService {
    @Inject ResourceProvider mResourceProvider;
    @Inject FeaturedBeersUseCase mFeaturedBeersUseCase;
    @Inject FavoriteBeersUseCase mFavoriteBeersUseCase;
    private String mNa;

    public BeerListWidgetUpdateService() {
        super("BeerListWidgetUpdateService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        App app = (App) getApplication();
        app.getNetComponent().inject(this);
        mNa = mResourceProvider.getString(R.string.n_a);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AppWidgetManager manager = AppWidgetManager.getInstance(this);

        int widgetId = intent.getIntExtra(EXTRA_APPWIDGET_ID, INVALID_APPWIDGET_ID);
        if (widgetId == INVALID_APPWIDGET_ID) {
            return;
        }

        Observable<ImmutableList<Beer>> useCase;
        int type = BeerListWidgetConfigureActivity.loadPref (this, widgetId);
        switch (type) {
            case R.string.title_favorites:
                useCase = mFavoriteBeersUseCase.execute(null);
                break;
            case R.string.title_featured:
                useCase = mFeaturedBeersUseCase.execute(null);
                break;
            default:
                return;
        }

        RemoteViews views = new RemoteViews(getPackageName(), R.layout.beer_list_widget);
        String title = getString(type);
        Intent remoteIntent = useCase
                .map(this::beersToBundles)
                .map(bundles -> {
                    Intent i = new Intent(this, BeerListWidgetRemoveViewService.class);
                    i.putExtra(BeerListWidgetRemoveViewService.VIEWSTATES, bundles);
                    return i;
                })
                .blockingFirst();

        views.setTextViewText(R.id.widget_title, title);
        Intent intentTemplate = new Intent(this, BeerActivity.class);
        PendingIntent startActivityPendingIntent = PendingIntent.getActivity(this, 0, intentTemplate, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_list, startActivityPendingIntent);
        views.setRemoteAdapter(R.id.widget_list, remoteIntent);

        manager.notifyAppWidgetViewDataChanged(widgetId, R.id.widget_list);
        manager.updateAppWidget(widgetId, views);
    }

    private Bundle[] beersToBundles(ImmutableList<Beer> beers) {
        Bundle[] bundles = new Bundle[beers.size()];
        int index = 0;
        for (Beer beer : beers) {
            ImageSet labels = beer.labels();
            String icon = labels != null ? labels.icon() : null;

            String ibuValue = beer.ibu() == null ? mNa : String.format(Locale.getDefault(), "%.1f", beer.ibu());
            String abvValue = beer.abv() == null ? mNa : String.format(Locale.getDefault(), "%.1f", beer.abv());
            String brewedBy = "";
            ImmutableList<Brewery> breweries = beer.breweries();
            if (breweries != null && !breweries.isEmpty()) {
                List<String> breweryNames = new ArrayList<>(breweries.size());
                for(Brewery brewery : breweries) {
                    String name = brewery.shortName();
                    if (name == null) {
                        name = brewery.name();
                    }
                    breweryNames.add(name);
                }
                brewedBy = TextUtils.join(", ", breweryNames);
            }

            String styleName = "";
            Style style = beer.style();
            if (style != null) {
                styleName = style.shortName();
                if (styleName == null) {
                    styleName = style.name();
                }
            }

            bundles[index++] = BeerListWidgetBeerViewState.create(beer.id(), beer.name(), icon, brewedBy, styleName, abvValue, ibuValue).toBundle();
        }

        return bundles;
    }
}
