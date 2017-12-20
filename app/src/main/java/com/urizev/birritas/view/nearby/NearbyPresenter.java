package com.urizev.birritas.view.nearby;

import com.google.common.collect.ImmutableList;
import com.urizev.birritas.app.rx.RxUtils;
import com.urizev.birritas.domain.entities.Place;
import com.urizev.birritas.domain.usecases.NearbyUseCase;
import com.urizev.birritas.view.common.Presenter;

import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import timber.log.Timber;

class NearbyPresenter extends Presenter<NearbyViewState<PlaceViewState>> {
    private final NearbyUseCase mUseCase;
    private final BehaviorSubject<NearbyModel> mModel;
    private final BehaviorSubject<NearbyUseCase.SearchSpec> mSearchSpec;

    NearbyPresenter(NearbyUseCase useCase, NearbyModel model) {
        this.mUseCase = useCase;
        this.mModel = BehaviorSubject.createDefault(model);
        this.mSearchSpec = BehaviorSubject.create();
        addDisposable(this.mModel
                .observeOn(Schedulers.computation())
                .map(this::modelToViewState)
                .doOnNext(this::publishViewState)
                .subscribe());
        subscribeToSearchSpec();
    }

    private void subscribeToSearchSpec() {
        addDisposable(mSearchSpec
                .doOnNext(s -> Timber.d("Searching…"))
                .doFinally(() -> Timber.d("Completed??"))
                .subscribeOn(Schedulers.computation())
                .switchMap(mUseCase::execute)
                .observeOn(Schedulers.computation())
                .map(places -> {
                    RxUtils.assertComputationThread();
                    Timber.d("Places %d", places.size());
                    return mModel.getValue().mergePlaces(places);
                })
                .onErrorReturn(throwable -> {
                    RxUtils.assertComputationThread();
                    Timber.e(throwable);
                    return mModel.getValue().withError(throwable);
                })
                .doOnNext(mModel::onNext)
                .doFinally(() -> Timber.d("Completed!"))
                .subscribe());
    }

    private NearbyViewState<PlaceViewState> modelToViewState(NearbyModel model) {
        RxUtils.assertComputationThread();

        ImmutableList.Builder<PlaceViewState> builder = new ImmutableList.Builder<>();
        for (Place place : model.places()) {
            builder = builder.add(placeToPlaceViewState(place));
        }
        return NearbyViewState.create(model.loading(), model.error(), builder.build());
    }

    private PlaceViewState placeToPlaceViewState(Place place) {
        RxUtils.assertComputationThread();

        return PlaceViewState.create(place.id(), place.name(), place.latitude(), place.longitude());
    }

    void mapMoveTo(double latitude, double longitude, int radius) {
        RxUtils.assertComputationThread();

        Timber.d("Map moved to (%f;%f) %d", latitude, longitude, radius);
        mSearchSpec.onNext(NearbyUseCase.SearchSpec.create(latitude, longitude, radius));
    }
}
