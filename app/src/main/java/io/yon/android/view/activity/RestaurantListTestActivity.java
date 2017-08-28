package io.yon.android.view.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;

import io.yon.android.R;
import io.yon.android.contract.TestContract;
import io.yon.android.model.Restaurant;
import io.yon.android.model.Tag;
import io.yon.android.presenter.TestPresenter;
import io.yon.android.view.RestaurantListItemConfig;

/**
 * Created by amirhosein on 8/27/2017 AD.
 */

public class RestaurantListTestActivity extends RestaurantListActivity implements TestContract.View {

    private TestPresenter presenter;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_restuarant_list_test;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("تست لیست");

        presenter = ViewModelProviders.of(this).get(TestPresenter.class);
        presenter.bindView(this);

        presenter.loadTestRestaurantList();
    }

    @Override
    public void showSomeDummy() {

    }

    @Override
    protected RestaurantListItemConfig getConfig() {
        return super.getConfig()
                .setShowTags(true);
    }

    @Override
    protected void onRestaurantClick(Restaurant restaurant) {
        Logger.d(restaurant.getName());
    }

    @Override
    protected void onTagClick(Tag tag) {
        Logger.d(tag.getName());
    }
}
