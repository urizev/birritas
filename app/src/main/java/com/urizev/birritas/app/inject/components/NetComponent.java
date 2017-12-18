package com.urizev.birritas.app.inject.components;

import com.urizev.birritas.app.inject.modules.AppModule;
import com.urizev.birritas.app.inject.modules.NetModule;
import com.urizev.birritas.view.featured.FeaturedFragment;
import com.urizev.birritas.view.nearby.NearbyFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules={AppModule.class, NetModule.class})
public interface NetComponent {
    void inject(FeaturedFragment fragment);
    void inject(NearbyFragment fragment);
}
