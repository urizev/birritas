package com.urizev.birritas.view.nearby;


import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.AutoTransition;
import android.support.transition.TransitionManager;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.urizev.birritas.R;
import com.urizev.birritas.app.providers.resources.ResourceProvider;
import com.urizev.birritas.app.rx.RxLocation;
import com.urizev.birritas.app.rx.RxMap;
import com.urizev.birritas.app.rx.RxUtils;
import com.urizev.birritas.app.utils.SphericalUtil;
import com.urizev.birritas.domain.entities.Coordinate;
import com.urizev.birritas.domain.usecases.NearbyUseCase;
import com.urizev.birritas.view.common.PresenterFragment;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class NearbyFragment extends PresenterFragment<NearbyViewState<MarkerViewState>,NearbyViewState<PlaceViewState>,NearbyPresenter> {
    private static final long PLACE_CARD_TRANSITION_DURATION = 300;
    private static final int RADIUS = 5000;

    private GoogleMap mMap;
    private Map<Marker, MarkerViewState> viewStatesByMarker;
    private Map<String,Marker> markersById;
    @BindView(R.id.constraint_layout) ConstraintLayout constraintLayout;
    @BindView(R.id.place_card) View placeCard;
    @BindView(R.id.go_to_user_location) FloatingActionButton gotoUserLocation;

    @Inject NearbyUseCase nearbyUseCase;
    @Inject ResourceProvider resourceProvider;
    @Inject RxLocation rxLocation;
    private float mPlaceCardTranslation;
    private ConstraintSet mPlaceCardHiddenConstraintSet;
    private ConstraintSet mPlaceCardShowedConstraintSet;
    private AutoTransition mPlaceCardTransition;
    private int mMapBoundsPadding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        markersById = new HashMap<>();
        viewStatesByMarker = new HashMap<>();
        mPlaceCardTranslation = getResources().getDimension(R.dimen.map_place_card);
        mMapBoundsPadding = (int) getResources().getDimension(R.dimen.map_region_padding);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getApp().getNetComponent().inject(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_nearby;
    }

    @Override
    protected NearbyPresenter createPresenter(Bundle savedInstanceState) {
        NearbyModel model = NearbyModel.builder()
                .shouldMoveMap(false)
                .waitingUserCoordinate(true)
                .places(ImmutableSet.of())
                .build();
        return new NearbyPresenter(nearbyUseCase, model, resourceProvider, rxLocation);
    }

    @Override
    protected void renderViewState(NearbyViewState<MarkerViewState> vs) {
        RxUtils.assertMainThread();
        Timber.d("Rendering map…");

        for (MarkerViewState movs : vs.viewStates()) {
            Marker marker = markersById.get(movs.id());
            if (marker == null) {
                marker = mMap.addMarker(movs.markerOptions());
                markersById.put(movs.id(), marker);
            }

            viewStatesByMarker.put(marker, movs);
        }

        Coordinate coordinate = vs.coordinate();
        if (vs.shouldMove() && coordinate != null) {
            this.moveMapTo(coordinate);
        }
    }

    private void moveMapTo(Coordinate coordinate) {
        LatLng center = new LatLng(coordinate.latitude(), coordinate.longitude());
        LatLngBounds bounds = SphericalUtil.createBounds(center, RADIUS);
        Timber.d("Animating camera to %s", bounds);
        CameraUpdate cameraUpdate;
        cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 0);
        mMap.animateCamera(cameraUpdate);
    }

    private void showPlaceCard(boolean show) {
        ConstraintSet set = show ? mPlaceCardShowedConstraintSet : mPlaceCardHiddenConstraintSet;
        TransitionManager.beginDelayedTransition(constraintLayout, mPlaceCardTransition);
        set.applyTo(constraintLayout);
    }

    @Override
    protected boolean bindView(View view) {
        ButterKnife.bind(this, view);

        mPlaceCardTransition = new AutoTransition();
        mPlaceCardTransition.setDuration(PLACE_CARD_TRANSITION_DURATION);

        mPlaceCardHiddenConstraintSet = new ConstraintSet();
        mPlaceCardHiddenConstraintSet.clone(constraintLayout);
        mPlaceCardShowedConstraintSet = new ConstraintSet();
        mPlaceCardShowedConstraintSet.clone(mPlaceCardHiddenConstraintSet);

        mPlaceCardShowedConstraintSet.clear(R.id.place_card, ConstraintSet.TOP);
        mPlaceCardShowedConstraintSet.addToVerticalChain(R.id.place_card, ConstraintSet.UNSET, ConstraintSet.PARENT_ID);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this::onMapReady);
        }

        return false;
    }

    private void onMapReady(GoogleMap map) {
        mMap = map;
        bindPresenter();
        addDisposable(RxMap.mapIsIdle(map)
                .observeOn(Schedulers.computation())
                .doOnNext(idle -> {
                    if (! idle) {
                        getPresenter().mapMoving();
                    }
                })
                .filter(idle -> idle)
                .observeOn(AndroidSchedulers.mainThread())
                .map(idle -> map.getProjection().getVisibleRegion().latLngBounds)
                .observeOn(Schedulers.computation())
                .doOnNext(bounds -> {
                    RxUtils.assertComputationThread();
                    LatLng center = bounds.getCenter();
                    double left = bounds.southwest.longitude;
                    double top = bounds.northeast.latitude;
                    Location l1 = new Location("");
                    l1.setLatitude(top);
                    l1.setLongitude(left);
                    Location l2 = new Location("");
                    l2.setLatitude(center.latitude);
                    l2.setLongitude(center.longitude);
                    int radius = (int) Math.ceil(l1.distanceTo(l2) / 1000);
                    getPresenter().mapIdleAt(center.latitude, center.longitude, radius);
                })
                .subscribe());
    }

    @OnClick(R.id.go_to_user_location)
    public void onGoToUserLocationClicked(View view) {
        Coordinate coordinate = Coordinate.create(37.774929, -122.419416);
        this.moveMapTo(coordinate);
    }

    @Override
    protected NearbyViewState<MarkerViewState> prepareViewState(NearbyViewState<PlaceViewState> vs) {
        RxUtils.assertComputationThread();

        ImmutableList.Builder<MarkerViewState> builder = new ImmutableList.Builder<>();
        for (PlaceViewState pvs : vs.viewStates()) {
            builder = builder.add(this.placeToMarker(pvs));
        }
        return NearbyViewState.create(vs.coordinate(), vs.shouldMove(), vs.error(), builder.build());
    }

    private MarkerViewState placeToMarker(PlaceViewState pvs) {
        RxUtils.assertComputationThread();

        MarkerOptions options = new MarkerOptions()
                .title(pvs.name())
                .position(new LatLng(pvs.latitude(), pvs.longitude()));
        return MarkerViewState.create(pvs.id(), options);
    }
}
