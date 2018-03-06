package com.urizev.birritas.view.featured;

import android.text.TextUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.urizev.birritas.R;
import com.urizev.birritas.app.providers.resources.ResourceProvider;
import com.urizev.birritas.app.rx.RxUtils;
import com.urizev.birritas.domain.entities.Beer;
import com.urizev.birritas.domain.entities.Brewery;
import com.urizev.birritas.domain.entities.ImageSet;
import com.urizev.birritas.domain.entities.SRM;
import com.urizev.birritas.domain.entities.Style;
import com.urizev.birritas.domain.usecases.FavoritesBeerIdsUseCase;
import com.urizev.birritas.domain.usecases.FeaturedBeersUseCase;
import com.urizev.birritas.domain.usecases.UpdateFavoriteBeerUseCase;
import com.urizev.birritas.view.common.Presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import timber.log.Timber;

class FeaturedPresenter extends Presenter<FeaturedViewState> {
    private final FeaturedBeersUseCase mUseCase;
    private final FavoritesBeerIdsUseCase mFavIdsUseCase;
    private final UpdateFavoriteBeerUseCase mUpdateFavoriteBeerUseCase;
    private final BehaviorSubject<FeaturedBeersModel> mFeaturedBeersModel;
    private final BehaviorSubject<ImmutableSet<String>> mFavoriteBeersModel;
    private final int mNoSrmColor;
    private Disposable mLoadingDataDisposable;
    private String mNa;

    FeaturedPresenter(FeaturedBeersUseCase beersUseCase,
                      FavoritesBeerIdsUseCase favIdsUseCase,
                      UpdateFavoriteBeerUseCase updateFavoriteBeerUseCase,
                      ResourceProvider resourceProvider) {
        this.mUpdateFavoriteBeerUseCase = updateFavoriteBeerUseCase;
        this.mNa = resourceProvider.getString(R.string.n_a);
        this.mNoSrmColor = resourceProvider.getColor(R.color.no_srm);
        this.mUseCase = beersUseCase;
        this.mFavIdsUseCase = favIdsUseCase;
        this.mFavoriteBeersModel = BehaviorSubject.createDefault(ImmutableSet.of());
        this.mFeaturedBeersModel = this.newFeaturedBeersModel();
        observeModelUpdates();
        observeFavoriteUpdates();
        loadData();
    }

    private void observeFavoriteUpdates() {
        addDisposable(mFavIdsUseCase.execute(null)
                .doOnNext(l -> Timber.d("Favorites: %s", l))
                .doOnNext(mFavoriteBeersModel::onNext)
                .subscribe());
    }

    private void observeModelUpdates() {
        addDisposable(Observable.combineLatest(this.mFeaturedBeersModel.observeOn(Schedulers.computation()),
                this.mFavoriteBeersModel.observeOn(Schedulers.computation()),
                this::modelToViewState)
                .observeOn(Schedulers.computation())
                .subscribeOn(Schedulers.computation())
                .doOnNext(this::publishViewState)
                .subscribe());
    }

    private BehaviorSubject<FeaturedBeersModel> newFeaturedBeersModel() {
        return BehaviorSubject.createDefault(FeaturedBeersModel.builder()
                .beers(ImmutableList.of())
                .loading(true)
                .error(null)
                .build());
    }

    private void loadData() {
        if (mLoadingDataDisposable != null) {
            return;
        }
        mLoadingDataDisposable = mUseCase.execute(null)
                .observeOn(Schedulers.computation())
                .map(beers -> {
                    RxUtils.assertComputationThread();
                    return mFeaturedBeersModel.getValue().withBeers(beers);
                })
                .onErrorReturn(throwable -> {
                    RxUtils.assertComputationThread();
                    return mFeaturedBeersModel.getValue().withError(throwable);
                })
                .doOnNext(mFeaturedBeersModel::onNext)
                .doFinally(() -> {
                    RxUtils.assertComputationThread();
                    mLoadingDataDisposable = null;
                })
                .subscribe();
    }

    private FeaturedViewState modelToViewState(FeaturedBeersModel beers, ImmutableSet<String> favIds) {
        RxUtils.assertComputationThread();

        ImmutableList.Builder<FeaturedItemViewState> viewStates = new ImmutableList.Builder<>();
        for (Beer beer : beers.beers()){
            FeaturedItemViewState vs = this.beerToViewState(beer, favIds.contains(beer.id()));
            viewStates = viewStates.add(vs);
        }
        return FeaturedViewState.create(viewStates.build(), beers.loading(), beers.error());
    }

    private FeaturedItemViewState beerToViewState(Beer beer, boolean favorite) {
        RxUtils.assertComputationThread();

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
        String ibuValue = beer.ibu() == null ? mNa : String.format(Locale.getDefault(), "%.1f", beer.ibu());
        String abvValue = beer.abv() == null ? mNa : String.format(Locale.getDefault(), "%.1f", beer.abv());
        String brewedBy = "";
        ImmutableList<Brewery> breweries = beer.breweries();
        if (breweries != null && !breweries.isEmpty()) {
            List<String> breweryNames = new ArrayList<>(breweries.size());
            for(Brewery brewery : breweries) {
                String name = brewery.shortName();
                if (TextUtils.isEmpty(name)) {
                    name = brewery.name();
                }
                if (!TextUtils.isEmpty(name)) {
                    breweryNames.add(name);
                }
            }
            if (!breweryNames.isEmpty()) {
                brewedBy = TextUtils.join(", ", breweryNames);
            }
        }

        String styleName = "";
        Style style = beer.style();
        if (style != null) {
            styleName = style.shortName();
        }

        return FeaturedItemViewState.create(beer.id(), beer.name(), icon, styleName, brewedBy,srmColor, srmValue, ibuValue, abvValue, favorite);
    }

    void onFavoriteEvent(FeaturedAdapter.FavoriteEvent e) {
        if (!mFeaturedBeersModel.hasValue()) {
            return;
        }

        FeaturedBeersModel model = mFeaturedBeersModel.getValue();
        if (model.beers().size() <= e.position()) {
            return;
        }

        String id = model.beers().get(e.position()).id();

        addDisposable(mUpdateFavoriteBeerUseCase.execute(UpdateFavoriteBeerUseCase.FavoriteEvent.create(id, e.isFavorite()))
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .subscribe());
    }
}
