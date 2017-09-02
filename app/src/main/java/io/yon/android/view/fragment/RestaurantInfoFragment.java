package io.yon.android.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import org.parceler.Parcels;

import java.util.List;

import io.yon.android.R;
import io.yon.android.api.Constants;
import io.yon.android.contract.RestaurantContract;
import io.yon.android.model.Map;
import io.yon.android.model.MenuSection;
import io.yon.android.model.Restaurant;
import io.yon.android.model.Tag;
import io.yon.android.model.UserReview;
import io.yon.android.presenter.RestaurantPresenter;
import io.yon.android.util.ViewUtils;
import io.yon.android.view.GlideApp;
import io.yon.android.view.activity.RestaurantViewActivity;
import io.yon.android.view.adapter.RestaurantMapPagerAdapter;

import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;

/**
 * Created by amirhosein on 8/8/17.
 */

public class RestaurantInfoFragment extends Fragment implements RestaurantContract.View {

    private Restaurant mRestaurant;

    private ProgressBar progressBar;
    private LinearLayout mainContentContainer, errorContainer;
    private FlexboxLayout tagsContainer;
    private ViewPager mapsContainer;
    private TabLayout mapSwitcher;
    private ImageView staticMap;
    private TextView tvAddress, btnDirections, tvPhoneNumber, tvPriceRange, tvParkingSpace, tvOpeningHour, tvDescription;
    private Button btnRetry;

    private RestaurantPresenter presenter;
    private int maxUnitSize;

    public static RestaurantInfoFragment create(Restaurant restaurant) {
        RestaurantInfoFragment infoFragment = new RestaurantInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("rest", Parcels.wrap(restaurant));
        infoFragment.setArguments(bundle);
        return infoFragment;
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

        mRestaurant = Parcels.unwrap(getArguments().getParcelable("rest"));

        maxUnitSize = ViewUtils.px(getContext(), 60);

        initView();

        presenter = ViewModelProviders.of(this).get(RestaurantPresenter.class);
        presenter.bindView(this);

        if (mRestaurant != null && mRestaurant.getId() != -1)
            presenter.loadRestaurant(mRestaurant.getId());
    }

    @Override
    protected void findViews(View v) {
        progressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
        mainContentContainer = (LinearLayout) v.findViewById(R.id.main_content_container);
        errorContainer = (LinearLayout) v.findViewById(R.id.error_container);
        btnRetry = (Button) v.findViewById(R.id.btn_retry);

        tagsContainer = (FlexboxLayout) v.findViewById(R.id.tags_container);

        mapsContainer = (ViewPager) v.findViewById(R.id.maps_container);
        mapSwitcher = (TabLayout) v.findViewById(R.id.restaurant_maps_switcher);

        staticMap = (ImageView) v.findViewById(R.id.static_map);
        tvAddress = (TextView) v.findViewById(R.id.address);
        btnDirections = (TextView) v.findViewById(R.id.btn_direction);

        tvPhoneNumber = (TextView) v.findViewById(R.id.phone);
        tvPriceRange = (TextView) v.findViewById(R.id.price_range);
        tvParkingSpace = (TextView) v.findViewById(R.id.parking_space);
        tvOpeningHour = (TextView) v.findViewById(R.id.open_hours);
        tvDescription = (TextView) v.findViewById(R.id.description);
    }

    @Override
    public void showLoading() {
        clearVisibilities();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void showRestaurant(Restaurant restaurant) {
        clearVisibilities();

        mRestaurant = restaurant;
        RestaurantViewActivity parent = getParentActivity();
        if (parent != null)
            parent.setUpdatedRestaurant(restaurant);

        fillUpRestaurantContent();
        mainContentContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRestaurantMenu(List<MenuSection> menu) {}

    @Override
    public void showRestaurantReview(List<UserReview> userReviews) {}

    private void initView() {
        btnRetry.setOnClickListener(view -> presenter.loadRestaurant(mRestaurant.getId()));
    }

    protected void clearVisibilities() {
        progressBar.setVisibility(View.INVISIBLE);
        errorContainer.setVisibility(View.INVISIBLE);
        mainContentContainer.setVisibility(View.INVISIBLE);
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

        if (mRestaurant.getLatitude() != -1 && mRestaurant.getLongitude() != -1)
            GlideApp.with(this)
                    .asBitmap()
                    .load(mRestaurant.getMapImageUrl())
                    .centerCrop()
                    .transition(withCrossFade())
                    .into(staticMap);

        tvAddress.setText(mRestaurant.getAddress());

        if (mRestaurant.getInfo() == null)
            return;

        if (mRestaurant.getInfo().containsKey("phone"))
            tvPhoneNumber.setText(mRestaurant.getInfo().get("phone"));

        if (mRestaurant.getInfo().containsKey("price_range"))
            tvPriceRange.setText(mRestaurant.getInfo().get("price_range"));

        if (mRestaurant.getInfo().containsKey("parking_space"))
            tvParkingSpace.setText(mRestaurant.getInfo().get("parking_space"));

        if (mRestaurant.getInfo().containsKey("opening_hour"))
            tvOpeningHour.setText(mRestaurant.getInfo().get("opening_hour"));

        if (mRestaurant.getInfo().containsKey("desc"))
            tvDescription.setText(mRestaurant.getInfo().get("desc"));
    }

    protected String getMapImageUrl(double lng, double lat) {
        String url = Constants.GoogleStaticMapUrl;
        url += "?scale=2";
        url += "&language=fa";
        url += "&markers=|" + String.valueOf(lng) + "," + String.valueOf(lat);
        url += "&key=" + Constants.GoogleStaticMapKey;

        return url;
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
