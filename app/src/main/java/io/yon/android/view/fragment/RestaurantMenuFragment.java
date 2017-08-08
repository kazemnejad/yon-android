package io.yon.android.view.fragment;

import io.yon.android.R;

/**
 * Created by amirhosein on 8/8/17.
 */

public class RestaurantMenuFragment extends Fragment {

    public static RestaurantMenuFragment create() {
        return new RestaurantMenuFragment();
    }

    @Override
    protected int getResourceLayoutId() {
        return R.layout.fragment_restaurant_menu;
    }
}
