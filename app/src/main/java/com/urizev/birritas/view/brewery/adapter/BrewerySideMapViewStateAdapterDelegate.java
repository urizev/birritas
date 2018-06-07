package com.urizev.birritas.view.brewery.adapter;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.auto.value.AutoValue;
import com.urizev.birritas.R;
import com.urizev.birritas.app.utils.SphericalUtil;
import com.urizev.birritas.view.common.ViewStateAdapterDelegate;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class BrewerySideMapViewStateAdapterDelegate extends ViewStateAdapterDelegate<BrewerySideMapViewStateAdapterDelegate.ViewState, BrewerySideMapViewStateAdapterDelegate.ViewHolder> {
    private static final int RADIUS = 5000;
    private int mMapBoundsPadding;

    public BrewerySideMapViewStateAdapterDelegate(Resources resources) {
        mMapBoundsPadding = (int) resources.getDimension(R.dimen.map_region_padding);
    }

    @Override
    protected boolean isForViewType(@NonNull com.urizev.birritas.view.common.ViewState item, @NonNull List<com.urizev.birritas.view.common.ViewState> items, int position) {
        return item instanceof ViewState;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        return new ViewHolder(viewItemForViewHolder(parent, R.layout.cell_brewery_map));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewState item, @NonNull ViewHolder holder, @NonNull List<Object> payloads) {
        holder.bind(item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {
        @BindView(R.id.map) MapView mMapView;
        private GoogleMap mMap;
        private ViewState mViewState;
        private Marker mMapMarker;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mMapView.onCreate(null);
        }


        public void bind(ViewState vs) {
            this.mViewState = vs;
            if (this.mMap == null) {
                mMapView.getMapAsync(this);
                return;
            }

            if (mMapMarker == null) {
                MarkerOptions options = mViewState.markerOptions();
                mMapMarker = mMap.addMarker(options);
                LatLng center = new LatLng(options.getPosition().latitude, options.getPosition().longitude);
                LatLngBounds bounds = SphericalUtil.createBounds(center, RADIUS);
                CameraUpdate cameraUpdate;
                cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, mMapBoundsPadding);
                mMap.animateCamera(cameraUpdate);

            }
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            bind(this.mViewState);
        }

        void onAttach() {
            mMapView.onResume();
        }

        void onDetach() {
            mMapView.onPause();
        }
    }

    @Override
    protected void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ((ViewHolder) holder).onAttach();
    }

    @Override
    protected void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        ((ViewHolder) holder).onDetach();
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    protected void onViewRecycled(@NonNull RecyclerView.ViewHolder viewHolder) {
        super.onViewRecycled(viewHolder);
    }

    @AutoValue
    public static abstract class ViewState implements com.urizev.birritas.view.common.ViewState {
        public abstract MarkerOptions markerOptions();

        public static BrewerySideMapViewStateAdapterDelegate.ViewState create(MarkerOptions markerOptions) {
            return new AutoValue_BrewerySideMapViewStateAdapterDelegate_ViewState(markerOptions);
        }

    }
}
