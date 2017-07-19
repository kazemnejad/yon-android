package io.yon.android.view.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.MenuInflater;
import android.view.View;

/**
 * Created by amirhosein on 5/31/17.
 */

public class PopupMenu extends android.support.v7.widget.PopupMenu implements StateAwareMenuInflater.OnInflateListener {
    private boolean isInflated = false;

    private Context mContext;

    public PopupMenu(@NonNull Context context, @NonNull View anchor) {
        super(context, anchor);
        mContext = context;
    }

    public PopupMenu(@NonNull Context context, @NonNull View anchor, int gravity) {
        super(context, anchor, gravity);
        mContext = context;
    }

    public PopupMenu(@NonNull Context context, @NonNull View anchor, int gravity, @AttrRes int popupStyleAttr, @StyleRes int popupStyleRes) {
        super(context, anchor, gravity, popupStyleAttr, popupStyleRes);
        mContext = context;
    }

    @NonNull
    @Override
    public MenuInflater getMenuInflater() {
        return new StateAwareMenuInflater(mContext, this);
    }

    @Override
    public void onInflate() {
        isInflated = true;
    }

    public boolean isInflated() {
        return isInflated;
    }
}
