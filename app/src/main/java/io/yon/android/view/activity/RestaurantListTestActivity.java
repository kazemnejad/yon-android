package io.yon.android.view.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;

import io.yon.android.R;
import io.yon.android.presenter.TestPresenter;

/**
 * Created by amirhosein on 8/27/2017 AD.
 */

public class RestaurantListTestActivity extends RestaurantListActivity {

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


    }
}
