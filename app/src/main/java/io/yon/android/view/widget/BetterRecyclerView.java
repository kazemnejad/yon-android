package io.yon.android.view.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by amirhosein on 7/23/17.
 */

public class BetterRecyclerView extends RecyclerView {

    private int INVALID_POINTER = -1;
    private int scrollPointerId = INVALID_POINTER;
    private int initialTouchX = 0;
    private int initialTouchY = 0;
    private int touchSlop = 0;

    public BetterRecyclerView(Context context) {
        super(context);
    }

    public BetterRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BetterRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public void setScrollingTouchSlop(int slopConstant) {
        super.setScrollingTouchSlop(slopConstant);

        ViewConfiguration vc = ViewConfiguration.get(getContext());
        switch (slopConstant) {
            case TOUCH_SLOP_DEFAULT:
                touchSlop = vc.getScaledTouchSlop();
                break;

            case TOUCH_SLOP_PAGING:
                touchSlop = vc.getScaledPagingTouchSlop();
                break;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {

        final int action = MotionEventCompat.getActionMasked(e);
        final int actionIndex = MotionEventCompat.getActionIndex(e);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                scrollPointerId = e.getPointerId(0);
                initialTouchX = Math.round(e.getX() + 0.5f);
                initialTouchY = Math.round(e.getY() + 0.5f);
                return super.onInterceptTouchEvent(e);


            case MotionEventCompat.ACTION_POINTER_DOWN:
                scrollPointerId = e.getPointerId(actionIndex);
                initialTouchX = Math.round(e.getX(actionIndex) + 0.5f);
                initialTouchY = Math.round(e.getY(actionIndex) + 0.5f);
                return super.onInterceptTouchEvent(e);

            case MotionEvent.ACTION_MOVE:
                int index = e.findPointerIndex(scrollPointerId);
                if (index < 0)
                    return false;

                int x = Math.round(e.getX(index) + 0.5f);
                int y = Math.round(e.getY(index) + 0.5f);
                boolean startScroll = false;
                if (getScrollState() != SCROLL_STATE_DRAGGING) {
                    int dx = x - initialTouchX;
                    int dy = y - initialTouchY;

                    if (getLayoutManager().canScrollHorizontally() && Math.abs(dx) > touchSlop && (getLayoutManager().canScrollVertically() || Math.abs(dx) > Math.abs(dy)))
                        startScroll = true;

                    if (getLayoutManager().canScrollVertically() && Math.abs(dy) > touchSlop && (getLayoutManager().canScrollHorizontally() || Math.abs(dy) > Math.abs(dx)))
                        startScroll = true;

                    return startScroll && super.onInterceptTouchEvent(e);
                }


            case MotionEventCompat.ACTION_POINTER_UP:
                return super.onInterceptTouchEvent(e);

            case MotionEvent.ACTION_UP:
                return super.onInterceptTouchEvent(e);

            case MotionEvent.ACTION_CANCEL:
                return super.onInterceptTouchEvent(e);
        }

        return super.onInterceptTouchEvent(e);
    }
}
