package io.yon.android.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.truizlop.sectionedrecyclerview.SectionedSpanSizeLookup;

import java.util.List;

import io.yon.android.R;
import io.yon.android.contract.ReservationContract;
import io.yon.android.model.OpenTimeSlot;
import io.yon.android.model.OpenTimeSlotSection;
import io.yon.android.presenter.ReservationPresenter;
import io.yon.android.util.RxBus;
import io.yon.android.util.ViewUtils;
import io.yon.android.view.activity.ReservationBuilderController;
import io.yon.android.view.adapter.OpenHourAdapter;

/**
 * Created by amirhosein on 8/19/2017 AD.
 */

public class ReservationTimeFragment extends Fragment implements ReservationContract.TimeView {

    private RxBus bus = new RxBus();
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private View errorContainer;

    private Button btnPrevious, btnNext, btnRetry;
    private ReservationPresenter mPresenter;

    private ReservationBuilderController mController;
    private OpenHourAdapter adapter;

    @Override
    protected int getResourceLayoutId() {
        return R.layout.fragment_reservation_time;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mController = getParentActivity();
        mPresenter = ViewModelProviders.of((FragmentActivity) getParentActivity()).get(ReservationPresenter.class);
        mPresenter.bindTimeView(this);

        initViews();

        mPresenter.loadOpenHours();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateNextButton();
    }

    @Override
    protected void findViews(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        progressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
        errorContainer = v.findViewById(R.id.error_container);
        btnPrevious = (Button) v.findViewById(R.id.btn_previous);
        btnNext = (Button) v.findViewById(R.id.btn_next);
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
        e.printStackTrace();
    }

    @Override
    public void showOpenHours(List<OpenTimeSlotSection> openTimeSlots) {
        clearVisibilities();

        recyclerView.setVisibility(View.VISIBLE);

        adapter = new OpenHourAdapter(getContext(), openTimeSlots, bus, mPresenter.getSelectedTimeSlot());
        recyclerView.setAdapter(adapter);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4);
        SectionedSpanSizeLookup lookup = new SectionedSpanSizeLookup(adapter, layoutManager);
        layoutManager.setSpanSizeLookup(lookup);
        recyclerView.setLayoutManager(layoutManager);

        updateNextButton();
    }

    private void initViews() {
        btnNext.setOnClickListener(view -> mController.next());
        btnPrevious.setOnClickListener(view -> mController.previous());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        updateNextButton();

        bus.toObservable().subscribe(this::handleOpenTimeSlotClick);
    }

    private void handleOpenTimeSlotClick(Object o) {
        int adapterPosition;
        try {
            adapterPosition = (int) o;
        } catch (Exception ignored) {
            return;
        }

        OpenTimeSlot timeSlot = findOpenSlotByAdapterPosition(adapterPosition);
        mPresenter.setSelectedTimeSlot(timeSlot);
        adapter.setSelectedTimeSlot(timeSlot);
        adapter.activate(adapterPosition);
        updateNextButton();
    }

    private OpenTimeSlot findOpenSlotByAdapterPosition(int position) {
        if (adapter.getData().size() == 0)
            return null;

        int i = 0;
        OpenTimeSlotSection section = adapter.getData().get(i++);
        position--;
        while (position >= section.getOpenTimeSlots().size() && i < adapter.getData().size()) {
            position -= section.getOpenTimeSlots().size();
            position--;
            section = adapter.getData().get(i++);
        }

        return section.getOpenTimeSlots().get(position);
    }

    private void updateNextButton() {
        ViewUtils.setButtonEnabled(btnNext, mPresenter.getSelectedTimeSlot() != null);
    }

    public void clearVisibilities() {
        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        errorContainer.setVisibility(View.INVISIBLE);
    }
}
