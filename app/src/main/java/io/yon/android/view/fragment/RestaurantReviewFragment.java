package io.yon.android.view.fragment;

import io.yon.android.R;
import io.yon.android.model.Restaurant;

/**
 * Created by amirhosein on 8/8/17.
 */

public class RestaurantReviewFragment extends Fragment {

    private Restaurant mRestaurant;

    public static RestaurantReviewFragment create() {
        return new RestaurantReviewFragment();
    }

    public RestaurantReviewFragment setRestaurant(Restaurant restaurant){
        mRestaurant = restaurant;
        return this;
    }

    @Override
    protected int getResourceLayoutId() {
        return R.layout.fragment_restaurant_review;
    }
}
