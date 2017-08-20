package io.yon.android.view.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

import io.yon.android.R;
import io.yon.android.model.Restaurant;
import io.yon.android.presenter.ReservationPresenter;
import io.yon.android.view.adapter.ReservationPagesAdapter;

/**
 * Created by amirhosein on 8/19/2017 AD.
 */

public class ReservationActivity extends Activity implements ReservationBuilderController {

    private Restaurant mRestaurant;
    private ReservationPagesAdapter adapter;
    private ViewPager viewPager;

    private ReservationPresenter mPresenter;

    public static void start(Context context, Restaurant restaurant) {
        context.startActivity(new Intent(context, ReservationActivity.class)
                .putExtra("rest", restaurant)
        );
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_reservatiuon;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRestaurant = new Restaurant();
        mRestaurant.setId(5);
        mRestaurant.setName(getString(R.string.app_name));
        mRestaurant.setRate(3.4f);
        mRestaurant.setPrice(4.9f);
//        mRestaurant = Parcels.unwrap(getIntent().getParcelableExtra("rest"));

        setDisplayHomeAsUpEnabled(true);
        setTitle(mRestaurant.getName());

        mPresenter = ViewModelProviders.of(this).get(ReservationPresenter.class);

        initViews();
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == ReservationPresenter.Step.DateSelect)
            super.onBackPressed();
        else
            previous();
    }

    @Override
    protected void findViews() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
    }

    @Override
    public void next() {
        int currentStep = mPresenter.getCurrentStep();
        if (currentStep == 0)
            return;

        mPresenter.setCurrentStep(currentStep - 1);
        viewPager.setCurrentItem(currentStep - 1, true);
    }

    @Override
    public void previous() {
        int currentStep = mPresenter.getCurrentStep();
        if (currentStep == ReservationPresenter.Step.DateSelect)
            return;

        mPresenter.setCurrentStep(currentStep + 1);
        viewPager.setCurrentItem(currentStep + 1, true);
    }

    public void initViews() {
        adapter = new ReservationPagesAdapter(getSupportFragmentManager());

        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(mPresenter.getCurrentStep());
    }
}
