package io.yon.android.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import org.parceler.Parcels;

import io.yon.android.R;
import io.yon.android.model.Restaurant;

/**
 * Created by amirhosein on 8/8/17.
 */

public class RestaurantReviewFragment extends Fragment {

    private Restaurant mRestaurant;

    public static RestaurantReviewFragment create(Restaurant restaurant) {
        RestaurantReviewFragment fragment = new RestaurantReviewFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("rest", Parcels.wrap(restaurant));
        fragment.setArguments(bundle);
        return fragment;
    }

    public RestaurantReviewFragment setRestaurant(Restaurant restaurant) {
        mRestaurant = restaurant;
        return this;
    }

    @Override
    protected int getResourceLayoutId() {
        return R.layout.fragment_restaurant_review;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void findViews(View v) {

    }


}
