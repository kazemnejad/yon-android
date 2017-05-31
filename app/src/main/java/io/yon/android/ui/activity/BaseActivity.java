package io.yon.android.ui.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.MenuRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import io.yon.android.R;
import io.yon.android.ui.view.PopupMenu;

/**
 * Created by amirhosein on 5/27/17.
 */

public abstract class BaseActivity extends AppCompatActivity {

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

    private boolean isOptionMenuEnabled = false;

    protected abstract
    @LayoutRes
    int getLayoutResourceId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());

        initToolbar();
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

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar == null)
            return;

        toolbarMain = (TextView) mToolbar.findViewById(R.id.toolbar_text_main);
        toolbarRightButton = (ImageButton) mToolbar.findViewById(R.id.toolbar_icon_right);
        toolbarMoreButton = (ImageButton) mToolbar.findViewById(R.id.toolbar_icon_more);
        toolbarLeftButton = (ImageButton) mToolbar.findViewById(R.id.toolbar_icon_left);
    }

    protected void setDisplayHomeAsUpEnabled(boolean enable) {
        // #TODO update using lambdas
        if (toolbarRightButton == null)
            return;

        final BaseActivity activity = this;
        if (enable) {
            toolbarRightButton.setImageResource(R.drawable.ic_navigation_back_24dp);
            toolbarRightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (NavUtils.getParentActivityIntent(activity) != null)
                        NavUtils.navigateUpFromSameTask(activity);
                    else
                        onBackPressed();
                }
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
            mPopupMenu = new PopupMenu(BaseActivity.this, toolbarMoreButton, Gravity.TOP, 0, R.style.Widget_AppCompat_Light_PopupMenu_Overflow);
            mPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return isOptionMenuEnabled && BaseActivity.this.onOptionsItemSelected(item);
                }
            });

            toolbarMoreButton.setVisibility(View.VISIBLE);
            toolbarMoreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPopupMenu != null && isOptionMenuEnabled)
                        mPopupMenu.show();

                }
            });
        } else {
            mPopupMenu = null;
            toolbarMoreButton.setVisibility(View.GONE);
            toolbarMoreButton.setOnClickListener(null);
        }
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
