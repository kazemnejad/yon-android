package io.yon.android.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import io.yon.android.R;
import io.yon.android.contract.RestaurantContract;
import io.yon.android.model.Restaurant;
import io.yon.android.model.Tag;
import io.yon.android.presenter.RestaurantPresenter;

/**
 * Created by amirhosein on 8/8/17.
 */

public class RestaurantInfoFragment extends Fragment implements RestaurantContract.View {

    private Restaurant mRestaurant;

    private FlexboxLayout tagsContainer;

    private RestaurantPresenter presenter;

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


    }

    protected void handleTagClick(View view) {

    }
}
