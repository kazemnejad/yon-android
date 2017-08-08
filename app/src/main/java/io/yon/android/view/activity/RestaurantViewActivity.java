package io.yon.android.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.AppCompatButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.ArrayList;

import io.yon.android.R;
import io.yon.android.model.Map;
import io.yon.android.model.Restaurant;
import io.yon.android.model.Table;
import io.yon.android.util.ViewUtils;
import io.yon.android.view.GlideApp;
import io.yon.android.view.RoundedCornersTransformation;
import io.yon.android.view.fragment.RestaurantInfoFragment;
import io.yon.android.view.fragment.RestaurantMenuFragment;
import io.yon.android.view.fragment.RestaurantReviewFragment;
import io.yon.android.view.widget.AppBarStateChangeListener;

import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;

/**
 * Created by amirhosein on 8/6/17.
 */

public class RestaurantViewActivity extends Activity {

    private Restaurant mRestaurant;

    private ViewPager mViewPager;
    private RestaurantViewPagesAdapter mAdapter;

    private TextView toolbarTitle;
    private AppBarLayout appBar;
    private AppCompatButton btnToolbarReserve;

    private ImageView mBanner, mIcon;

    public static void start(Context context, Restaurant restaurant) {
        context.startActivity(
                new Intent(context, RestaurantViewActivity.class)
                        .putExtra("rest", Parcels.wrap(restaurant))
        );
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_restaurant_view;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRestaurant = Parcels.unwrap(getIntent().getParcelableExtra("rest"));

        setDisplayHomeAsUpEnabled(true);

        initViews();

        ImageView iv = (ImageView) findViewById(R.id.banner);
        GlideApp.with(this)
                .load("http://www.pizzaeast.com/system/files/032016/56fd2c58ebeeb56aa00d9df6/large/24.3.16_pizzaeast2052.jpg?1459432756")
                .centerCrop()
                .into(iv);

        ImageView iv2 = (ImageView) findViewById(R.id.icon);
        GlideApp.with(this)
                .load("http://162.243.174.32/restaurant_avatars/1166.jpeg")
                .centerCrop()
                .transform(new RoundedCornersTransformation(this, 30, 0))
                .into(iv2);
    }

    @Override
    protected void findViews() {
        appBar = (AppBarLayout) findViewById(R.id.appbar);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_text_main);
        btnToolbarReserve = (AppCompatButton) findViewById(R.id.small_reserve);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mBanner = (ImageView) findViewById(R.id.banner);
        mIcon = (ImageView) findViewById(R.id.icon);
    }

    private void initViews() {
        mAdapter = new RestaurantViewPagesAdapter(getSupportFragmentManager());

        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(2);

        GlideApp.with(this)
                .asBitmap()
                .load(mRestaurant.getAvatarUrl())
                .centerCrop()
                .placeholder(R.color.solidPlaceHolder)
                .transform(new RoundedCornersTransformation(this, 30, 0))
                .transition(withCrossFade())
                .into(mIcon);

        GlideApp.with(this)
                .asBitmap()
                .load(mRestaurant.getBannerUrl())
                .centerCrop()
                .placeholder(R.color.colorPrimary)
                .transition(withCrossFade())
                .into(mBanner);

        final int actionBarSize = getToolbarHeight();
        appBar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.COLLAPSED) {
                    btnToolbarReserve.animate()
                            .translationY(0)
                            .setInterpolator(new FastOutSlowInInterpolator())
                            .alpha(1f)
                            .setDuration(200)
                            .setStartDelay(100)
                            .start();

                    toolbarTitle.animate()
                            .translationY(0)
                            .setInterpolator(new FastOutSlowInInterpolator())
                            .alpha(1f)
                            .setDuration(200)
                            .start();
                } else {
                    btnToolbarReserve.setTranslationY(ViewUtils.px(RestaurantViewActivity.this, actionBarSize));
                    btnToolbarReserve.setAlpha(0f);

                    toolbarTitle.setTranslationY(actionBarSize);
                    toolbarTitle.setAlpha(0f);
                }
            }
        });
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
