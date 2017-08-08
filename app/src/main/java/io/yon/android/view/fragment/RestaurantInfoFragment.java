package io.yon.android.view.fragment;

import io.yon.android.R;

/**
 * Created by amirhosein on 8/8/17.
 */

public class RestaurantInfoFragment extends Fragment {

    public static RestaurantInfoFragment create() {
        return new RestaurantInfoFragment();
    }

    @Override
    protected int getResourceLayoutId() {
        return R.layout.fragment_restaurant_info;
    }
}
