package io.yon.android.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import io.yon.android.R;
import io.yon.android.presenter.Presenter;
import io.yon.android.view.activity.ReservationBuilderController;

/**
 * Created by amirhosein on 8/19/2017 AD.
 */

public class ReservationGuestCountFragment extends Fragment {

    private Button btnPrevious, btnNext;

    private Presenter mPresenter;
    private ReservationBuilderController mController;

    @Override
    protected int getResourceLayoutId() {
        return R.layout.fragment_reservation_guest_count;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mController = getParentActivity();

        initViews();
    }

    @Override
    protected void findViews(View v) {
        btnPrevious = (Button) v.findViewById(R.id.btn_previous);
        btnNext = (Button) v.findViewById(R.id.btn_next);
    }

    private void initViews() {
        btnNext.setOnClickListener(v -> mController.next());
        btnPrevious.setOnClickListener(v -> mController.previous());
    }
}
