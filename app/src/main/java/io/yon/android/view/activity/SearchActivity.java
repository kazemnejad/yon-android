package io.yon.android.view.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.util.List;

import io.yon.android.R;
import io.yon.android.contract.SearchContract;
import io.yon.android.model.Restaurant;
import io.yon.android.model.SearchResultSection;
import io.yon.android.model.Tag;
import io.yon.android.model.Zone;
import io.yon.android.presenter.SearchPresenter;
import io.yon.android.util.RxBus;
import io.yon.android.util.ViewUtils;
import io.yon.android.view.LinearDividerItemDecoration;
import io.yon.android.view.adapter.SearchResultAdapter;

/**
 * Created by amirhosein on 8/30/2017 AD.
 */

public class SearchActivity extends Activity implements SearchContract.View {

    boolean isClearButtonVisible = false;

    private ImageButton btnClear;
    private EditText searchBar;

    private ProgressBar progressBar;
    private View errorContainer, emptyStateContainer;
    private Button btnRetry;
    private RecyclerView recyclerView;
    private SearchResultAdapter adapter;
    private RxBus bus = new RxBus();

    private SearchPresenter presenter;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_search;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setDisplayHomeAsUpEnabled(true);

        initViews();

        presenter = ViewModelProviders.of(this).get(SearchPresenter.class);
        presenter.bindView(this);

        bus.toObservable().subscribe(this::handelItemClick);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!"".equals(searchBar.getText().toString())) {
            presenter.loadSearchResult(searchBar.getText().toString());
            ViewUtils.closeKeyboard(this);
        }
    }

    @Override
    protected void findViews() {
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        btnClear = (ImageButton) findViewById(R.id.toolbar_icon_left);
        searchBar = (EditText) findViewById(R.id.toolbar_et_search);
        errorContainer = findViewById(R.id.error_container);
        emptyStateContainer = findViewById(R.id.empty_state_container);
        btnRetry = (Button) findViewById(R.id.btn_retry);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
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
    public void showResult(List<SearchResultSection> result) {
        clearVisibilities();

        if (result.size() == 0) {
            emptyStateContainer.setVisibility(View.VISIBLE);
            return;
        }

        recyclerView.setVisibility(View.VISIBLE);

        adapter.setData(result);
        adapter.notifyDataSetChanged();
    }

    protected void initViews() {
        int dividerColor = ContextCompat.getColor(this, R.color.restaurant_list_divider_color);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new LinearDividerItemDecoration(this, dividerColor, ViewUtils.px(this, 0.8f)));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new SearchResultAdapter(this, null, bus);
        recyclerView.setAdapter(adapter);

        btnRetry.setOnClickListener(v -> presenter.loadSearchResult(searchBar.getText().toString()));
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
        searchBar.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });
    }

    protected void performSearch() {
        ViewUtils.closeKeyboard(this);

        if (!"".equals(searchBar.getText().toString()))
            presenter.loadSearchResult(searchBar.getText().toString(), true);
    }

    protected void handelItemClick(Object o) {
        Object item = null;
        try {
            item = findItemByAdapterPosition((Integer) o);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }

        if (item instanceof Tag)
            Logger.d(((Tag) item).getName());
        else if (item instanceof Zone)
            Logger.d(((Zone) item).getName());
        else if (item instanceof Restaurant)
            Logger.d(((Restaurant) item).getName());
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
    }

    private Object findItemByAdapterPosition(int position) {
        if (adapter.getData().size() == 0)
            return null;

        int i = 0;
        SearchResultSection section = adapter.getData().get(i++);
        position--;
        while (position >= section.getItems().size() && i < adapter.getData().size()) {
            position -= section.getItems().size();
            position--;
            section = adapter.getData().get(i++);
        }

        return section.getItems().get(position);
    }
}
