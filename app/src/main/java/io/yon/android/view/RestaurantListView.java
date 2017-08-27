package io.yon.android.view;

import java.util.List;

import io.yon.android.model.Restaurant;

/**
 * Created by amirhosein on 8/27/2017 AD.
 */

public interface RestaurantListView extends MvpView {
    void showRlLoading();

    void showRlError(Throwable e);

    void showRl(List<Restaurant> restaurants);
}
