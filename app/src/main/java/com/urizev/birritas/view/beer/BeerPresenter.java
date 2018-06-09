package com.urizev.birritas.view.beer;


import android.text.TextUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.urizev.birritas.R;
import com.urizev.birritas.app.providers.resources.ResourceProvider;
import com.urizev.birritas.app.rx.RxUtils;
import com.urizev.birritas.domain.entities.Beer;
import com.urizev.birritas.domain.entities.Brewery;
import com.urizev.birritas.domain.entities.Hop;
import com.urizev.birritas.domain.entities.ImageSet;
import com.urizev.birritas.domain.entities.Ingredients;
import com.urizev.birritas.domain.entities.Malt;
import com.urizev.birritas.domain.entities.SRM;
import com.urizev.birritas.domain.entities.Style;
import com.urizev.birritas.domain.entities.Yeast;
import com.urizev.birritas.domain.usecases.BeerDetailsUseCase;
import com.urizev.birritas.domain.usecases.FavoritesBeerIdsUseCase;
import com.urizev.birritas.domain.usecases.UpdateFavoriteBeerUseCase;
import com.urizev.birritas.view.common.Presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

class BeerPresenter extends Presenter<PresenterBeerViewState> {
    private final BehaviorSubject<BeerModel> mBeerModel;
    private final String mHyphen;
    private final BeerDetailsUseCase mBeerDetailsUseCase;
    private final FavoritesBeerIdsUseCase mFavoritesBeerIdsUseCase;
    private final UpdateFavoriteBeerUseCase mUpdateFavoriteBeerUseCase;
    private final String mNa;
    private final String mBeerId;
    private final int mNoSrm;

    BeerPresenter(@NonNull String beerId,
                  @NonNull BeerDetailsUseCase beerDetailsUseCase,
                  @NonNull FavoritesBeerIdsUseCase favoritesBeerIdsUseCase,
                  @NonNull UpdateFavoriteBeerUseCase updateFavoriteBeerUseCase,
                  @NonNull ResourceProvider resourceProvider) {
        this.mBeerId = beerId;
        this.mBeerDetailsUseCase = beerDetailsUseCase;
        this.mFavoritesBeerIdsUseCase = favoritesBeerIdsUseCase;
        this.mUpdateFavoriteBeerUseCase = updateFavoriteBeerUseCase;
        this.mNa = resourceProvider.getString(R.string.n_a);
        this.mNoSrm = resourceProvider.getColor(R.color.no_srm);
        this.mHyphen = resourceProvider.getString(R.string.hyphen);
        this.mBeerModel = BehaviorSubject.createDefault(BeerModel.builder().loading(true).build());
        observeModels();
        loadBeer(mBeerId);
    }

    private void loadBeer(String beerId) {
        addDisposable(Observable.just(beerId)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .flatMap(id -> mBeerDetailsUseCase.execute(id).subscribeOn(Schedulers.computation()))
                .map(beer -> mBeerModel.getValue().withBeer(beer))
                .startWith(mBeerModel.getValue().withLoading(true))
                .onErrorReturn(throwable -> mBeerModel.getValue().withThrowable(throwable))
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

    private PresenterBeerViewState modelToViewState(BeerModel model, ImmutableSet<String> favorites) {
        RxUtils.assertComputationThread();

        String name = "";
        String imageUrl = null;
        String brewedBy = mHyphen;
        String brewedById = null;
        String styleText = mHyphen;
        String srmText = mNa;
        String ibuText = mNa;
        String abvText = mNa;
        ImmutableList.Builder<String> ingredients = new ImmutableList.Builder<>();
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
                brewedById = breweries.get(0).id();
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

            if (beer.ingredients() != null) {
                Ingredients beerIngredients = beer.ingredients();
                for (Yeast yeast : beerIngredients.yeasts()) {
                    ingredients = ingredients.add(yeast.name());
                }
                for (Malt malt : beerIngredients.malts()) {
                    ingredients = ingredients.add(malt.name());
                }
                for (Hop hop : beerIngredients.hops()) {
                    ingredients = ingredients.add(hop.name());
                }
            }
        }

        return PresenterBeerViewState.create(model.loading(), model.throwable(), name, imageUrl, brewedBy, brewedById, styleText, srmText, mNoSrm,
                ibuText, abvText, ingredients.build(), favorite);
    }

    public void onFavoriteClicked(boolean selected) {
        Beer beer = mBeerModel.getValue().beer();
        if (beer == null) {
             return;
        }

        String id = beer.id();
        addDisposable(mUpdateFavoriteBeerUseCase
                .execute(UpdateFavoriteBeerUseCase.FavoriteEvent.create(id, selected))
                .subscribe());
    }

    public void reloadBeer() {
        loadBeer(mBeerId);
    }
}
