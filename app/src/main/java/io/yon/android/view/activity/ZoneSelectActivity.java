package io.yon.android.view.activity;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tbruyelle.rxpermissions2.RxPermissions;

import org.parceler.Parcels;

import java.util.List;

import io.yon.android.R;
import io.yon.android.contract.ZoneSearchContract;
import io.yon.android.model.Zone;
import io.yon.android.presenter.ZoneSearchPresenter;
import io.yon.android.util.RxBus;
import io.yon.android.util.ViewUtils;
import io.yon.android.view.adapter.Adapter;
import io.yon.android.view.adapter.viewholder.ItemSearchResultZoneViewHolder;

/**
 * Created by amirhosein on 9/1/2017 AD.
 */

public class ZoneSelectActivity extends Activity implements ZoneSearchContract.View {

    private static int SUCCESSFUL = 5;

    boolean isClearButtonVisible = false;

    private ImageButton btnClear;
    private EditText searchBar;

    private ProgressBar progressBar;
    private View errorContainer, emptyStateContainer, defaultOption;
    private Button btnRetry;
    private TextView btnCurrentLocation;
    private RecyclerView recyclerView;
    private Adapter<Zone, ItemSearchResultZoneViewHolder> adapter;
    private RxBus bus = new RxBus();

    private ZoneSearchPresenter presenter;

    public static void start(Activity activity, OnZoneSelectListener listener) {
        if (activity == null)
            return;

        if (listener != null) {
            int requestCode = activity.getNewRequestCode();
            activity.addOnActivityResultListener(requestCode, (resultCode, data) -> {
                if (resultCode != SUCCESSFUL)
                    return;
                Zone selectedZone = null;
                try {
                    selectedZone = Parcels.unwrap(data.getParcelableExtra("zone"));
                } catch (Exception ignored) {
                }
                listener.onSelect(selectedZone);
            });
            activity.startActivityForResult(new Intent(activity, ZoneSelectActivity.class), requestCode);
        } else {
            activity.startActivity(new Intent(activity, ZoneSelectActivity.class));
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_zone_select;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setDisplayHomeAsUpEnabled(true);

        initViews();

        presenter = ViewModelProviders.of(this).get(ZoneSearchPresenter.class);
        presenter.bindView(this);

        bus.toObservable().subscribe(this::handleZoneClick);

        presenter.loadZones("");
    }

    @Override
    protected void findViews() {
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        btnClear = (ImageButton) findViewById(R.id.toolbar_icon_left);
        btnCurrentLocation = (TextView) findViewById(R.id.btn_current_location);
        searchBar = (EditText) findViewById(R.id.toolbar_et_search);
        errorContainer = findViewById(R.id.error_container);
        emptyStateContainer = findViewById(R.id.empty_state_container);
        defaultOption = findViewById(R.id.default_options);
        btnRetry = (Button) findViewById(R.id.btn_retry);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    }

    @Override
    public void showError(Throwable error) {
        clearVisibilities();
        error.printStackTrace();
        errorContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoading() {
        clearVisibilities();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showData(List<Zone> data) {
        clearVisibilities();

        if (data.size() == 0) {
            emptyStateContainer.setVisibility(View.VISIBLE);
            return;
        }

        recyclerView.setVisibility(View.VISIBLE);
        adapter.setDataAndNotify(data);
    }

    protected void initViews() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new Adapter<>(this, null, bus, ItemSearchResultZoneViewHolder.getFactory());
        recyclerView.setAdapter(adapter);

        btnCurrentLocation.setOnClickListener(v -> handleCurrentLocationClick());
        btnRetry.setOnClickListener(v -> presenter.loadZones(true, searchBar.getText().toString()));
        btnClear.setOnClickListener(v -> searchBar.setText(""));

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0)
                    showClearButton();
                else
                    hideClearButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchBar.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch();
                return true;
            }
            return false;
        });
    }

    protected void performSearch() {
        ViewUtils.closeKeyboard(this);
        presenter.loadZones(true, searchBar.getText().toString());
    }

    protected void showClearButton() {
        if (isClearButtonVisible)
            return;

        isClearButtonVisible = true;

        btnClear.setAlpha(0f);
        btnClear.setScaleX(0);
        btnClear.setScaleY(0);

        btnClear.animate()
                .alpha(1f)
                .scaleX(1)
                .scaleY(1)
                .setDuration(200)
                .setInterpolator(new DecelerateInterpolator());
    }

    protected void hideClearButton() {
        isClearButtonVisible = false;

        btnClear.setAlpha(1f);
        btnClear.setScaleX(1);
        btnClear.setScaleY(1);

        btnClear.animate()
                .alpha(0f)
                .scaleX(0)
                .scaleY(0)
                .setDuration(200)
                .setInterpolator(new AccelerateInterpolator());
    }

    protected void clearVisibilities() {
        progressBar.setVisibility(View.INVISIBLE);
        errorContainer.setVisibility(View.INVISIBLE);
        emptyStateContainer.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        defaultOption.setVisibility(View.INVISIBLE);
    }

    private void handleZoneClick(Object o) {
        Zone zone;
        try {
            zone = adapter.getData().get((Integer) o);
        } catch (Exception ignored) {
            return;
        }

        setResult(SUCCESSFUL, new Intent()
                .putExtra("zone", Parcels.wrap(zone))
        );
        finish();
    }

    private void handleCurrentLocationClick() {
        new RxPermissions(this)
                .request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(granted -> {
                    if (granted) {
                        setResult(SUCCESSFUL);
                        finish();
                    }
                });
    }

    public interface OnZoneSelectListener {
        void onSelect(Zone zone);
    }
}
