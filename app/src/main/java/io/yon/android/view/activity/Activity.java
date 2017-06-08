package io.yon.android.view.activity;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.MenuRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import io.yon.android.R;
import io.yon.android.view.widget.PopupMenu;
import io.yon.android.util.DrawerHelper;

/**
 * Created by amirhosein on 5/27/17.
 */

public abstract class Activity extends AppCompatActivity implements LifecycleRegistryOwner {

    private final LifecycleRegistry mRegistry = new LifecycleRegistry(this);

    @Nullable
    private Toolbar mToolbar;

    @Nullable
    private TextView toolbarMain;

    @Nullable
    private ImageButton toolbarRightButton;

    @Nullable
    private ImageButton toolbarMoreButton;

    @Nullable
    private ImageButton toolbarLeftButton;

    @Nullable
    private PopupMenu mPopupMenu;

    @Nullable
    private DrawerLayout mDrawerLayout;
    private DrawerHelper drawerHelper;

    private boolean isOptionMenuEnabled = false;

    protected abstract
    @LayoutRes
    int getLayoutResourceId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());

        initToolbar();
        initDrawer();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isOptionMenuEnabled && mPopupMenu != null && !mPopupMenu.isInflated())
            onCreateOptionMenu(mPopupMenu.getMenu(), mPopupMenu.getMenuInflater());
    }

    @Override
    public void setTitle(CharSequence title) {
        if (toolbarMain != null)
            toolbarMain.setText(title);
    }

    @Override
    public void setTitle(@StringRes int titleId) {
        if (toolbarMain != null)
            toolbarMain.setText(titleId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return isOptionMenuEnabled && mPopupMenu != null && onCreateOptionMenu(mPopupMenu.getMenu(), mPopupMenu.getMenuInflater());
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            mDrawerLayout.closeDrawer(Gravity.RIGHT);
            return;
        }

        super.onBackPressed();
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return mRegistry;
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar == null)
            return;

        toolbarMain = (TextView) mToolbar.findViewById(R.id.toolbar_text_main);
        toolbarRightButton = (ImageButton) mToolbar.findViewById(R.id.toolbar_icon_right);
        toolbarMoreButton = (ImageButton) mToolbar.findViewById(R.id.toolbar_icon_more);
        toolbarLeftButton = (ImageButton) mToolbar.findViewById(R.id.toolbar_icon_left);
    }

    private void initDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawerLayout == null)
            return;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow_left, Gravity.RIGHT);
        }

        if (toolbarRightButton == null)
            return;

        toolbarRightButton.setImageResource(R.drawable.ic_menu_24dp);
        toolbarRightButton.setVisibility(View.VISIBLE);
        toolbarRightButton.setOnClickListener(v -> onDrawerButtonClick());

        drawerHelper = new DrawerHelper(this, mDrawerLayout);
        drawerHelper.init();
    }

    private void onDrawerButtonClick() {
        if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT))
            mDrawerLayout.closeDrawer(Gravity.RIGHT);
        else
            mDrawerLayout.openDrawer(Gravity.RIGHT);
    }

    protected void setDisplayHomeAsUpEnabled(boolean enable) {
        if (toolbarRightButton == null)
            return;

        final Activity activity = this;
        if (enable) {
            toolbarRightButton.setImageResource(R.drawable.ic_navigation_back_24dp);
            toolbarRightButton.setVisibility(View.VISIBLE);
            toolbarRightButton.setOnClickListener(v -> {
                if (NavUtils.getParentActivityIntent(activity) != null)
                    NavUtils.navigateUpFromSameTask(activity);
                else
                    onBackPressed();
            });
        } else {
            toolbarRightButton.setOnClickListener(null);
        }
    }

    protected void setHasOptionMenu(boolean enable) {
        if (toolbarMoreButton == null) {
            isOptionMenuEnabled = false;
            return;
        }

        isOptionMenuEnabled = enable;
        if (enable) {
            mPopupMenu = new PopupMenu(Activity.this, toolbarMoreButton, Gravity.TOP, 0, R.style.Widget_AppCompat_Light_PopupMenu_Overflow);
            mPopupMenu.setOnMenuItemClickListener(item -> isOptionMenuEnabled && Activity.this.onOptionsItemSelected(item));

            toolbarMoreButton.setVisibility(View.VISIBLE);
            toolbarMoreButton.setOnClickListener(v -> {
                if (mPopupMenu != null && isOptionMenuEnabled)
                    mPopupMenu.show();
            });
        } else {
            mPopupMenu = null;
            toolbarMoreButton.setVisibility(View.GONE);
            toolbarMoreButton.setOnClickListener(null);
        }
    }

    protected DrawerHelper getDrawerHelper() {
        return drawerHelper;
    }

    protected void forceEnableOptionMenu(@MenuRes int menuId) {
        setHasOptionMenu(true);
        if (mPopupMenu != null)
            mPopupMenu.inflate(menuId);
    }

    public boolean onCreateOptionMenu(Menu menu, MenuInflater inflater) {
        return false;
    }
}
