package com.urizev.birritas.view.brewery;


import android.text.TextUtils;

import com.google.common.collect.ImmutableList;
import com.urizev.birritas.R;
import com.urizev.birritas.app.providers.resources.ResourceProvider;
import com.urizev.birritas.app.rx.RxUtils;
import com.urizev.birritas.domain.entities.Beer;
import com.urizev.birritas.domain.entities.Brewery;
import com.urizev.birritas.domain.entities.Coordinate;
import com.urizev.birritas.domain.entities.ImageSet;
import com.urizev.birritas.domain.entities.Place;
import com.urizev.birritas.domain.entities.SRM;
import com.urizev.birritas.domain.entities.Style;
import com.urizev.birritas.domain.usecases.BreweryDetailsUseCase;
import com.urizev.birritas.view.common.Presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

class BreweryPresenter extends Presenter<BreweryPresenterViewState> {
    private final BehaviorSubject<BreweryModel> mBreweryModel;
    private final BreweryDetailsUseCase mBreweryDetailsUseCase;
    private final ResourceProvider mResourceProvider;
    private final String mBreweryId;
    private final String mNa;
    private final int mNoSrmColor;

    BreweryPresenter(@NonNull String beerId,
                     @NonNull BreweryDetailsUseCase breweryDetailsUseCase,
                     @NonNull ResourceProvider resourceProvider) {
        this.mNa = resourceProvider.getString(R.string.n_a);
        this.mNoSrmColor = resourceProvider.getColor(R.color.no_srm);
        this.mBreweryId = beerId;
        this.mBreweryDetailsUseCase = breweryDetailsUseCase;
        mResourceProvider = resourceProvider;
        this.mBreweryModel = BehaviorSubject.createDefault(BreweryModel.builder().build());
        observeModels();
        loadBeer(mBreweryId);
    }

    private void loadBeer(String beerId) {
        addDisposable(Observable.just(beerId)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .flatMap(id -> mBreweryDetailsUseCase.execute(id).subscribeOn(Schedulers.computation()))
                .map(beer -> mBreweryModel.getValue().withBrewery(beer))
                .doOnNext(mBreweryModel::onNext)
                .subscribe());
    }

    private void observeModels() {
        Scheduler sch = Schedulers.computation();
        addDisposable(mBreweryModel
                .subscribeOn(sch)
                .observeOn(sch)
                .map(this::modelToViewState)
                .doOnNext(this::publishViewState)
                .subscribe());
    }

    private BreweryPresenterViewState modelToViewState(BreweryModel model) {
        RxUtils.assertComputationThread();

        String name = "";
        String imageUrl = null;
        String established = null;
        Coordinate coordinate = null;
        String address = null;
        String description = null;
        ImmutableList.Builder<BreweryBeerPresenterViewState> beersBuilder;
        beersBuilder = new ImmutableList.Builder<>();
        Brewery brewery = model.brewery();
        if (brewery != null) {
            name = brewery.name();
            ImageSet labels = brewery.images();
            if (labels != null) {
                imageUrl = labels.large();
            }
            ImmutableList<Beer> beers = brewery.beers();
            if (beers != null && !beers.isEmpty()) {
                for (Beer beer : beers) {
                    beersBuilder = beersBuilder.add(this.beerToViewState(beer));
                }
            }

            if (brewery.established() != null) {
                established = mResourceProvider.getString(R.string.since_established, brewery.established());
            }

            Place mainLocation = brewery.getMainPlace();
            if (mainLocation != null) {
                coordinate = Coordinate.create(mainLocation.latitude(), mainLocation.longitude());
                address = mainLocation.streetAddress();
            }

            description = brewery.description();
        }

        return BreweryPresenterViewState.create(name, imageUrl, established, coordinate, address, description, beersBuilder.build());
    }

    private BreweryBeerPresenterViewState beerToViewState(Beer beer) {
        RxUtils.assertComputationThread();

        ImageSet labels = beer.labels();
        String icon = labels != null ? labels.medium() : null;
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
                breweryNames.add(brewery.shortName());
            }
            brewedBy = TextUtils.join(", ", breweryNames);
        }

        String styleName = "";
        Style style = beer.style();
        if (style != null) {
            styleName = style.shortName();
        }

        return BreweryBeerPresenterViewState.create(beer.id(), beer.name(), icon, styleName, brewedBy,srmColor, srmValue, ibuValue, abvValue);
    }
}
