package io.yon.android.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

import io.yon.android.R;
import io.yon.android.model.Restaurant;
import io.yon.android.model.Tag;
import io.yon.android.util.RxBus;
import io.yon.android.util.ViewUtils;
import io.yon.android.view.LinearDividerItemDecoration;
import io.yon.android.view.RestaurantListItemConfig;
import io.yon.android.view.RestaurantListView;
import io.yon.android.view.adapter.Adapter;
import io.yon.android.view.adapter.viewholder.ItemRestaurantViewHolder;

/**
 * Created by amirhosein on 8/27/2017 AD.
 */

public abstract class RestaurantListActivity extends Activity implements RestaurantListView {

    private RxBus bus = new RxBus();
    private Adapter<Restaurant, ItemRestaurantViewHolder> adapter;
    private RecyclerView recyclerView;
    private View emptyStateContainer, errorContainer;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        bus.toObservable().subscribe(this::handleClick);
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

        makeContentVisible();

        adapter.setDataAndNotify(restaurants);
    }

    private void initView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new Adapter<>(this, null, bus, ItemRestaurantViewHolder.getFactory(getConfig()));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new LinearDividerItemDecoration(
                this,
                ContextCompat.getColor(this, R.color.restaurant_list_divider_color),
                ViewUtils.px(this, 0.8f)));
    }

    protected void clearVisibilities() {
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        errorContainer.setVisibility(View.INVISIBLE);
        emptyStateContainer.setVisibility(View.INVISIBLE);
    }

    protected void makeContentVisible() {
        recyclerView.setVisibility(View.VISIBLE);
    }

    protected RestaurantListItemConfig getConfig() {
        return RestaurantListItemConfig.defaultConfig();
    }

    protected void handleClick(Object o) {
        if (o instanceof Restaurant)
            onRestaurantClick((Restaurant) o);
        else if (o instanceof Tag)
            onTagClick((Tag) o);
    }

    protected void onRestaurantClick(Restaurant restaurant) {}

    protected void onTagClick(Tag tag) {}
}
