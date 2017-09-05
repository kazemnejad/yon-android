package io.yon.android.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.AppCompatButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.parceler.Parcels;

import io.yon.android.R;
import io.yon.android.model.Restaurant;
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
    private TabLayout tabLayout;
    private RestaurantViewPagesAdapter mAdapter;

    private TextView toolbarTitle;
    private AppBarLayout appBar;
    private AppCompatButton btnToolbarReserve;
    private AppCompatButton btnReserve;

    private ImageView mBanner, mIcon;
    private TextView title, subTitle, rateLabel, priceLabel;

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
//        mRestaurant = createRestaurant();

        setDisplayHomeAsUpEnabled(true);

        initViews();
        fillViewWithOfflineContent();
    }

    @Override
    protected void findViews() {
        appBar = (AppBarLayout) findViewById(R.id.appbar);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_text_main);
        btnToolbarReserve = (AppCompatButton) findViewById(R.id.small_reserve);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        mBanner = (ImageView) findViewById(R.id.banner);
        mIcon = (ImageView) findViewById(R.id.icon);
        title = (TextView) findViewById(R.id.title);
        subTitle = (TextView) findViewById(R.id.sub_title);
        rateLabel = (TextView) findViewById(R.id.rate);
        priceLabel = (TextView) findViewById(R.id.price_Label);
        btnReserve = (AppCompatButton) findViewById(R.id.btn_reserve);
    }

    private void initViews() {
        mAdapter = new RestaurantViewPagesAdapter(this, getSupportFragmentManager(), mRestaurant);

        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(2);

        tabLayout.setupWithViewPager(mViewPager);

        btnToolbarReserve.setOnClickListener(v -> {
            if (mRestaurant.getMaps() == null)
                return;

            ReservationActivity.start(this, mRestaurant);
        });

        btnReserve.setOnClickListener(v -> {
            if (mRestaurant.getMaps() == null)
                return;

            ReservationActivity.start(this, mRestaurant);
        });

        final int actionBarSize = getToolbarHeight();
        appBar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.COLLAPSED) {
                    toolbarTitle.animate()
                            .translationY(0)
                            .setInterpolator(new FastOutSlowInInterpolator())
                            .alpha(1f)
                            .setDuration(200)
                            .start();

                    btnToolbarReserve.animate()
                            .translationY(0)
                            .setInterpolator(new FastOutSlowInInterpolator())
                            .alpha(1f)
                            .setDuration(200)
                            .setStartDelay(80)
                            .start();
                } else {
                    btnToolbarReserve.setTranslationY(ViewUtils.px(RestaurantViewActivity.this, actionBarSize));
                    btnToolbarReserve.setAlpha(0f);

                    toolbarTitle.setTranslationY(actionBarSize);
                    toolbarTitle.setAlpha(0f);
                }
            }
        });

        ImageView iv = (ImageView) findViewById(R.id.banner);
        GlideApp.with(this)
                .load(mRestaurant.getBannerUrl())
                .centerCrop()
                .into(iv);

        ImageView iv2 = (ImageView) findViewById(R.id.icon);
        GlideApp.with(this)
                .load(mRestaurant.getAvatarUrl())
                .centerCrop()
                .transform(new RoundedCornersTransformation(this, 30, 0))
                .into(iv2);
    }

    private void fillViewWithOfflineContent() {
        title.setText(mRestaurant.getName());
        toolbarTitle.setText(mRestaurant.getName());
        subTitle.setText(mRestaurant.getZoneLabel());
        rateLabel.setText(mRestaurant.getRateLabel());
        priceLabel.setText(mRestaurant.getPriceLabel());


        GlideApp.with(this)
                .asBitmap()
                .load(mRestaurant.getAvatarUrl())
                .centerCrop()
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
    }

    public void setUpdatedRestaurant(Restaurant restaurant) {
        mRestaurant = restaurant;
    }

    private static class RestaurantViewPagesAdapter extends FragmentStatePagerAdapter {

        private RestaurantInfoFragment infoFragment;
        private RestaurantMenuFragment menuFragment;
        private RestaurantReviewFragment reviewFragment;

        private String infoFragmentTitle;
        private String menuFragmentTitle;
        private String reviewFragmentTitle;

        private Restaurant mRestaurant;

        public RestaurantViewPagesAdapter(Context context, FragmentManager fm, Restaurant restaurant) {
            super(fm);

            infoFragmentTitle = context.getString(R.string.info);
            menuFragmentTitle = context.getString(R.string.restaurant_menu);
            reviewFragmentTitle = context.getString(R.string.review);

            mRestaurant = restaurant;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (reviewFragment == null)
                        reviewFragment = RestaurantReviewFragment.create(mRestaurant);

                    return reviewFragment;

                case 1:
                    if (menuFragment == null)
                        menuFragment = RestaurantMenuFragment.create(mRestaurant);

                    return menuFragment;

                case 2:
                    if (infoFragment == null)
                        infoFragment = RestaurantInfoFragment.create(mRestaurant);

                    return infoFragment;
            }

            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return reviewFragmentTitle;

                case 1:
                    return menuFragmentTitle;

                case 2:
                    return infoFragmentTitle;
            }

            return "";
        }
    }
}
