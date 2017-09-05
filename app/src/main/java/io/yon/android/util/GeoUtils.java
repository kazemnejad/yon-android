package io.yon.android.util;

/**
 * Created by amirhosein on 9/4/2017 AD.
 */

public class GeoUtils {
    public static double convertDistanceFromMyLocationToKilometer(double initialDistanceInMl) {
        double distanceInKm = initialDistanceInMl * 1.6;
        return (double) Math.round(distanceInKm * 10) / 10;
    }
}
