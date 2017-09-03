package io.yon.android.view.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.orhanobut.logger.Logger;

import java.util.List;

import io.yon.android.R;
import io.yon.android.contract.ShowcaseContract;
import io.yon.android.model.Restaurant;
import io.yon.android.model.Zone;
import io.yon.android.presenter.ShowcasePresenter;
import io.yon.android.util.Auth;
import io.yon.android.util.RxBus;
import io.yon.android.view.GlideApp;
import io.yon.android.view.adapter.ShowcaseAdapter;
import io.yon.android.view.widget.ShowcaseOnScrollListener;


public class MainActivity extends Activity implements ShowcaseContract.View {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;

    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout errorContainer;
    private Button btnRetry;
    private ImageButton btnSearch;
    private View appBar;
    private TextView btnSelectZone;

    private ShowcaseAdapter mAdapter;
    private RxBus mClickEventBus = new RxBus();

    private ShowcasePresenter presenter;

    public static void start(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    @Override
    protected int[] getCheckedMenuItems() {
        return new int[]{
                R.id.menu_item_home, R.id.menu_item_mb_home
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!checkPlayServiceAvailable())
            return;

//        setHasOptionMenu(true);

        initView();

//        Config.getCache(this).edit().remove(Config.Field.ShowCase).commit();

        presenter = ViewModelProviders.of(this).get(ShowcasePresenter.class);
        presenter.bindView(this);

        presenter.fetchData();
    }

    @Override
    protected void findViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.mother_recycler_view);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        errorContainer = (LinearLayout) findViewById(R.id.error_container);
        btnSelectZone = (TextView) findViewById(R.id.toolbar_text_main);
        btnRetry = (Button) findViewById(R.id.btn_retry);
        appBar = findViewById(R.id.appbar);
        btnSearch = (ImageButton) findViewById(R.id.toolbar_icon_left);
    }

    private void initView() {
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setItemPrefetchEnabled(true);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.addOnScrollListener(new ShowcaseOnScrollListener(this) {
            @Override
            protected View findViewById(int id) {
                return MainActivity.this.findViewById(id);
            }
        });

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR2)
            enableGlideBalance();

        mAdapter = new ShowcaseAdapter(this, mClickEventBus, null);
        mAdapter.setHasStableIds(true);

        mRecyclerView.setAdapter(mAdapter);

        btnSearch.setOnClickListener(v -> SearchActivity.start(this));
        btnRetry.setOnClickListener(v -> presenter.fetchData());
        btnSelectZone.setOnClickListener(v -> handleSelectZoneClick());

        swipeRefreshLayout.setOnRefreshListener(() -> presenter.reFetchData());
    }

    @Override
    public boolean onCreateOptionMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.create_new:
                RestaurantViewActivity.start(this, new Restaurant());
                return true;

            case R.id.open:
                Auth.login(this, isSuccessful -> Logger.i("auth result", isSuccessful));
                return true;
        }
        return false;
    }

    @Override
    public void showLoading() {
        invisibleAll();
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showReloading() {
        if (!swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void showError(Throwable e) {
        e.printStackTrace();
        invisibleAll();
        errorContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showReloadError(Throwable e) {
        if (swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
        e.printStackTrace();
        Snackbar.make(getRootView(), R.string.unable_to_connect_to_server, Snackbar.LENGTH_LONG)
                .setAction(R.string.retry, v -> presenter.reFetchData())
                .show();
    }

    @Override
    public void showData(List<Object> data, Zone location) {
        invisibleAll();

        if (location != null)
            btnSelectZone.setText(location.getName());

        if (swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);

        appBar.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);

        mAdapter.setDataAndUpdate(data);
    }

    protected boolean checkPlayServiceAvailable() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private void invisibleAll() {
        mProgressBar.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.INVISIBLE);
        errorContainer.setVisibility(View.GONE);
        appBar.setVisibility(View.INVISIBLE);
    }

    private void enableGlideBalance() {
        new Thread(() -> {
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE)
                        GlideApp.with(MainActivity.this).resumeRequests();

                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING)
                        GlideApp.with(MainActivity.this).pauseRequests();

                    super.onScrollStateChanged(recyclerView, newState);
                }
            });
        }).start();
    }

    private void handleSelectZoneClick() {
        ZoneSelectActivity.start(this, zone -> {
            if (btnSelectZone == null)
                return;

            if (zone != null)
                btnSelectZone.setText(zone.getName());


            presenter.reFetchData();
        });
    }
}
