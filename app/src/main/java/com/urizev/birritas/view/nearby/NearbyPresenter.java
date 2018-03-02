package com.urizev.birritas.view.nearby;

import com.google.common.collect.ImmutableList;
import com.urizev.birritas.R;
import com.urizev.birritas.app.providers.resources.ResourceProvider;
import com.urizev.birritas.app.rx.RxLocation;
import com.urizev.birritas.app.rx.RxUtils;
import com.urizev.birritas.domain.entities.Brewery;
import com.urizev.birritas.domain.entities.Coordinate;
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
    private final RxLocation mRxLocation;
    private final String mNa;

    NearbyPresenter(NearbyUseCase useCase, NearbyModel model, ResourceProvider resourceProvider, RxLocation rxLocation) {
        this.mNa = resourceProvider.getString(R.string.n_a);
        this.mUseCase = useCase;
        this.mModel = BehaviorSubject.createDefault(model);
        this.mSearchSpec = BehaviorSubject.create();
        this.mRxLocation = rxLocation;

        addDisposable(rxLocation.observe()
                .map(location -> Coordinate.create(location.getLatitude(), location.getLongitude()))
                .doOnNext(coordinate -> Timber.d("Coordinate: %s", coordinate))
                .doOnNext(coordinate -> mModel.onNext(mModel.getValue().withUserCoordinate(coordinate)))
                .subscribe());
        addDisposable(this.mModel
                .observeOn(Schedulers.computation())
                .distinctUntilChanged()
                .doOnNext(m -> Timber.d("New model: %s", m))
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

        PlaceViewState selectedViewState = null;
        ImmutableList.Builder<PlaceViewState> builder = new ImmutableList.Builder<>();
        for (Place place : model.places()) {
            PlaceViewState vs = placeToPlaceViewState(place);
            builder = builder.add();
            if (place.id().equals(model.selectedPlaceId())) {
                selectedViewState = vs;
            }
        }

        return NearbyViewState.create(model.mapCoordinate(), model.shouldMoveMap(), model.error(), builder.build(), selectedViewState);
    }

    private PlaceViewState placeToPlaceViewState(Place place) {
        RxUtils.assertComputationThread();

        String name = mNa;
        Brewery brewery = place.brewery();
        if (brewery != null) {
            if (brewery.shortName() != null) {
                name = brewery.shortName();
            } else if (brewery.name() != null) {
                name = brewery.name();
            }
        }

        return PlaceViewState.create(place.id(), name, place.latitude(), place.longitude());
    }

    void mapIdleAt(double latitude, double longitude, int radius) {
        RxUtils.assertComputationThread();
        Timber.d("Map moved to (%f;%f) %d", latitude, longitude, radius);

        Coordinate coordinate = Coordinate.create(latitude, longitude);
        NearbyModel newModel = mModel.getValue().withIdleLocation(coordinate);
        mModel.onNext(newModel);
        coordinate = newModel.mapCoordinate();
        if (coordinate != null) {
            Timber.d("Searching at (%f;%f) %d", coordinate.latitude(), coordinate.longitude(), radius);
            mSearchSpec.onNext(NearbyUseCase.SearchSpec.create(coordinate.latitude(), coordinate.longitude(), radius));
        }
        else {
            Timber.d("Search ignored move");
        }
    }

    void onGoToUserLocationClicked() {

    }

    void mapMoving() {
        Timber.d("Creando modelo con shouldMove = false");
        mModel.onNext(mModel.getValue().withMapMoving());
    }

    void onPlaceSelected(String id) {
        mModel.onNext(mModel.getValue().withSelection(id));
    }
}
