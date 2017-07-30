package io.yon.android.view.widget;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;

/**
 * Created by amirhosein on 7/25/17.
 */

public class NonScrollingGridLayoutManager extends GridLayoutManager {

    public NonScrollingGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public NonScrollingGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public NonScrollingGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    @Override
    public boolean canScrollHorizontally() {
        return false;
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }
}
