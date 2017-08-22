package io.yon.android.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import io.yon.android.presenter.ReservationPresenter.Step;
import io.yon.android.view.fragment.ReservationConfirmFragment;
import io.yon.android.view.fragment.ReservationDateFragment;
import io.yon.android.view.fragment.ReservationGuestCountFragment;
import io.yon.android.view.fragment.ReservationTableFragment;
import io.yon.android.view.fragment.ReservationTimeFragment;

/**
 * Created by amirhosein on 8/20/2017 AD.
 */

public class ReservationPagesAdapter extends FragmentStatePagerAdapter {
    private ReservationDateFragment dateFragment;
    private ReservationTimeFragment timeFragment;
    private ReservationGuestCountFragment guestCountFragment;
    private ReservationTableFragment tableFragment;
    private ReservationConfirmFragment confirmFragment;

    public ReservationPagesAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case Step.DateSelect:
                if (dateFragment == null)
                    dateFragment = new ReservationDateFragment();

                return dateFragment;

            case Step.TimeSelect:
                if (timeFragment == null)
                    timeFragment = new ReservationTimeFragment();

                return timeFragment;

            case Step.GuestCount:
                if (guestCountFragment == null)
                    guestCountFragment = new ReservationGuestCountFragment();

                return guestCountFragment;

            case Step.TableSelect:
                if (tableFragment == null)
                    tableFragment = new ReservationTableFragment();

                return tableFragment;

            case Step.Confirm:
                if (confirmFragment == null)
                    confirmFragment = new ReservationConfirmFragment();

                return confirmFragment;
        }

        return null;
    }

    @Override
    public int getCount() {
        return 5;
    }
}
