package com.urizev.birritas.view.beer;


import android.text.TextUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.urizev.birritas.R;
import com.urizev.birritas.app.providers.resources.ResourceProvider;
import com.urizev.birritas.app.rx.RxUtils;
import com.urizev.birritas.domain.usecases.BeerDetailsUseCase;
import com.urizev.birritas.domain.entities.Beer;
import com.urizev.birritas.domain.entities.Brewery;
import com.urizev.birritas.domain.entities.ImageSet;
import com.urizev.birritas.domain.entities.SRM;
import com.urizev.birritas.domain.entities.Style;
import com.urizev.birritas.domain.usecases.FavoritesBeerIdsUseCase;
import com.urizev.birritas.view.common.Presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

class BeerPresenter extends Presenter<BeerViewState> {
    private final BehaviorSubject<BeerModel> mBeerModel;
    private final String mHyphen;
    private final BeerDetailsUseCase mBeerDetailsUseCase;
    private final FavoritesBeerIdsUseCase mFavoritesBeerIdsUseCase;
    private final String mNa;
    private final String mBeerId;

    BeerPresenter(@NonNull String beerId,
                  @NonNull BeerDetailsUseCase beerDetailsUseCase,
                  @NonNull FavoritesBeerIdsUseCase favoritesBeerIdsUseCase,
                  @NonNull ResourceProvider resourceProvider) {
        this.mBeerId = beerId;
        this.mBeerDetailsUseCase = beerDetailsUseCase;
        this.mFavoritesBeerIdsUseCase = favoritesBeerIdsUseCase;
        this.mNa = resourceProvider.getString(R.string.n_a);
        this.mHyphen = resourceProvider.getString(R.string.hyphen);
        this.mBeerModel = BehaviorSubject.createDefault(BeerModel.builder().build());
        observeModels();
        loadBeer(mBeerId);
    }

    private void loadBeer(String beerId) {
        addDisposable(Observable.just(beerId)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .flatMap(id -> mBeerDetailsUseCase.execute(id).subscribeOn(Schedulers.computation()))
                .map(beer -> mBeerModel.getValue().withBeer(beer))
                .doOnNext(mBeerModel::onNext)
                .subscribe());
    }

    private void observeModels() {
        Scheduler sch = Schedulers.computation();
        Observable<BeerModel> beerModel = mBeerModel.observeOn(sch).subscribeOn(sch);
        Observable<ImmutableSet<String>> favoriteModel = mFavoritesBeerIdsUseCase.execute(null).observeOn(sch).subscribeOn(sch);
        addDisposable(Observable.combineLatest(beerModel, favoriteModel, this::modelToViewState)
                .subscribeOn(sch)
                .observeOn(sch)
                .doOnNext(this::publishViewState)
                .subscribe());
    }

    private BeerViewState modelToViewState(BeerModel model, ImmutableSet<String> favorites) {
        RxUtils.assertComputationThread();

        String name = "";
        String imageUrl = null;
        String brewedBy = mHyphen;
        String styleText = mHyphen;
        String srmText = mNa;
        String ibuText = mNa;
        String abvText = mNa;
        Beer beer = model.beer();
        boolean favorite = favorites.contains(mBeerId);
        if (beer != null) {
            name = beer.name();
            ImageSet labels = beer.labels();
            if (labels != null) {
                imageUrl = labels.large();
            }
            ImmutableList<Brewery> breweries = beer.breweries();
            if (breweries != null && !breweries.isEmpty()) {
                List<String> names = new ArrayList<>(breweries.size());
                for (Brewery brewery : breweries) {
                    names.add(brewery.name());
                }
                brewedBy = TextUtils.join(", ", names);
            }
            Style style = beer.style();
            if (style != null) {
                styleText = style.shortName();
            }
            SRM srm = beer.srm();
            if (srm != null) {
                String suffix = "";
                if (srm.over()) {
                    suffix = "+";
                }
                srmText = String.format(Locale.getDefault(), "%d%s", srm.value(), suffix);
            }

            if (beer.ibu() != null) {
                ibuText = String.format(Locale.getDefault(), "%.1f", beer.ibu());
            }

            if (beer.abv() != null) {
                abvText = String.format(Locale.getDefault(), "%.1f", beer.abv());
            }
        }

        return BeerViewState.create(name, imageUrl, brewedBy, styleText, srmText, ibuText, abvText, favorite);
    }
}
