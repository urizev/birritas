package com.urizev.birritas.app.utils;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import static java.lang.Math.toRadians;

/**
 * Creado por jcvallejo en 18/2/18.
 */

public class SphericalUtil {
    private static final double EARTH_RADIUS = 6371009;
    private static final double sqrt2 = Math.sqrt(2);

    private static LatLng computeOffset(LatLng from, double distance, double heading) {
        distance /= EARTH_RADIUS;
        heading = Math.toRadians(heading);
        double fromLat = toRadians(from.latitude);
        double fromLng = toRadians(from.longitude);
        double cosDistance = Math.cos(distance);
        double sinDistance = Math.sin(distance);
        double sinFromLat = Math.sin(fromLat);
        double cosFromLat = Math.cos(fromLat);
        double sinLat = cosDistance * sinFromLat + sinDistance * cosFromLat * Math.cos(heading);
        double dLng = Math.atan2(
                sinDistance * cosFromLat * Math.sin(heading),
                cosDistance - sinFromLat * sinLat);
        return new LatLng(Math.toDegrees(Math.asin(sinLat)), Math.toDegrees(fromLng + dLng));
    }

    public static LatLngBounds createBounds(LatLng center, int radius) {
        LatLng ne = SphericalUtil.computeOffset(center, radius * sqrt2, 135);
        LatLng sw = SphericalUtil.computeOffset(center, radius * sqrt2, 315);

        double lngDiff = ne.latitude - sw.latitude;
        if (lngDiff < 0) {
            double lng = ne.longitude;
            ne = new LatLng(ne.latitude, sw.longitude);
            sw = new LatLng(sw.latitude, lng);
        }

        return new LatLngBounds(ne, sw);
    }
}
