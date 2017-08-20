package io.yon.android.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

import io.yon.android.R;
import io.yon.android.model.Restaurant;
import io.yon.android.view.adapter.MonthAdapter;
import io.yon.android.view.adapter.ReservationPagesAdapter;

/**
 * Created by amirhosein on 8/19/2017 AD.
 */

public class ReservationActivity extends Activity {

    private Restaurant mRestaurant;
    private MonthAdapter adapter;
    private ViewPager viewPager;

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

        initViews();
    }

    @Override
    protected void findViews() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
    }

    public void initViews() {
        viewPager.setAdapter(new ReservationPagesAdapter(getSupportFragmentManager()));
    }
}
