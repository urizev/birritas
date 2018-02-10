package com.urizev.birritas.domain.usecases;

import com.google.common.collect.ImmutableMap;
import com.urizev.birritas.data.api.ApiService;
import com.urizev.birritas.data.api.data.mappers.SearchMapper;
import com.urizev.birritas.domain.entities.SearchResult;

import java.net.URLEncoder;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class SearchUseCase extends UseCase<String,SearchResult> {
    private static final Map<String, String> SEARCH_PARAMS = ImmutableMap.of(
            ApiService.WITH_BREWERIES, ApiService.YES
    );
    private final ApiService mService;
    private SearchMapper mSearchMapper;

    @Inject
    public SearchUseCase(ApiService service, SearchMapper searchMapper) {
        this.mService = service;
        this.mSearchMapper = searchMapper;
    }

    @Override
    protected Observable<SearchResult> createObservable(String query) {
        return Observable.just(query)
                .map(s -> URLEncoder.encode(s, "UTF-8").replace("+", "%20"))
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .flatMap(s -> mService.search(s, SEARCH_PARAMS).subscribeOn(Schedulers.io()))
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .map(mSearchMapper::map);
    }
}
