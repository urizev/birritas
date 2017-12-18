package com.urizev.birritas.view.nearby;

import com.google.common.collect.ImmutableList;
import com.urizev.birritas.domain.entities.Place;
import com.urizev.birritas.domain.usecases.NearbyUseCase;
import com.urizev.birritas.view.common.Presenter;

import io.reactivex.subjects.BehaviorSubject;

class NearbyPresenter extends Presenter<NearbyViewState<PlaceViewState>> {
    private final NearbyUseCase mUseCase;
    private final BehaviorSubject<NearbyModel> mModel;
    private final BehaviorSubject<NearbyUseCase.Param> mSearchSpec;

    NearbyPresenter(NearbyUseCase useCase, NearbyModel model) {
        this.mUseCase = useCase;
        this.mModel = BehaviorSubject.createDefault(model);
        this.mSearchSpec = BehaviorSubject.create();
        addDisposable(this.mModel.doOnNext(this::modelToViewState).subscribe());
        subscribeToSearchSpec();
    }

    private void subscribeToSearchSpec() {
        addDisposable(mSearchSpec.flatMap(mUseCase::execute)
                .map(places -> mModel.getValue().mergePlaces(places))
                .onErrorReturn(throwable -> mModel.getValue().withError(throwable))
                .doOnNext(mModel::onNext)
                .subscribe());
    }

    private NearbyViewState modelToViewState(NearbyModel model) {
        ImmutableList.Builder<PlaceViewState> builder = new ImmutableList.Builder<>();
        for (Place place : model.places()) {
            builder = builder.add(placeToPlaceViewState(place));
        }
        return NearbyViewState.create(model.loading(), model.error(), builder.build());
    }

    private PlaceViewState placeToPlaceViewState(Place place) {
        return PlaceViewState.create(place.id(), place.name(), place.latitude(), place.longitude());
    }
}
