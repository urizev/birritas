package com.urizev.birritas.view.featured;

import android.text.TextUtils;

import com.google.common.collect.ImmutableList;
import com.urizev.birritas.R;
import com.urizev.birritas.app.providers.ResourceProvider;
import com.urizev.birritas.domain.entities.Beer;
import com.urizev.birritas.domain.entities.Brewery;
import com.urizev.birritas.domain.entities.ImageSet;
import com.urizev.birritas.domain.entities.SRM;
import com.urizev.birritas.domain.entities.Style;
import com.urizev.birritas.domain.repositories.BeerRepository;
import com.urizev.birritas.view.common.Presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;

class FeaturedPresenter extends Presenter<FeaturedViewState> {
    private final BeerRepository mRepository;
    private final BehaviorSubject<FeaturedModel> mModel;
    private final ResourceProvider resourceProvider;
    private final int mNoSrmColor;
    private Disposable mLoadingDataDisposable;
    private String mNa;

    FeaturedPresenter(BeerRepository repository, FeaturedModel model, ResourceProvider resourceProvider) {
        mNa = resourceProvider.getString(R.string.n_a);
        mNoSrmColor = resourceProvider.getColor(R.color.no_srm);
        this.mRepository = repository;
        this.mModel = BehaviorSubject.createDefault(model);
        this.resourceProvider = resourceProvider;
        this.mModel.doOnNext(this::modelToViewState).subscribe();
        loadData();
    }

    private void loadData() {
        if (mLoadingDataDisposable != null) {
            return;
        }
        mLoadingDataDisposable = mRepository.getFeaturedBeers()
                .map(beers -> mModel.getValue().withBeers(beers))
                .onErrorReturn(throwable -> mModel.getValue().withError(throwable))
                .doOnNext(mModel::onNext)
                .subscribe();
    }

    private void modelToViewState(FeaturedModel model) {
        ImmutableList.Builder<FeaturedItemViewState> viewStates = new ImmutableList.Builder<>();
        for (Beer beer : model.beers()){
            FeaturedItemViewState vs = this.beerToViewState(beer);
            viewStates = viewStates.add(vs);
        }
        FeaturedViewState viewState = FeaturedViewState.create(viewStates.build(), model.loading(), model.error());
        publishViewState(viewState);
    }

    private FeaturedItemViewState beerToViewState(Beer beer) {
        ImageSet labels = beer.labels();
        String icon = labels != null ? labels.icon() : null;
        int srmColor = mNoSrmColor;
        String srmValue = mNa;
        SRM srm = beer.srm();
        if (srm != null) {
            srmColor = srm.color();
            String extra = "";
            if (srm.over()) {
                extra = "+";
            }
            srmValue = String.format(Locale.getDefault(), "%d%s", srm.value(), extra);
        }
        String ibuValue = beer.ibu() == null ? mNa : String.format(Locale.getDefault(), "%d", beer.ibu());
        String abvValue = beer.abv() == null ? mNa : String.format(Locale.getDefault(), "%.1f", beer.abv());
        String brewedBy = "";
        ImmutableList<Brewery> breweries = beer.breweries();
        if (breweries != null && !breweries.isEmpty()) {
            List<String> breweryNames = new ArrayList<>(breweries.size());
            for(Brewery brewery : breweries) {
                breweryNames.add(brewery.shortName());
            }
            brewedBy = TextUtils.join(", ", breweryNames);
        }

        String styleName = "";
        Style style = beer.style();
        if (style != null) {
            styleName = style.shortName();
        }

        return FeaturedItemViewState.create(beer.name(), icon, styleName, brewedBy,srmColor, srmValue, ibuValue, abvValue, false);
    }
}