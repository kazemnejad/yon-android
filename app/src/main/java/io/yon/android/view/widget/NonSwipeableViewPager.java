package io.yon.android.view.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by amirhosein on 7/18/17.
 */

public class NonSwipeableViewPager extends ViewPager {

    private boolean allowScrolling = false;

    public NonSwipeableViewPager(Context context) {
        super(context);
    }

    public NonSwipeableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setAllowScrolling(boolean allowScrolling) {
        this.allowScrolling = allowScrolling;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return allowScrolling && super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return allowScrolling && super.onTouchEvent(event);
    }
}
