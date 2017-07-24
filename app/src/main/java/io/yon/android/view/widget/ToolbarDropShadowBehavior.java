package io.yon.android.view.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by amirhosein on 3/4/16.
 */
public class ToolbarDropShadowBehavior extends CoordinatorLayout.Behavior<View> {
    // We only support the FAB <> Snackbar shift movement on Honeycomb and above. This is
    // because we can use view translation properties which greatly simplifies the code.
    private static final boolean SNACKBAR_BEHAVIOR_ENABLED = Build.VERSION.SDK_INT >= 11;

    private ValueAnimator mFabTranslationYAnimator;
    private float mFabTranslationY;
    private Rect mTmpRect;

    public ToolbarDropShadowBehavior() {
    }

    //Required to attach behavior via XML
    public ToolbarDropShadowBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent,
                                   View child, View dependency) {
        // We're dependent on all SnackbarLayouts (if enabled)
        return dependency instanceof FloatingActionButton;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child,
                                          View dependency) {
        if (dependency instanceof FloatingActionButton) {
            // If we're depending on an AppBarLayout we will show/hide it automatically
            // if the FAB is anchored to the AppBarLayout
            updateFabVisibility(parent, (FloatingActionButton) dependency, child);
        }
        return false;
    }

    private void updateFabVisibility(CoordinatorLayout parent, FloatingActionButton dependency, View child) {
        makeItVisible((dependency.getVisibility() != View.VISIBLE), child);
    }


    public void makeItVisible(boolean visible, View child) {
        child.setVisibility((visible) ? View.VISIBLE : View.INVISIBLE);
        /*child.animate()
                .alpha((visible) ? 1.0f : 0.0f)
                .setDuration(0);*/
    }
}