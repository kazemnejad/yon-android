package io.yon.android.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.yon.android.R;
import io.yon.android.contract.RestaurantContract;
import io.yon.android.model.Map;
import io.yon.android.model.Restaurant;
import io.yon.android.model.Tag;
import io.yon.android.presenter.RestaurantPresenter;
import io.yon.android.util.ViewUtils;
import io.yon.android.view.adapter.RestaurantMapPagerAdapter;

/**
 * Created by amirhosein on 8/8/17.
 */

public class RestaurantInfoFragment extends Fragment implements RestaurantContract.View {

    private Restaurant mRestaurant;

    private FlexboxLayout tagsContainer;
    private ViewPager mapsContainer;
    private TabLayout mapSwitcher;

    private RestaurantPresenter presenter;
    private int maxUnitSize;

    public static RestaurantInfoFragment create() {
        return new RestaurantInfoFragment();
    }

    public RestaurantInfoFragment setRestaurant(Restaurant mRestaurant) {
        this.mRestaurant = mRestaurant;
        return this;
    }

    @Override
    protected int getResourceLayoutId() {
        return R.layout.fragment_restaurant_info;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.maxUnitSize = ViewUtils.px(getContext(), 60);

        initView();

        presenter = ViewModelProviders.of(this).get(RestaurantPresenter.class);
        presenter.bindView(this);

//        if (mRestaurant != null && mRestaurant.getId() != -1)
        if (mRestaurant != null)
            presenter.loadRestaurant(mRestaurant.getId());
    }

    @Override
    protected void findViews(View v) {
        tagsContainer = (FlexboxLayout) v.findViewById(R.id.tags_container);
        mapsContainer = (ViewPager) v.findViewById(R.id.maps_container);
        mapSwitcher = (TabLayout) v.findViewById(R.id.restaurant_maps_switcher);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showError(Throwable e) {

    }

    @Override
    public void showRestaurant(Restaurant restaurant) {
        mRestaurant = restaurant;
        fillUpRestaurantContent();
    }

    @Override
    public void showRestaurantMenu() {}

    @Override
    public void showRestaurantReview() {}

    private void initView() {

    }

    protected void fillUpRestaurantContent() {
        if (mRestaurant.getTags() != null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());

            for (Tag tag : mRestaurant.getTags()) {
                TextView tagLabelView = (TextView) inflater.inflate(R.layout.item_tag_label, tagsContainer, false);
                tagLabelView.setText(tag.getName());
                tagLabelView.setTag(tag);
                tagLabelView.setOnClickListener(this::handleTagClick);

                tagsContainer.addView(tagLabelView);
            }
        }

        if (mRestaurant.getMaps() != null) {
            float mapsContainerHeight = 0;
            for (Map map : mRestaurant.getMaps()) {
                float mapViewHeight = getMapViewHeight(map);
                if (mapViewHeight > mapsContainerHeight)
                    mapsContainerHeight = mapViewHeight;
            }
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mapsContainer.getLayoutParams();
            params.height = (int) mapsContainerHeight + ViewUtils.px(getContext(), 30);
            mapsContainer.setLayoutParams(params);

            RestaurantMapPagerAdapter adapter = new RestaurantMapPagerAdapter(getContext(), mRestaurant.getMaps());
            mapsContainer.setAdapter(adapter);
            mapsContainer.setCurrentItem(mRestaurant.getMaps().size() - 1);
            mapSwitcher.setupWithViewPager(mapsContainer);
        }
    }

    private float getMapViewHeight(Map map) {
        int viewWidth = ViewUtils.getScreenWidth(getContext());

        float k = 1.0f * viewWidth / map.getWidth();
        float unitSize = Math.min(k, maxUnitSize);

        return unitSize * map.getHeight();
    }

    protected void handleTagClick(View view) {

    }
}
