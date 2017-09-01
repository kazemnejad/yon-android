package io.yon.android.view.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.orhanobut.logger.Logger;
import com.patloew.rxlocation.RxLocation;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.yon.android.R;
import io.yon.android.repository.ContentRepository;
import io.yon.android.util.RxUtils;

/**
 * Created by amirhosein on 8/27/2017 AD.
 */

public class RestaurantListTestActivity extends Activity {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_restuarant_list_test;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("تست لیست");

        RxLocation rxLocation = new RxLocation(this);

//        RxLocation rxLocation = new RxLocation(context);


//        LocationRequest locationRequest = LocationRequest.create()
//                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//                .setInterval(5000);
//
//
//        new RxPermissions(this)
//                .request(Manifest.permission.ACCESS_FINE_LOCATION)
//                .filter(granted ->  granted)
//                .flatMap(x -> rxLocation.settings().checkAndHandleResolution(locationRequest).toObservable())
//                .filter(ok -> ok)
//                .switchIfEmpty(x -> rxLocation.location().lastLocation())
//                .flatMap(x -> rxLocation.location().updates(locationRequest))
//                .subscribe(a -> {
//                    Logger.d(a.getLongitude());
//                    Logger.d("Asdddd");
//                }, e -> {
//                    e.printStackTrace();
//                    Logger.d("sdsd");
//                }, () -> {
//                    Logger.d("sdsd");
//                });

//        Logger.d(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION));
//
//        new RxPermissions(this)
//                .request(Manifest.permission.ACCESS_FINE_LOCATION)
//                .subscribe(Logger::d);
//


//        ContentRepository.getInstance()
//                .getLocationObservable(this)
//                .compose(RxUtils.applySchedulers())
//                .subscribe(location -> {
//                    if (ContentRepository.NULL_LOCATION_PROVIDER.equals(location.getProvider()))
//                        Logger.d("null");
//                    else
//                        Logger.d("sag");
//                });
    }
}
