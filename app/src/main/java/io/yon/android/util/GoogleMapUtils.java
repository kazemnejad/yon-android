package io.yon.android.util;

import android.net.Uri;

import java.text.MessageFormat;

/**
 * Created by amirhosein on 9/5/2017 AD.
 */

public class GoogleMapUtils {
    public static Uri getSearchViewUrl(double longitude, double latitude) {
        return Uri.parse(MessageFormat.format(
                "https://www.google.com/maps/search/?api=1&query={0},{1}",
                longitude,
                latitude
        ));
    }

    public static Uri getDirectionsViewUrl(double longitude, double latitude) {
        return Uri.parse(MessageFormat.format(
                "https://www.google.com/maps/dir/?api=1&destination={0},{1}",
                longitude,
                latitude
        ));
    }
}
