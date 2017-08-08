package io.yon.android.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

import io.yon.android.R;
import io.yon.android.model.Map;
import io.yon.android.model.Table;
import io.yon.android.view.fragment.RestaurantInfoFragment;
import io.yon.android.view.fragment.RestaurantMenuFragment;
import io.yon.android.view.fragment.RestaurantReviewFragment;

/**
 * Created by amirhosein on 8/6/17.
 */

public class RestaurantViewActivity extends Activity {

    private ViewPager mViewPager;
    private RestaurantViewPagesAdapter mAdapter;

    public static void start(Context context) {
        context.startActivity(new Intent(context, RestaurantViewActivity.class));
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_restaurant_view;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setDisplayHomeAsUpEnabled(true);

        initViews();
    }

    @Override
    protected void findViews() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
    }

    private void initViews() {
        mAdapter = new RestaurantViewPagesAdapter(getSupportFragmentManager());

        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(2);
    }

    private Map createMap() {
        Map m = new Map();
        m.setWidth(4.6f);
        m.setHeight(2.5f);

        ArrayList<Table> tables = new ArrayList<>();
        tables.add(makeTable(0.5f, 0.5f));
        tables.add(makeTable(1.7f, 0.5f));
        tables.add(makeTable(2.9f, 0.5f));
        tables.add(makeTable(4.1f, 0.5f));
        Table t = makeTable(0.5f, 2f);
        t.setAngle(45);
        tables.add(t);

        m.setTables(tables);

        return m;
    }

    private Table makeTable(float x, float y) {
        Table t = new Table();
        t.setX(x);
        t.setY(y);
        t.setShape(1);
//        t.setAngle(45f);

        return t;
    }

    private static class RestaurantViewPagesAdapter extends FragmentStatePagerAdapter {

        private RestaurantInfoFragment infoFragment;
        private RestaurantMenuFragment menuFragment;
        private RestaurantReviewFragment reviewFragment;

        public RestaurantViewPagesAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (reviewFragment == null)
                        reviewFragment = RestaurantReviewFragment.create();

                    return reviewFragment;

                case 1:
                    if (menuFragment == null)
                        menuFragment = RestaurantMenuFragment.create();

                    return menuFragment;

                case 2:
                    if (infoFragment == null)
                        infoFragment = RestaurantInfoFragment.create();

                    return infoFragment;
            }

            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        public RestaurantInfoFragment getInfoFragment() {
            return infoFragment;
        }

        public RestaurantMenuFragment getMenuFragment() {
            return menuFragment;
        }

        public RestaurantReviewFragment getReviewFragment() {
            return reviewFragment;
        }
    }
}
