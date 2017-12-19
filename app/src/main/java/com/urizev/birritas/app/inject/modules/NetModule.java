package com.urizev.birritas.app.inject.modules;

import android.content.Context;

import com.squareup.moshi.Moshi;
import com.urizev.birritas.app.providers.image.ImageLoader;
import com.urizev.birritas.app.providers.image.PicassoImageLoader;
import com.urizev.birritas.app.rx.RxUtils;
import com.urizev.birritas.data.api.ApiKeyInterceptor;
import com.urizev.birritas.data.api.ApiService;
import com.urizev.birritas.data.api.adapters.ApiAdapterFactory;
import com.urizev.birritas.data.api.adapters.ImmutableListJsonAdapter;
import com.urizev.birritas.data.api.adapters.LiveDataJsonAdapter;
import com.urizev.birritas.data.cache.EntityCache;
import com.urizev.birritas.data.cache.MemoryEntityCache;
import com.urizev.birritas.data.repositories.DefaultBeerRepository;
import com.urizev.birritas.domain.repositories.BeerRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;
import timber.log.Timber;

@Module
public class NetModule {
    private static final long MAX_CACHE_SIZE = 10 * 1024 * 1024;

    @Provides
    @Singleton
    BeerRepository providesBeerRepository(DefaultBeerRepository repository) {
        return repository;
    }

    @Provides
    @Singleton
    ImageLoader providesImageLoader(PicassoImageLoader loader) {
        return loader;
    }

    @Provides
    @Singleton
    Interceptor provideLoggingInterceptor() {
        return new HttpLoggingInterceptor(message -> Timber.tag("okhttp").d(message))
                .setLevel(HttpLoggingInterceptor.Level.BASIC);
    }

    @Provides
    @Singleton
    Cache providesCache(Context context) {
        return new Cache(context.getCacheDir(), MAX_CACHE_SIZE);
    }

    @Provides
    @Singleton
    OkHttpClient providesOkHttpClient(Cache cache, Interceptor loggingInterceptor) {
        return new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(new ApiKeyInterceptor())
                .addInterceptor(RxUtils.interceptor())
                .addInterceptor(loggingInterceptor)
                .build();
    }

    @Provides
    @Singleton
    Retrofit providesRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(MoshiConverterFactory.create(new Moshi.Builder()
                        .add(ApiAdapterFactory.create())
                        .add(LiveDataJsonAdapter.FACTORY)
                        .add(ImmutableListJsonAdapter.FACTORY)
                        .build()))
                .client(client)
                .build();
    }

    @Provides
    @Singleton
    ApiService providesApiService(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }

    @Provides
    @Singleton
    EntityCache providesEntityCache(MemoryEntityCache entityCache) {
        return entityCache;
    }
}
