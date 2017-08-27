package io.yon.android.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

import io.yon.android.R;
import io.yon.android.model.Restaurant;
import io.yon.android.view.RestaurantListView;

/**
 * Created by amirhosein on 8/27/2017 AD.
 */

public abstract class RestaurantListActivity extends Activity implements RestaurantListView {

    private RecyclerView recyclerView;
    private View emptyStateContainer, errorContainer;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    @Override
    protected void findViews() {
        progressBar = (ProgressBar) findViewById(R.id.rl_progress_bar);
        recyclerView = (RecyclerView) findViewById(R.id.rl_recycler_view);
        errorContainer = findViewById(R.id.error_container);
        emptyStateContainer = findViewById(R.id.empty_state_container);
    }

    @Override
    public void showRlLoading() {
        clearVisibilities();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRlError(Throwable e) {
        clearVisibilities();
        e.printStackTrace();
        errorContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRl(List<Restaurant> restaurants) {
        clearVisibilities();
    }

    private void initView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    protected void clearVisibilities() {
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        errorContainer.setVisibility(View.INVISIBLE);
        emptyStateContainer.setVisibility(View.INVISIBLE);
    }
}
