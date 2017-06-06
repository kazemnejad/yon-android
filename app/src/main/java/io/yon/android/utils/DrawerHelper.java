package io.yon.android.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.design.widget.NavigationView;
import android.view.Menu;
import android.view.MenuItem;

import io.yon.android.R;

/**
 * Created by amirhosein on 6/3/17.
 */

public class DrawerHelper {
    private final Context mContext;
    private final DrawerLayout mDrawerLayout;
    private final NavigationView mNavigationView;
    private NavigationView.OnNavigationItemSelectedListener mListener;

    public DrawerHelper(Context context, DrawerLayout drawerLayout) {
        mContext = context;
        mDrawerLayout = drawerLayout;
        mNavigationView = (NavigationView) mDrawerLayout.findViewById(R.id.navigation_view);
    }

    public void init() {
        if (mNavigationView == null)
            return;

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (mDrawerLayout != null)
                    mDrawerLayout.closeDrawers();

                return mListener != null && mListener.onNavigationItemSelected(item);
            }
        });

        handleNavigationMenus();
    }

    private void handleNavigationMenus() {
        boolean isUserAuthenticated = false;

        mNavigationView.getMenu().setGroupVisible(R.id.gp_member, isUserAuthenticated);
        mNavigationView.getMenu().setGroupVisible(R.id.gp_guest, !isUserAuthenticated);
    }

    public void invalidate() {
        if (mNavigationView == null)
            return;

        handleNavigationMenus();
    }

    public Menu getMenu() {
        return mNavigationView != null ? mNavigationView.getMenu() : null;
    }

    public void setNavigationItemSelectListener(final NavigationView.OnNavigationItemSelectedListener listener) {
        mListener = listener;
    }
}
