package com.urizev.birritas.app.inject.components;

import com.urizev.birritas.app.base.BaseActivity;
import com.urizev.birritas.app.inject.modules.AppModule;
import com.urizev.birritas.app.inject.modules.NetModule;
import com.urizev.birritas.view.beer.BeerFragment;
import com.urizev.birritas.view.brewery.BreweryFragment;
import com.urizev.birritas.view.favorites.FavoriteBeersFragment;
import com.urizev.birritas.view.featured.FeaturedFragment;
import com.urizev.birritas.view.nearby.NearbyFragment;
import com.urizev.birritas.view.search.SearchFragment;
import com.urizev.birritas.view.widget.BeerListWidgetRemoveViewService;
import com.urizev.birritas.view.widget.BeerListWidgetUpdateService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules={AppModule.class, NetModule.class})
public interface NetComponent {
    void inject(FeaturedFragment fragment);
    void inject(NearbyFragment fragment);
    void inject(FavoriteBeersFragment fragment);
    void inject(BeerFragment fragment);
    void inject(BreweryFragment fragment);
    void inject(SearchFragment fragment);

    void inject(BaseActivity activity);

    void inject(BeerListWidgetRemoveViewService service);
    void inject(BeerListWidgetUpdateService service);
}
