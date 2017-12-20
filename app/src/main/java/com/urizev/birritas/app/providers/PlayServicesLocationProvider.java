package com.urizev.birritas.app.providers;

import android.content.Context;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import javax.inject.Inject;

public class PlayServicesLocationProvider extends LocationProvider {
    private static final long REQUEST_INTERVAL = 60 * 1000;
    private static final long REQUEST_FASTEST_INTERVAL = 10 * 1000;
    private final FusedLocationProviderClient mClient;
    private final LocationRequest mLocationRequest;

    @Inject
    PlayServicesLocationProvider(Context context) {
        super();
        mClient = LocationServices.getFusedLocationProviderClient(context);
        mLocationRequest = LocationRequest.create()
                .setInterval(REQUEST_INTERVAL)
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setFastestInterval(REQUEST_FASTEST_INTERVAL);
    }

    @Override
    protected void stopMonitoringLocation() {
    }

    @Override
    protected void startMonitoringLocation() {

    }
}
