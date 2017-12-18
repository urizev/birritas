package com.urizev.birritas.view.nearby;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.common.collect.ImmutableList;
import com.urizev.birritas.R;
import com.urizev.birritas.domain.usecases.NearbyUseCase;
import com.urizev.birritas.view.common.PresenterFragment;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.ButterKnife;

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
        NearbyModel model = NearbyModel.builder().build();
        return new NearbyPresenter(nearbyUseCase, model);
    }

    @Override
    protected void renderViewState(NearbyViewState<MarkerViewState> vs) {
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
            mapFragment.getMapAsync(map -> {
                mMap = map;
                bindPresenter();
            });
        }
        return false;
    }

    @Override
    protected NearbyViewState<MarkerViewState> prepareViewState(NearbyViewState<PlaceViewState> vs) {
        ImmutableList.Builder<MarkerViewState> builder = new ImmutableList.Builder<>();
        for (PlaceViewState pvs : vs.viewStates()) {
            builder = builder.add(this.placeToMarker(pvs));
        }
        return NearbyViewState.create(vs.loading(), vs.error(), builder.build());
    }

    private MarkerViewState placeToMarker(PlaceViewState pvs) {
        MarkerOptions options = new MarkerOptions()
                .title(pvs.name())
                .position(new LatLng(pvs.latitude(), pvs.longitude()));
        return MarkerViewState.create(options);
    }
}
