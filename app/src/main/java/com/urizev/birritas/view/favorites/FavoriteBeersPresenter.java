package com.urizev.birritas.view.favorites;


import android.text.TextUtils;

import com.google.common.collect.ImmutableList;
import com.urizev.birritas.R;
import com.urizev.birritas.app.providers.resources.ResourceProvider;
import com.urizev.birritas.app.rx.RxUtils;
import com.urizev.birritas.domain.entities.Beer;
import com.urizev.birritas.domain.entities.Brewery;
import com.urizev.birritas.domain.entities.ImageSet;
import com.urizev.birritas.domain.entities.Style;
import com.urizev.birritas.domain.usecases.FavoriteBeersUseCase;
import com.urizev.birritas.domain.usecases.UpdateFavoriteBeerUseCase;
import com.urizev.birritas.view.common.Presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

class FavoriteBeersPresenter extends Presenter<FavoriteBeersViewState> {
    private final FavoriteBeersUseCase favoriteBeersUseCase;
    private final UpdateFavoriteBeerUseCase updateFavoriteBeerUseCase;
    private final BehaviorSubject<FavoriteBeersModel> model;
    private final String mNa;

    FavoriteBeersPresenter(FavoriteBeersUseCase favoriteBeersUseCase,
                           UpdateFavoriteBeerUseCase updateFavoriteBeerUseCase,
                           ResourceProvider resourceProvider) {
        this.favoriteBeersUseCase = favoriteBeersUseCase;
        this.updateFavoriteBeerUseCase = updateFavoriteBeerUseCase;
        this.mNa = resourceProvider.getString(R.string.n_a);
        this.model = BehaviorSubject.createDefault(FavoriteBeersModel.builder()
                .beers(ImmutableList.of())
                .loading(true)
                .build());

        this.observeModelUpdates();
        this.observeFavorites();
    }

    private void observeFavorites() {
        addDisposable(this.favoriteBeersUseCase.execute(null)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .map(beers -> model.getValue().withBeers(beers))
                .doOnNext(model::onNext)
                .subscribe());
    }

    private void observeModelUpdates() {
        addDisposable(model
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .map(this::modelToViewState)
                .doOnNext(this::publishViewState)
                .subscribe());
    }

    private FavoriteBeersViewState modelToViewState(FavoriteBeersModel model) {
        RxUtils.assertComputationThread();

        ImmutableList.Builder<FavoriteBeersItemViewState> builder = new ImmutableList.Builder<>();
        for (Beer beer : model.beers()) {
            builder = builder.add(this.beerToViewState(beer));
        }
        return FavoriteBeersViewState.create(builder.build(), model.loading());
    }

    private FavoriteBeersItemViewState beerToViewState(Beer beer) {
        RxUtils.assertComputationThread();

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
        }

        return FavoriteBeersItemViewState.create(beer.id(), beer.name(), icon, styleName, brewedBy, ibuValue, abvValue);
    }

    void deleteFavorite(int position) {
        FavoriteBeersModel model = this.model.getValue();
        if (model == null) {
            return;
        }

        ImmutableList<Beer> beers = model.beers();
        if (beers.size() <= position) {
            return;
        }

        Beer beer = beers.get(position);
        updateFavoriteBeerUseCase.execute(UpdateFavoriteBeerUseCase.FavoriteEvent.create(beer.id(), false))
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .subscribe();
    }
}
