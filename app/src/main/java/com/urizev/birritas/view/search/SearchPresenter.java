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
import com.urizev.birritas.view.common.adapter.NoResultsViewStateAdapterDelegate;
import com.urizev.birritas.view.search.adapter.SearchResultItemViewStateAdapterDelegate;

import java.util.Locale;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

class SearchPresenter extends Presenter<SearchViewState> {
    private final SearchUseCase mUseCase;
    private final String mBeersHeader;
    private final String mBreweriesHeader;
    private Disposable mSearchDisposable;
    private final BehaviorSubject<SearchResult> mModel;

    SearchPresenter(SearchUseCase useCase, ResourceProvider resourceProvider) {
        this.mUseCase = useCase;
        this.mModel = BehaviorSubject.create();
        this.mBeersHeader = resourceProvider.getString(R.string.header_beers).toUpperCase(Locale.getDefault());
        this.mBreweriesHeader = resourceProvider.getString(R.string.header_breweries).toUpperCase(Locale.getDefault());

        addDisposable(mModel
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .map(this::resultToViewState)
                .doOnNext(this::publishViewState)
                .subscribe());
    }

    public void search(String query) {
        RxUtils.assertComputationThread();

        if (mSearchDisposable != null) {
            mSearchDisposable.dispose();
        }

        mSearchDisposable = mUseCase.execute(query)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .doOnNext(mModel::onNext)
                .subscribe();
    }

    @Override
    protected void dispose() {
        if (mSearchDisposable != null) {
            mSearchDisposable.dispose();
        }
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
                viewStates = viewStates.add(SearchResultItemViewStateAdapterDelegate.ViewState.create(beer.id(), SearchResultItemViewStateAdapterDelegate.ViewState.TYPE_BEER, imageUrl, beer.name(), subtitle));
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
                viewStates = viewStates.add(SearchResultItemViewStateAdapterDelegate.ViewState.create(brewery.id(), SearchResultItemViewStateAdapterDelegate.ViewState.TYPE_BREWERY, imageUrl, brewery.name(), subtitle));
            }
        }

        if (result.beers().isEmpty() && result.breweries().isEmpty()) {
            viewStates = viewStates.add(NoResultsViewStateAdapterDelegate.ViewState.create());
        }

        return SearchViewState.create(viewStates.build());
    }
}
