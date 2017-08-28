package io.yon.android.view.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import io.yon.android.R;
import io.yon.android.contract.ZoneContract;
import io.yon.android.model.Zone;
import io.yon.android.presenter.ZonePresenter;
import io.yon.android.util.ViewUtils;
import io.yon.android.view.GlideApp;
import io.yon.android.view.widget.AppBarStateChangeListener;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by amirhosein on 8/28/2017 AD.
 */

public class ZoneViewActivity extends RestaurantListActivity implements ZoneContract.View {

    private TextView appbarTitle;
    private TextView toolbarTile;
    private ImageView zoneAvatar;
    private Zone zone;
    private AppBarLayout appbar;

    private ZonePresenter presenter;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_zone_view;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        zone = new Zone();
        zone.setLongitude(35.7009524);
        zone.setLatitude(51.3897519);
        zone.setName("انقلاب");

        initView();

        setDisplayHomeAsUpEnabled(true);
        setTitle(zone.getName());

        presenter = ViewModelProviders.of(this).get(ZonePresenter.class);
        presenter.bindView(this);

        presenter.loadRestaurantsInZone(zone);
    }

    @Override
    protected void findViews() {
        super.findViews();
        appbar = (AppBarLayout) findViewById(R.id.appbar);
        appbarTitle = (TextView) findViewById(R.id.toolbar_text_main2);
        toolbarTile = (TextView) findViewById(R.id.toolbar_text_main);
        zoneAvatar = (ImageView) findViewById(R.id.zone_avatar);
    }

    private void initView() {
        appbarTitle.setText(zone.getName());
        GlideApp.with(this)
                .load(zone.getGoogleMapImageUrl())
                .placeholder(R.color.solidPlaceHolder)
                .transition(withCrossFade())
                .centerCrop()
                .into(zoneAvatar);

        final int actionBarSize = ViewUtils.px(this, 10);
        appbar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.COLLAPSED) {
                    toolbarTile.animate()
                            .alpha(1f)
                            .translationY(0)
                            .setDuration(300)
                            .setInterpolator(new AccelerateDecelerateInterpolator());
                    appbarTitle.setAlpha(0);
                } else {
                    toolbarTile.setAlpha(0);
                    toolbarTile.setTranslationY(actionBarSize);
                    appbarTitle.setAlpha(1);
                }
            }
        });
    }

    @Override
    protected void onBtnRetryClick() {
        presenter.loadRestaurantsInZone(zone);
    }
}
