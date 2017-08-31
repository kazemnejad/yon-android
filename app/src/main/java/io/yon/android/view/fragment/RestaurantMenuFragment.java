package io.yon.android.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.orhanobut.logger.Logger;

import org.parceler.Parcels;
import org.zakariya.stickyheaders.StickyHeaderLayoutManager;

import java.util.List;

import io.yon.android.R;
import io.yon.android.contract.RestaurantContract;
import io.yon.android.model.MenuSection;
import io.yon.android.model.Restaurant;
import io.yon.android.model.UserReview;
import io.yon.android.presenter.RestaurantPresenter;
import io.yon.android.view.adapter.RestaurantMenuAdapter;

/**
 * Created by amirhosein on 8/8/17.
 */

public class RestaurantMenuFragment extends Fragment implements RestaurantContract.View {

    private Restaurant mRestaurant;

    private ProgressBar progressBar;
    private LinearLayout errorContainer;
    private Button btnRetry;

    private RecyclerView recyclerView;

    private RestaurantPresenter presenter;

    public static RestaurantMenuFragment create(Restaurant restaurant) {
        RestaurantMenuFragment fragment = new RestaurantMenuFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("rest", Parcels.wrap(restaurant));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getResourceLayoutId() {
        return R.layout.fragment_restaurant_menu;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRestaurant = Parcels.unwrap(getArguments().getParcelable("rest"));

        initViews();

        presenter = ViewModelProviders.of(this).get(RestaurantPresenter.class);
        presenter.bindView(this);

        presenter.loadRestaurantMenu(mRestaurant.getId());
    }

    @Override
    protected void findViews(View v) {
        progressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
        errorContainer = (LinearLayout) v.findViewById(R.id.error_container);
        btnRetry = (Button) v.findViewById(R.id.btn_retry);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
    }

    private void initViews() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StickyHeaderLayoutManager());
        btnRetry.setOnClickListener(view -> presenter.loadRestaurantMenu(mRestaurant.getId()));
    }

    @Override
    public void showLoading() {
        clearVisibilities();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(Throwable e) {
        clearVisibilities();
        e.printStackTrace();
        errorContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRestaurantMenu(List<MenuSection> menu) {
        clearVisibilities();

        recyclerView.setAdapter(new RestaurantMenuAdapter(getContext(), menu, null));
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRestaurant(Restaurant restaurant) {}

    @Override
    public void showRestaurantReview(List<UserReview> reviews) {}

    protected void clearVisibilities() {
        progressBar.setVisibility(View.INVISIBLE);
        errorContainer.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }
}
