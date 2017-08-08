package io.yon.android.view.fragment;

import io.yon.android.R;

/**
 * Created by amirhosein on 8/8/17.
 */

public class RestaurantReviewFragment extends Fragment {

    public static RestaurantReviewFragment create() {
        return new RestaurantReviewFragment();
    }

    @Override
    protected int getResourceLayoutId() {
        return R.layout.fragment_restaurant_review;
    }
}
