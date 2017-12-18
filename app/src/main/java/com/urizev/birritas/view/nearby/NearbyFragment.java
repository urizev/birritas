package com.urizev.birritas.view.nearby;


import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.urizev.birritas.R;
import com.urizev.birritas.app.rx.RxMap;
import com.urizev.birritas.app.rx.RxUtils;
import com.urizev.birritas.domain.usecases.NearbyUseCase;
import com.urizev.birritas.view.common.PresenterFragment;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.ButterKnife;
import io.reactivex.schedulers.Schedulers;

public class NearbyFragment extends PresenterFragment<NearbyViewState<MarkerViewState>,NearbyViewState<PlaceViewState>,NearbyPresenter> {
    private GoogleMap mMap;
    @Inject
    NearbyUseCase nearbyUseCase;
    private Map<Marker, MarkerViewState> markers;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        markers = new HashMap<>();
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
        return new NearbyPresenter(nearbyUseCase, model);
    }

    @Override
    protected void renderViewState(NearbyViewState<MarkerViewState> vs) {
        RxUtils.assertMainThread();

        mMap.clear();
        for (MarkerViewState movs : vs.viewStates()) {
            Marker marker = mMap.addMarker(movs.markerOptions());
            markers.put(marker, movs);
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
        addDisposable(RxMap.cameraIdle(map)
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
        return MarkerViewState.create(options);
    }
}
