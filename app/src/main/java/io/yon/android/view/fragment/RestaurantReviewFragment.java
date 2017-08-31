package io.yon.android.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import org.parceler.Parcels;

import java.util.List;

import io.yon.android.R;
import io.yon.android.contract.RestaurantContract;
import io.yon.android.model.MenuSection;
import io.yon.android.model.Restaurant;
import io.yon.android.model.UserReview;
import io.yon.android.presenter.RestaurantPresenter;
import io.yon.android.view.adapter.Adapter;
import io.yon.android.view.adapter.viewholder.ItemUserReviewViewHolder;

/**
 * Created by amirhosein on 8/8/17.
 */

public class RestaurantReviewFragment extends Fragment implements RestaurantContract.View {

    private Restaurant mRestaurant;

    private ProgressBar progressBar;
    private LinearLayout errorContainer;
    private Button btnRetry;

    private RecyclerView recyclerView;

    private RestaurantPresenter presenter;

    public static RestaurantReviewFragment create(Restaurant restaurant) {
        RestaurantReviewFragment fragment = new RestaurantReviewFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("rest", Parcels.wrap(restaurant));
        fragment.setArguments(bundle);
        return fragment;
    }

    public RestaurantReviewFragment setRestaurant(Restaurant restaurant) {
        mRestaurant = restaurant;
        return this;
    }

    @Override
    protected int getResourceLayoutId() {
        return R.layout.fragment_restaurant_review;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRestaurant = Parcels.unwrap(getArguments().getParcelable("rest"));

        initViews();

        presenter = ViewModelProviders.of(this).get(RestaurantPresenter.class);
        presenter.bindView(this);

        if (mRestaurant != null && mRestaurant.getId() != -1)
            presenter.loadRestaurantReview(mRestaurant.getId());
    }

    @Override
    protected void findViews(View v) {
        progressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
        errorContainer = (LinearLayout) v.findViewById(R.id.error_container);
        btnRetry = (Button) v.findViewById(R.id.btn_retry);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
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
    public void showRestaurant(Restaurant restaurant) {}

    @Override
    public void showRestaurantMenu(List<MenuSection> menu) {}

    @Override
    public void showRestaurantReview(List<UserReview> reviews) {
        clearVisibilities();

        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(new Adapter<>(getContext(), reviews, null, ItemUserReviewViewHolder.getFactory()));
    }

    private void initViews() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    protected void clearVisibilities() {
        progressBar.setVisibility(View.INVISIBLE);
        errorContainer.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }
}
