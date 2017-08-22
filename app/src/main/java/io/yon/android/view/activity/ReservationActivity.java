package io.yon.android.view.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.yon.android.R;
import io.yon.android.model.Restaurant;
import io.yon.android.presenter.ReservationPresenter;
import io.yon.android.repository.RestaurantRepository;
import io.yon.android.view.adapter.ReservationPagesAdapter;
import io.yon.android.view.widget.TextViewCompatTint;

/**
 * Created by amirhosein on 8/19/2017 AD.
 */

public class ReservationActivity extends Activity implements ReservationBuilderController {

    private Restaurant mRestaurant;
    private ReservationPagesAdapter adapter;
    private ViewPager viewPager;
    private ImageButton btnClose;
    private LinearLayout indicator;

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

        mRestaurant = RestaurantRepository.createRestaurant();
//        mRestaurant = Parcels.unwrap(getIntent().getParcelableExtra("rest"));

        setDisplayHomeAsUpEnabled(true);
        setTitle(mRestaurant.getName());

        mPresenter = ViewModelProviders.of(this).get(ReservationPresenter.class);
        mPresenter.setRestaurant(mRestaurant);

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
        btnClose = (ImageButton) findViewById(R.id.toolbar_icon_right);
        indicator = (LinearLayout) findViewById(R.id.indicator_container);
    }

    @Override
    public void next() {
        int currentStep = mPresenter.getCurrentStep();
        if (currentStep == 0)
            return;

        mPresenter.setCurrentStep(currentStep - 1);
        viewPager.setCurrentItem(currentStep - 1, true);

        updateIndicator();
    }

    @Override
    public void previous() {
        int currentStep = mPresenter.getCurrentStep();
        if (currentStep == ReservationPresenter.Step.DateSelect)
            return;

        mPresenter.setCurrentStep(currentStep + 1);
        viewPager.setCurrentItem(currentStep + 1, true);

        updateIndicator();
    }

    @Override
    public void goToStep(int step) {
        mPresenter.setCurrentStep(step);
        viewPager.setCurrentItem(step, true);

        updateIndicator();
    }

    public void initViews() {
        adapter = new ReservationPagesAdapter(getSupportFragmentManager());

        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(mPresenter.getCurrentStep());

        btnClose.setImageResource(R.drawable.ic_close_24dp);
        btnClose.setOnClickListener(v -> finish());

        initIndicator();
        updateIndicator();
    }

    public void initIndicator() {
        int disableColor = ContextCompat.getColor(this, R.color.reservation_indicator_item_disabled_color);
        for (int i = 0; i < indicator.getChildCount(); i++)
            try {
                TextViewCompatTint item = (TextViewCompatTint) indicator.getChildAt(i);
                item.setClickable(false);
                item.setDrawableTint(disableColor);
            } catch (Exception ignored) {
            }
    }

    public void updateIndicator() {
        int primaryColor = ContextCompat.getColor(this, R.color.colorPrimary);
        int disableColor = ContextCompat.getColor(this, R.color.reservation_indicator_item_disabled_color);

        int step = ReservationPresenter.Step.DateSelect;
        int itemIndex = indicator.getChildCount() - 1;

        while (step >= mPresenter.getCurrentStep() && itemIndex >= 0) {
            View view = indicator.getChildAt(itemIndex--);
            if (!(view instanceof TextViewCompatTint))
                continue;

            int finalStep = step;
            TextViewCompatTint item = (TextViewCompatTint) view;
            item.setDrawableTint(primaryColor);
            item.setClickable(true);
            item.setOnClickListener(v -> goToStep(finalStep));

            step--;
        }

        for (int i = itemIndex; i >= 0; i--) {
            try {
                TextViewCompatTint item = (TextViewCompatTint) indicator.getChildAt(i);
                item.setClickable(false);
                item.setDrawableTint(disableColor);
            } catch (Exception ignored) {
            }
        }
    }
}
