package io.yon.android.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.HashMap;
import java.util.List;

import io.yon.android.R;
import io.yon.android.contract.ReservationContract;
import io.yon.android.model.Map;
import io.yon.android.model.Table;
import io.yon.android.presenter.ReservationPresenter;
import io.yon.android.util.ViewUtils;
import io.yon.android.view.activity.ReservationBuilderController;
import io.yon.android.view.adapter.RestaurantMapPagerAdapter;
import io.yon.android.view.widget.RestaurantMapView;

/**
 * Created by amirhosein on 8/19/2017 AD.
 */

public class ReservationTableFragment extends Fragment implements ReservationContract.TableView, RestaurantMapView.OnTableClickListener {
    private int maxUnitSize;

    private ProgressBar progressBar;
    private LinearLayout errorContainer;
    private FrameLayout mapSwitcherContainer;
    private Button btnRetry, btnPrevious, btnNext;
    private ViewPager mapsContainer;
    private TabLayout mapSwitcher;

    private ReservationPresenter mPresenter;
    private ReservationBuilderController mController;

    private RestaurantMapView lastSelectedTableMapView;
    private ImageView lastSelectedTable;

    @Override
    protected int getResourceLayoutId() {
        return R.layout.fragment_reservation_table;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        maxUnitSize = ViewUtils.px(getContext(), 60);

        mController = getParentActivity();
        mPresenter = ViewModelProviders.of((FragmentActivity) getParentActivity()).get(ReservationPresenter.class);
        mPresenter.bindTableView(this);

        initView();

        mPresenter.loadForbiddenTables();
    }

    @Override
    protected void findViews(View v) {
        progressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
        errorContainer = (LinearLayout) v.findViewById(R.id.error_container);
        btnRetry = (Button) v.findViewById(R.id.btn_retry);
        mapsContainer = (ViewPager) v.findViewById(R.id.maps_container);
        mapSwitcher = (TabLayout) v.findViewById(R.id.restaurant_maps_switcher);
        mapSwitcherContainer = (FrameLayout) v.findViewById(R.id.restaurant_maps_switcher_container);
        btnNext = (Button) v.findViewById(R.id.btn_next);
        btnPrevious = (Button) v.findViewById(R.id.btn_previous);
    }

    @Override
    public void showLoading() {
        clearVisibilities();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(Throwable e) {
        clearVisibilities();
        errorContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showForbiddenTables(HashMap<String, Boolean> forbiddenTables) {
        clearVisibilities();

        mapsContainer.setVisibility(View.VISIBLE);
        mapSwitcherContainer.setVisibility(View.VISIBLE);

        float mapsContainerHeight = getMaxMapViewHeight(mPresenter.getRestaurant().getMaps());
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mapsContainer.getLayoutParams();
        params.height = (int) mapsContainerHeight + ViewUtils.px(getContext(), 30);
        mapsContainer.setLayoutParams(params);

        RestaurantMapPagerAdapter adapter = new RestaurantMapPagerAdapter(
                getContext(),
                mPresenter.getRestaurant().getMaps(),
                forbiddenTables,
                this,
                mPresenter.getSelectedTable()
        );
        mapsContainer.setAdapter(adapter);
        mapsContainer.setCurrentItem(mPresenter.getRestaurant().getMaps().size() - 1);

        mapSwitcher.setupWithViewPager(mapsContainer);

        updateNextButton();
    }

    protected void initView() {
        btnRetry.setOnClickListener(v -> mPresenter.loadForbiddenTables());
        btnNext.setOnClickListener(v -> mController.next());
        btnPrevious.setOnClickListener(v -> mController.previous());

        updateNextButton();
    }

    protected void clearVisibilities() {
        progressBar.setVisibility(View.INVISIBLE);
        errorContainer.setVisibility(View.INVISIBLE);
        mapsContainer.setVisibility(View.INVISIBLE);
        mapSwitcherContainer.setVisibility(View.INVISIBLE);
    }

    private float getMaxMapViewHeight(List<Map> maps) {
        float max = -1;
        for (Map map : maps) {
            float height = getMapViewHeight(map);
            if (height > max)
                max = height;
        }

        return max;
    }

    private float getMapViewHeight(Map map) {
        int viewWidth = ViewUtils.getScreenWidth(getContext());

        float k = 1.0f * viewWidth / map.getWidth();
        float unitSize = Math.min(k, maxUnitSize);

        return unitSize * map.getHeight();
    }

    @Override
    public void onClick(RestaurantMapView mapView, View v, Table table) {
        mapView.removeTableSelection();
        if (lastSelectedTableMapView != null)
            lastSelectedTableMapView.removeTableSelection();

        Table lastSelectedTable = mPresenter.getSelectedTable();
        if (lastSelectedTable == null || !lastSelectedTable.getId().equals(table.getId())) {
            mapView.setTableSelected(table);
            mPresenter.setSelectedTable(table);
        } else {
            mPresenter.setSelectedTable(null);
        }

        lastSelectedTableMapView = mapView;
        updateNextButton();
    }

    private void updateNextButton() {
        ViewUtils.setButtonEnabled(btnNext, mPresenter.getSelectedTable() != null);
    }
}
