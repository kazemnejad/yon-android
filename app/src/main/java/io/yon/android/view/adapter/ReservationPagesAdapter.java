package io.yon.android.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import io.yon.android.view.fragment.ReservationDateFragment;

/**
 * Created by amirhosein on 8/20/2017 AD.
 */

public class ReservationPagesAdapter extends FragmentStatePagerAdapter {
    private ReservationDateFragment dateFragment;

    public ReservationPagesAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if (dateFragment == null)
                    dateFragment = new ReservationDateFragment();

                return dateFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 1;
    }
}
