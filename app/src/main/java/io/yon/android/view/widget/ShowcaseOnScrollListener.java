package io.yon.android.view.widget;

import android.content.Context;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import io.yon.android.R;
import io.yon.android.util.ViewUtils;

/**
 * Created by amirhosein on 7/24/17.
 */

public abstract class ShowcaseOnScrollListener extends RecyclerView.OnScrollListener {
    private final int height;
    private final boolean isPreLollipop;
    private final int fourDp;

    private AppBarLayout appBar;
    private View banners;
    private View toolbarShadow;

    private boolean lastState = false;

    public ShowcaseOnScrollListener(Context context) {
        height = ViewUtils.px(context, 200);
        fourDp = ViewUtils.px(context, 4);
        isPreLollipop = Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;

        appBar = (AppBarLayout) findViewById(R.id.appbar);
        banners = findViewById(R.id.banners);
        toolbarShadow = findViewById(R.id.toolbar_shadow);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int scrolled = recyclerView.computeVerticalScrollOffset();
        setAlpha(scrolled);

        boolean isTaller = scrolled > height;
        boolean isChanged = lastState != isTaller;

        if (!isChanged)
            return;

        lastState = isTaller;
        if (isPreLollipop)
            toolbarShadow.setVisibility(isTaller ? View.VISIBLE : View.INVISIBLE);

        ViewCompat.setElevation(appBar, isTaller ? fourDp : 0);
    }

    private void setAlpha(int scrolled) {
        if (banners == null)
            banners = findViewById(R.id.banners);

        if (banners == null)
            return;

        banners.setAlpha((float) (height - scrolled) / height);
    }

    protected abstract View findViewById(int id);
}
