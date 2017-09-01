package io.yon.android.repository;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.location.LocationRequest;
import com.orhanobut.logger.Logger;
import com.patloew.rxlocation.RxLocation;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.yon.android.util.RxUtils;

/**
 * Created by amirhosein on 9/1/2017 AD.
 */

public class LocationRepository {
    public static String NULL_LOCATION_PROVIDER = "null location provider";

    private static LocationRepository instance;

    public static LocationRepository getInstance() {
        if (instance == null)
            instance = new LocationRepository();

        return instance;
    }

    public Observable<Location> getLocation(Context context) {
        int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_DENIED)
            return Observable.just(new Location(NULL_LOCATION_PROVIDER));

        RxLocation rxLocation = new RxLocation(context);

        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000);

        return rxLocation.settings()
                .checkAndHandleResolution(locationRequest)
                .flatMapObservable(granted -> {
                    if (granted)
                        return rxLocation.location().updates(locationRequest, 10, TimeUnit.SECONDS).compose(RxUtils.workerSchedulers());
                    else
                        return rxLocation.location().lastLocation().toObservable().compose(RxUtils.workerSchedulers());
                })
                .firstElement()
                .toObservable();
    }
}
