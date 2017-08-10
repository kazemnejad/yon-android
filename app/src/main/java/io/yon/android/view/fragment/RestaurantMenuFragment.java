package io.yon.android.view.fragment;

import io.yon.android.R;
import io.yon.android.model.Restaurant;

/**
 * Created by amirhosein on 8/8/17.
 */

public class RestaurantMenuFragment extends Fragment {

    private Restaurant mRestaurant;

    public static RestaurantMenuFragment create() {
        return new RestaurantMenuFragment();
    }

    public RestaurantMenuFragment setRestaurant(Restaurant mRestaurant) {
        this.mRestaurant = mRestaurant;
        return this;
    }

    @Override
    protected int getResourceLayoutId() {
        return R.layout.fragment_restaurant_menu;
    }
}
