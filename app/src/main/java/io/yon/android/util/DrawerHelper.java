package io.yon.android.util;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import io.yon.android.R;
import io.yon.android.db.AppDatabase;
import io.yon.android.model.User;
import io.yon.android.util.calendar.LanguageUtils;
import io.yon.android.view.GlideApp;
import io.yon.android.view.activity.Activity;
import io.yon.android.view.adapter.viewholder.ItemNvReservationViewHolder;

/**
 * Created by amirhosein on 6/3/17.
 */

public class DrawerHelper implements NavigationView.OnNavigationItemSelectedListener {
    private final Activity mContext;
    private final DrawerLayout mDrawerLayout;
    private final NavigationView mNavigationView;
    private NavigationView.OnNavigationItemSelectedListener mListener;

    private RxBus bus = new RxBus();

    private boolean isUserAuthenticated = false;

    public DrawerHelper(Activity context, DrawerLayout drawerLayout) {
        mContext = context;
        mDrawerLayout = drawerLayout;
        mNavigationView = (NavigationView) mDrawerLayout.findViewById(R.id.navigation_view);
    }

    public void init() {
        if (mNavigationView == null)
            return;

        mNavigationView.setNavigationItemSelectedListener(this);

        setHeaderViewHeight();

        invalidate();
    }

    private void setHeaderViewHeight() {
        mNavigationView.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (mNavigationView.getWidth() != 0) {
                            ViewUtils.removeOnGlobalLayoutListener(mNavigationView.getViewTreeObserver(), this);

                            int navigationViewWidth = mNavigationView.getWidth();

                            View view = mNavigationView.getHeaderView(0);
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
                            params.height = (int) (navigationViewWidth * 9.0 / 16.0);
                            view.setLayoutParams(params);
                        }
                    }
                });
    }

    private void initNavigationMenus() {
        mNavigationView.getMenu().setGroupVisible(R.id.gp_member, isUserAuthenticated);
        mNavigationView.getMenu().setGroupVisible(R.id.gp_guest, !isUserAuthenticated);
    }

    private void initHeaderLayouts() {
        View headerLayout = mNavigationView.getHeaderView(0);

        if (isUserAuthenticated) {
            headerLayout.findViewById(R.id.header_view_member).setVisibility(View.VISIBLE);
            headerLayout.findViewById(R.id.header_view_guest).setVisibility(View.INVISIBLE);

            initHeaderWithUserProfile();
        } else {
            headerLayout.findViewById(R.id.header_view_member).setVisibility(View.INVISIBLE);
            headerLayout.findViewById(R.id.header_view_guest).setVisibility(View.VISIBLE);
        }
    }

    private void initHeaderWithUserProfile() {
        User user = Auth.user(mContext);

        View h = mNavigationView.getHeaderView(0).findViewById(R.id.header_view_member);

        TextView userName = (TextView) h.findViewById(R.id.user_name);
        TextView point = (TextView) h.findViewById(R.id.user_point);
        ImageView avatar = (ImageView) h.findViewById(R.id.user_avatar);

        userName.setText(user.getFirstName() + " " + user.getLastName());
        point.setText(LanguageUtils.getPersianNumbers(mContext.getString(R.string.user_points, user.getPoint())));

        ColorGenerator generator = ColorGenerator.MATERIAL;
        Drawable placeHolder = TextDrawable.builder()
                .buildRound(
                        user.getFirstName().length() > 0 ? String.valueOf(user.getFirstName().charAt(0)) : "",
                        generator.getColor(user.getEmail())
                );

        GlideApp.with(mContext)
                .asBitmap()
                .load(user.getAvatar())
                .placeholder(placeHolder)
                .circleCrop()
                .into(avatar);
    }

    private void initCurrentReservations() {
        if (!isUserAuthenticated)
            return;

////        RecyclerView recyclerView = (RecyclerView) mNavigationView.getMenu()
////                .findItem(R.id.current_reservation)
////                .getActionView();
//
////        mNavigationView.g
//
//        RecyclerView recyclerView = (RecyclerView) mNavigationView.findViewById(R.id.navigation_current_reservation_recycler_view);
//
////        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) recyclerView.getLayoutParams();
////        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
////        recyclerView.setLayoutParams(params);
//
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
//
//        Adapter<Reservation, ItemNvReservationViewHolder> adapter = new Adapter<>(mContext, null, bus, ItemNvReservationViewHolder.getFactory());
//        recyclerView.setAdapter(adapter);
//
////        AppDatabase.getInstance(mContext.getApplicationContext())
////                .reservationDao()
////                .loadAllReservations()
////                .toObservable()
////                .compose(RxUtils.applySchedulers())
////                .subscribe(lst -> {
////                    if (adapter != null)
////                        adapter.setDataAndNotify(lst);
////                });

        View lastReservation = mNavigationView.findViewById(R.id.nv_reserve_info_container);
        ItemNvReservationViewHolder viewHolder = new ItemNvReservationViewHolder(lastReservation, mContext, bus);
        AppDatabase.getInstance(mContext.getApplicationContext())
                .reservationDao()
                .loadAllReservations()
                .observe(mContext, lst -> {
                    if (lst.size() > 0) {
                        lastReservation.setVisibility(View.VISIBLE);
                        viewHolder.bindContent(lst.get(lst.size() - 1));
                    }
                });

    }

    public void invalidate() {
        if (mNavigationView == null)
            return;

        isUserAuthenticated = Auth.check(mContext);

        initNavigationMenus();
        initHeaderLayouts();
        initCurrentReservations();
    }

    public Menu getMenu() {
        return mNavigationView != null ? mNavigationView.getMenu() : null;
    }

    public void setNavigationItemSelectListener(final NavigationView.OnNavigationItemSelectedListener listener) {
        mListener = listener;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (mDrawerLayout != null)
            mDrawerLayout.closeDrawers();

        if (!item.isChecked())
            return mListener != null && mListener.onNavigationItemSelected(item);

        return false;
    }

    public void setMenuItemsChecked(int[] checkedMenuItems) {
        if (mNavigationView == null)
            return;

        for (int i = 0; i < checkedMenuItems.length; i++) {
            int id = checkedMenuItems[i];
            mNavigationView.getMenu().findItem(id).setChecked(true);
        }
    }
}
