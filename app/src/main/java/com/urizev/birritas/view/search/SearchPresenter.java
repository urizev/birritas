package com.urizev.birritas.view.search;

import com.google.common.collect.ImmutableList;
import com.urizev.birritas.R;
import com.urizev.birritas.app.providers.resources.ResourceProvider;
import com.urizev.birritas.app.rx.RxUtils;
import com.urizev.birritas.domain.entities.Beer;
import com.urizev.birritas.domain.entities.Brewery;
import com.urizev.birritas.domain.entities.ImageSet;
import com.urizev.birritas.domain.entities.Place;
import com.urizev.birritas.domain.entities.SearchResult;
import com.urizev.birritas.domain.usecases.SearchUseCase;
import com.urizev.birritas.view.common.Presenter;
import com.urizev.birritas.view.common.ViewState;
import com.urizev.birritas.view.common.adapter.HeaderViewStateAdapterDelegate;
import com.urizev.birritas.view.search.adapter.SearchResultItemViewStateAdapterDelegate;

import java.util.Locale;

import io.reactivex.disposables.Disposable;

class SearchPresenter extends Presenter<SearchViewState> {
    private final SearchUseCase mUseCase;
    private final String mBeersHeader;
    private final String mBreweriesHeader;
    private Disposable mSearchDisposable;

    SearchPresenter(SearchUseCase useCase, ResourceProvider resourceProvider) {
        this.mUseCase = useCase;
        this.mBeersHeader = resourceProvider.getString(R.string.header_beers).toUpperCase(Locale.getDefault());
        this.mBreweriesHeader = resourceProvider.getString(R.string.header_breweries).toUpperCase(Locale.getDefault());
    }

    public void search(String query) {
        RxUtils.assertComputationThread();

        if (mSearchDisposable != null) {
            mSearchDisposable.dispose();
        }

        mSearchDisposable = mUseCase.execute(query)
                .map(this::resultToViewState)
                .doOnNext(this::publishViewState)
                .subscribe();
    }

    @Override
    protected void dispose() {
        mSearchDisposable.dispose();
        super.dispose();
    }

    private SearchViewState resultToViewState(SearchResult result) {
        RxUtils.assertComputationThread();

        ImmutableList.Builder<ViewState> viewStates = new ImmutableList.Builder<>();

        if (!result.beers().isEmpty()) {
            viewStates = viewStates.add(HeaderViewStateAdapterDelegate.ViewState.create(mBeersHeader));
            for (Beer beer : result.beers()) {
                String imageUrl = null;
                ImageSet labels = beer.labels();
                if (labels != null) {
                    imageUrl = labels.icon();
                }
                String subtitle = null;
                ImmutableList<Brewery> breweries = beer.breweries();
                if (breweries != null  && !breweries.isEmpty()) {
                    subtitle = breweries.get(0).name();
                }
                viewStates = viewStates.add(SearchResultItemViewStateAdapterDelegate.ViewState.create(imageUrl, beer.name(), subtitle));
            }
        }

        if (!result.breweries().isEmpty()) {
            viewStates = viewStates.add(HeaderViewStateAdapterDelegate.ViewState.create(mBreweriesHeader));
            for (Brewery brewery : result.breweries()) {
                String imageUrl = null;
                ImageSet labels = brewery.images();
                if (labels != null) {
                    imageUrl = labels.icon();
                }
                String subtitle = null;
                Place place = brewery.getMainPlace();
                if (place != null) {
                    subtitle = place.locality();
                }
                viewStates = viewStates.add(SearchResultItemViewStateAdapterDelegate.ViewState.create(imageUrl, brewery.name(), subtitle));
            }
        }

        return SearchViewState.create(viewStates.build());
    }
}
