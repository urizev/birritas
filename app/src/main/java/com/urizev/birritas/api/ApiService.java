package com.urizev.birritas.api;

import com.google.common.collect.ImmutableList;
import com.urizev.birritas.api.data.BeerData;
import com.urizev.birritas.api.data.BreweryData;
import com.urizev.birritas.api.data.GeopointData;
import com.urizev.birritas.api.data.LiveData;
import com.urizev.birritas.api.data.ResultData;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface ApiService {
    String BASE_URL = "http://api.brewerydb.com/v2/";
    String LATITUDE = "lat";
    String LONGITUDE = "lng";
    String RADIUS = "radius";
    String UNIT = "unit";
    String QUERY = "q";

    @GET("beers")
    Observable<ResultData<ImmutableList<BeerData>>> getBeers(@QueryMap Map<String,String> params);

    @GET("beer/{beerId}")
    Observable<ResultData<BeerData>> getBeer(@Path("beerId") String beerId);

    @GET("search/geo/point")
    Observable<ResultData<ImmutableList<GeopointData>>> searchGeoPoint(@QueryMap Map<String,String> params);

    @GET("search")
    Observable<ResultData<LiveData>> search(@QueryMap Map<String,String> params);

    @GET("brewery/{breweryId}")
    Observable<ResultData<BreweryData>> getBrewery(@Path("breweryId") String beerId);
}
