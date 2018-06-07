package com.urizev.birritas.view.nearby;

import com.google.common.collect.ImmutableList;
import com.urizev.birritas.R;
import com.urizev.birritas.app.providers.resources.ResourceProvider;
import com.urizev.birritas.app.rx.RxLocation;
import com.urizev.birritas.app.rx.RxUtils;
import com.urizev.birritas.domain.entities.Brewery;
import com.urizev.birritas.domain.entities.Coordinate;
import com.urizev.birritas.domain.entities.ImageSet;
import com.urizev.birritas.domain.entities.Place;
import com.urizev.birritas.domain.usecases.NearbyUseCase;
import com.urizev.birritas.view.common.Presenter;

import java.util.concurrent.TimeUnit;

import io.reactivex.Maybe;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import timber.log.Timber;

class NearbyPresenter extends Presenter<NearbyViewState<PlaceViewState>> {
    private static final long LAST_LOCATION_TIME = TimeUnit.MINUTES.toMillis(10);
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

        addDisposable(this.mModel
                .observeOn(Schedulers.computation())
                .distinctUntilChanged()
                .map(this::modelToViewState)
                .doOnNext(this::publishViewState)
                .subscribe());
        subscribeToSearchSpec();
        listenUserLocation();
    }

    private void listenUserLocation() {
        addDisposable(mRxLocation
                .observePermission()
                .firstElement()
                .flatMap(granted -> {
                    if (granted) {
                        mModel.onNext(mModel.getValue().withHasPermission());
                        return mRxLocation.observeLast(LAST_LOCATION_TIME);
                    }
                    else {
                        mModel.onNext(mModel.getValue().withRequestPermission(true));
                        return Maybe.empty();
                    }
                })
                .map(location -> Coordinate.create(location.getLatitude(), location.getLongitude()))
                .doOnSuccess(coordinate -> Timber.d("Coordinate: %s", coordinate))
                .doOnSuccess(coordinate -> mModel.onNext(mModel.getValue().withUserCoordinate(coordinate)))
                .subscribe());
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
            builder = builder.add(vs);
            Brewery brewery = place.brewery();
            if (brewery != null) {
                if (brewery.id().equals(model.selectedPlaceId())) {
                    selectedViewState = vs;
                }
            }
        }

        return NearbyViewState.create(model.mapCoordinate(), model.shouldMoveMap(), model.error(), builder.build(), selectedViewState, model.requestLocationPermission(), model.hasLocationPermission());
    }

    private PlaceViewState placeToPlaceViewState(Place place) {
        RxUtils.assertComputationThread();

        String name = mNa;
        String imageUrl = null;
        Brewery brewery = place.brewery();
        String breweryId = null;
        if (brewery != null) {
            breweryId = brewery.id();
            if (brewery.shortName() != null) {
                name = brewery.shortName();
            } else if (brewery.name() != null) {
                name = brewery.name();
            }

            ImageSet images = brewery.images();
            if (images != null) {
                imageUrl = images.squareMedium();
            }
        }

        return PlaceViewState.create(breweryId, name, imageUrl, place.streetAddress(), place.coordinate());
    }

    void mapIdleAt(double latitude, double longitude, int radius) {
        RxUtils.assertComputationThread();

        Coordinate coordinate = Coordinate.create(latitude, longitude);
        NearbyModel newModel = mModel.getValue().withIdleLocation(coordinate);
        mModel.onNext(newModel);
        coordinate = newModel.mapCoordinate();
        if (coordinate != null) {
            mSearchSpec.onNext(NearbyUseCase.SearchSpec.create(coordinate.latitude(), coordinate.longitude(), radius));
        }
    }

    void onGoToUserLocationClicked() {
        mModel.onNext(mModel.getValue().withMapMoving());
        listenUserLocation();
    }

    void mapMoving() {
        mModel.onNext(mModel.getValue().withMapMoving());
    }

    void onPlaceSelected(String id, Coordinate coordinate) {
        Timber.d("Place selected: %s", id);
        mModel.onNext(mModel.getValue().withSelection(id, coordinate));
    }

    void clearRequestLocationPermissions() {
        mModel.onNext(mModel.getValue().withRequestPermission(false));
    }
}
