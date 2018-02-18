package com.urizev.birritas.view.nearby;


import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

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
import com.urizev.birritas.domain.usecases.NearbyUseCase;
import com.urizev.birritas.view.common.PresenterFragment;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.ButterKnife;
import io.reactivex.schedulers.Schedulers;

public class NearbyFragment extends PresenterFragment<NearbyViewState<MarkerViewState>,NearbyViewState<PlaceViewState>,NearbyPresenter> {
    private static final double MIN_RADIUS = 5000;

    private GoogleMap mMap;
    private Map<Marker, MarkerViewState> viewStatesByMarker;
    private Map<String,Marker> markersById;

    @Inject NearbyUseCase nearbyUseCase;
    @Inject ResourceProvider resourceProvider;
    @Inject RxLocation rxLocation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        markersById = new HashMap<>();
        viewStatesByMarker = new HashMap<>();
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
                .loading(true)
                .places(ImmutableSet.of())
                .build();
        return new NearbyPresenter(nearbyUseCase, model, resourceProvider);
    }

    @Override
    protected void renderViewState(NearbyViewState<MarkerViewState> vs) {
        RxUtils.assertMainThread();

        for (MarkerViewState movs : vs.viewStates()) {
            Marker marker = markersById.get(movs.id());
            if (marker == null) {
                marker = mMap.addMarker(movs.markerOptions());
                markersById.put(movs.id(), marker);
            }

            viewStatesByMarker.put(marker, movs);
        }
    }

    @Override
    protected boolean bindView(View view) {
        ButterKnife.bind(this, view);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this::onMapReady);
        }
        return false;
    }

    private void onMapReady(GoogleMap map) {
        mMap = map;
        bindPresenter();
        addDisposable(RxMap.cameraIdleBounds(map)
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
                    getPresenter().mapMoveTo(center.latitude, center.longitude, radius);
                }).subscribe());

        addDisposable(rxLocation.observeLast(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(1))
                .map(location -> new LatLng(location.getLatitude(), location.getLongitude()))
                .map(center -> {
                    double distanceFromCenterToCorner = MIN_RADIUS * Math.sqrt(2.0);
                    LatLng southwestCorner =
                            SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 225.0);
                    LatLng northeastCorner =
                            SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 45.0);
                    return new LatLngBounds(southwestCorner, northeastCorner);
                })
                .map(bounds -> CameraUpdateFactory.newLatLngBounds(bounds, 20))
                .doOnSuccess(ca -> mMap.moveCamera(ca))
                .subscribe());
    }

    @Override
    protected NearbyViewState<MarkerViewState> prepareViewState(NearbyViewState<PlaceViewState> vs) {
        RxUtils.assertComputationThread();

        ImmutableList.Builder<MarkerViewState> builder = new ImmutableList.Builder<>();
        for (PlaceViewState pvs : vs.viewStates()) {
            builder = builder.add(this.placeToMarker(pvs));
        }
        return NearbyViewState.create(vs.loading(), vs.error(), builder.build());
    }

    private MarkerViewState placeToMarker(PlaceViewState pvs) {
        RxUtils.assertComputationThread();

        MarkerOptions options = new MarkerOptions()
                .title(pvs.name())
                .position(new LatLng(pvs.latitude(), pvs.longitude()));
        return MarkerViewState.create(pvs.id(), options);
    }
}
