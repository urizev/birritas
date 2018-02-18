package com.urizev.birritas.data.api;

import com.google.common.collect.ImmutableList;
import com.urizev.birritas.data.api.data.BeerData;
import com.urizev.birritas.data.api.data.BreweryData;
import com.urizev.birritas.data.api.data.PlaceData;
import com.urizev.birritas.data.api.data.LiveData;
import com.urizev.birritas.data.api.data.ResultData;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiService {
    String BASE_URL = "http://api.brewerydb.com/v2/";
    String LATITUDE = "lat";
    String LONGITUDE = "lng";
    String RADIUS = "radius";
    String UNIT = "unit";
    String KM = "km";
    String QUERY = "q";
    String HAS_LABELS = "hasLabels";
    String YES = "Y";
    String ORDER = "order";
    String ORDER_UPDATE_DATE = "updateDate";
    String SORT = "sort";
    String SORT_DESC = "DESC";
    String STATUS_VERIFIED = "verified";
    String WITH_BREWERIES = "withBreweries";
    String WITH_INGREDIENTS = "withIngredients";
    String WITH_SOCIAL_ACCOUNTS = "withSocialAccounts";
    String WITH_LOCATIONS = "withLocations";

    @GET("beers")
    Observable<ResultData<ImmutableList<BeerData>>> getBeers(@QueryMap Map<String,String> params);

    @GET("beer/{beerId}")
    Observable<ResultData<BeerData>> getBeer(@Path("beerId") String beerId, @QueryMap Map<String,String> params);

    @GET("search/geo/point")
    Observable<ResultData<ImmutableList<PlaceData>>> searchGeoPoint(@QueryMap Map<String,String> params);

    @GET("search")
    Observable<ResultData<LiveData>> search(@Query(value = QUERY, encoded = true) String query, @QueryMap Map<String,String> params);

    @GET("brewery/{breweryId}")
    Observable<ResultData<BreweryData>> getBrewery(@Path("breweryId") String breweryId, @QueryMap Map<String, String> params);

    @GET("brewery/{breweryId}/beers")
    Observable<ResultData<ImmutableList<BeerData>>> getBreweryBeers(@Path("breweryId") String breweryId);
}
