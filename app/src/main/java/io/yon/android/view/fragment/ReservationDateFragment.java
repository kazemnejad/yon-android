package io.yon.android.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import io.yon.android.R;

/**
 * Created by amirhosein on 8/19/2017 AD.
 */

public class ReservationDateFragment extends Fragment {
    private RecyclerView recyclerView;

    @Override
    protected int getResourceLayoutId() {
        return R.layout.fragment_reservation_date;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
    }

    @Override
    protected void findViews(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
    }

    private void initView() {
        recyclerView.setHasFixedSize(true);
    }
}
