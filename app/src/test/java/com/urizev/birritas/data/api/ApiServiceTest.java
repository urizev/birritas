package com.urizev.birritas.data.api;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.squareup.moshi.Moshi;
import com.urizev.birritas.data.api.adapters.ApiAdapterFactory;
import com.urizev.birritas.data.api.adapters.ImmutableListJsonAdapter;
import com.urizev.birritas.data.api.adapters.LiveDataJsonAdapter;
import com.urizev.birritas.data.api.data.BeerData;
import com.urizev.birritas.data.api.data.BreweryData;
import com.urizev.birritas.data.api.data.PlaceData;
import com.urizev.birritas.data.api.data.LiveData;
import com.urizev.birritas.data.api.data.ResultData;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ApiServiceTest {
    private static final int MAX_PAGE_RESULTS = 50;
    private static final String STATUS_SUCCESS = "success";
    private static final String BEER_ID = "cBLTUw";
    private static final String BREWERY_ID = "YXDiJk";
    private static final String LATITUDE = "35.772096";
    private static final String LONGITUDE = "-78.638614";
    private static final String MILES = "10";
    private static final String SEARCH_QUERY = "IPA";

    private ApiService api;

    @Before
    public void setUp() throws Exception {
        this.api = new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(new Moshi.Builder()
                        .add(ApiAdapterFactory.create())
                        .add(ImmutableListJsonAdapter.FACTORY)
                        .add(LiveDataJsonAdapter.FACTORY)
                        .build()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.trampoline()))
                .client(new OkHttpClient.Builder()
                        .addInterceptor(new ApiKeyInterceptor())
                        .addInterceptor(new HttpLoggingInterceptor()
                                .setLevel(HttpLoggingInterceptor.Level.BASIC))
                        .build())
                .build()
                .create(ApiService.class);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void getBeers() throws Exception {
        TestObserver<ResultData<ImmutableList<BeerData>>> observer = new TestObserver<>();
        Observable<ResultData<ImmutableList<BeerData>>> observable = this.api.getBeers(Collections.emptyMap());
        observable.subscribe(observer);
        observer.assertComplete();
        observer.assertNoErrors();
        observer.assertValueCount(1);
        ResultData<ImmutableList<BeerData>> result = observer.values().get(0);
        assertNotNull(result);
        assertEquals(STATUS_SUCCESS, result.status());
        assertEquals(1, result.currentPage().intValue());
        assertTrue(result.numberOfPages() > 1);
        assertTrue(result.totalResults() > 1);
        assertEquals(MAX_PAGE_RESULTS, result.data().size());
    }

    @Test
    public void getBeer() throws Exception {
        TestObserver<ResultData<BeerData>> observer = new TestObserver<>();
        Observable<ResultData<BeerData>> observable = this.api.getBeer(BEER_ID);
        observable.subscribe(observer);
        observer.assertComplete();
        observer.assertNoErrors();
        observer.assertValueCount(1);
        ResultData<BeerData> result = observer.values().get(0);
        assertNotNull(result);
        assertEquals(STATUS_SUCCESS, result.status());
        assertNull(result.currentPage());
        assertNull(result.numberOfPages());
        assertNull(result.totalResults());
    }

    // /search/geo/point?lat=35.772096&lng=-78.638614
    @SuppressWarnings("ConstantConditions")
    @Test
    public void searchGeopoint() {
        TestObserver<ResultData<ImmutableList<PlaceData>>> observer = new TestObserver<>();
        ImmutableMap<String, String> params = ImmutableMap.of(ApiService.LATITUDE, LATITUDE, ApiService.LONGITUDE, LONGITUDE, ApiService.RADIUS, MILES);
        Observable<ResultData<ImmutableList<PlaceData>>> observable = this.api.searchGeoPoint(params);
        observable.subscribe(observer);
        observer.assertComplete();
        observer.assertNoErrors();
        observer.assertValueCount(1);
        ResultData<ImmutableList<PlaceData>> result = observer.values().get(0);
        assertNotNull(result);
        assertEquals(STATUS_SUCCESS, result.status());
        assertNotNull(result.currentPage());
        assertEquals(1, result.currentPage().intValue());
        assertNotNull(result.numberOfPages());
        assertTrue(result.numberOfPages() > 0);
        assertNotNull(result.totalResults());
        assertTrue(result.totalResults() > 0);
    }

    // /search
    @SuppressWarnings("ConstantConditions")
    @Test
    public void liveSearch() {
        TestObserver<ResultData<LiveData>> observer = new TestObserver<>();
        ImmutableMap<String, String> params = ImmutableMap.of(ApiService.QUERY, SEARCH_QUERY);
        Observable<ResultData<LiveData>> observable = this.api.search(params);
        observable.subscribe(observer);
        observer.assertComplete();
        observer.assertNoErrors();
        observer.assertValueCount(1);
        ResultData<LiveData> result = observer.values().get(0);
        assertNotNull(result);
        assertEquals(STATUS_SUCCESS, result.status());
        assertNotNull(result.currentPage());
        assertEquals(1, result.currentPage().intValue());
        assertNotNull(result.numberOfPages());
        assertTrue(result.numberOfPages() > 0);
        assertNotNull(result.totalResults());
        assertTrue(result.totalResults() > 0);
        assertNotNull(result.data());
        assertNotNull(result.data().beers());
        assertNotNull(result.data().breweries());
    }

    // /brewery/<id>
    @Test
    public void getBrewery() {
        TestObserver<ResultData<BreweryData>> observer = new TestObserver<>();
        Observable<ResultData<BreweryData>> observable = this.api.getBrewery(BREWERY_ID);
        observable.subscribe(observer);
        observer.assertComplete();
        observer.assertNoErrors();
        observer.assertValueCount(1);
        ResultData<BreweryData> result = observer.values().get(0);
        assertNotNull(result);
        assertEquals(STATUS_SUCCESS, result.status());
        assertNull(result.currentPage());
        assertNull(result.numberOfPages());
        assertNull(result.totalResults());
    }
}