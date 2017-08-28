package io.yon.android.repository;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.yon.android.model.Restaurant;

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

    public Observable<Lce<List<Restaurant>>> getRestaurantList(int id) {
        return Observable.just(RestaurantRepository.createRestaurantList())
                .delay(700, TimeUnit.MILLISECONDS)
                .map(Lce::data)
                .startWith(Lce.loading())
                .onErrorReturn(Lce::error);
    }
}
