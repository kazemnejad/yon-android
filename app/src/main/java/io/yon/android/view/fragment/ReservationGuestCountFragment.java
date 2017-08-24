package io.yon.android.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import io.yon.android.R;
import io.yon.android.presenter.ReservationPresenter;
import io.yon.android.util.RxBus;
import io.yon.android.util.ViewUtils;
import io.yon.android.view.activity.ReservationBuilderController;
import io.yon.android.view.adapter.Adapter;
import io.yon.android.view.adapter.viewholder.ItemGuestCountViewHolder;
import io.yon.android.view.widget.NonScrollingGridLayoutManager;

/**
 * Created by amirhosein on 8/19/2017 AD.
 */

public class ReservationGuestCountFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Integer> guestCountOptions = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
    private RxBus bus = new RxBus();

    private Button btnPrevious, btnNext;

    private ReservationPresenter mPresenter;
    private ReservationBuilderController mController;

    @Override
    protected int getResourceLayoutId() {
        return R.layout.fragment_reservation_guest_count;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mController = getParentActivity();
        bus.toObservable().subscribe(this::handleClick);

        mPresenter = ViewModelProviders.of((FragmentActivity) getParentActivity()).get(ReservationPresenter.class);

        initViews();
    }

    @Override
    protected void findViews(View v) {
        btnNext = (Button) v.findViewById(R.id.btn_next);
        btnPrevious = (Button) v.findViewById(R.id.btn_previous);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
    }

    private void initViews() {
        btnNext.setOnClickListener(v -> mController.next());
        btnPrevious.setOnClickListener(v -> mController.previous());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new NonScrollingGridLayoutManager(getContext(), 4, LinearLayoutManager.VERTICAL, false));

        Adapter<Integer, ItemGuestCountViewHolder> adapter = new Adapter<>(getContext(), guestCountOptions, bus, ItemGuestCountViewHolder.getFactory());
        recyclerView.setAdapter(adapter);

        ViewUtils.setButtonEnabled(btnNext, mPresenter.getGuestCount() != -1);
        if (mPresenter.getLastGuestCountAdapterPosition() != -1)
            updateSelectedOption(mPresenter.getLastGuestCountAdapterPosition());
    }

    private void handleClick(Object o) {
        int adapterPosition = -1;
        try {
            adapterPosition = (int) o;
        } catch (Exception exp) {
            return;
        }

        int lastAdapterPosition = mPresenter.getLastGuestCountAdapterPosition();
        if (lastAdapterPosition != -1) {
            View v = recyclerView.getChildAt(lastAdapterPosition);
            if (v != null)
                ItemGuestCountViewHolder.setSelected((TextView) v, false);
        }

        if (adapterPosition >= guestCountOptions.size() || adapterPosition < 0)
            return;

        mPresenter.setLastGuestCountAdapterPosition(adapterPosition);
        mPresenter.setGuestCount(guestCountOptions.get(adapterPosition));

        ViewUtils.setButtonEnabled(btnNext, true);
    }

    private void updateSelectedOption(int adapterPosition) {
        recyclerView.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        View v = recyclerView.getChildAt(adapterPosition);
                        if (v != null) {
                            ViewUtils.removeOnGlobalLayoutListener(recyclerView.getViewTreeObserver(), this);
                            ItemGuestCountViewHolder.setSelected((TextView) v, true);
                        }
                    }
                });
    }
}
