package io.yon.android.repository;

import android.content.Context;

import java.util.List;

import io.reactivex.Observable;
import io.yon.android.api.WebService;
import io.yon.android.model.Restaurant;
import io.yon.android.model.RestaurantList;

import static io.yon.android.repository.LocationRepository.NULL_LOCATION_PROVIDER;

/**
 * Created by amirhosein on 8/28/2017 AD.
 */

public class RestaurantListRepository {
    private static RestaurantListRepository instance;

    public static RestaurantListRepository getInstance() {
        if (instance == null)
            instance = new RestaurantListRepository();

        return instance;
    }

    public Observable<Lce<List<Restaurant>>> getRestaurantList(Context context, int id) {
        return LocationRepository.getInstance()
                .getLocation(context)
                .flatMap(location -> {
                    if (NULL_LOCATION_PROVIDER.equals(location.getProvider()))
                        return WebService.getInstance().getRestaurantList(id, null, null);
                    else
                        return WebService.getInstance().getRestaurantList(id, location.getLongitude(), location.getLatitude());
                })
                .map(RestaurantList::getRestaurants)
                .map(Lce::data)
                .startWith(Lce.loading())
                .onErrorReturn(Lce::error);
    }
}
