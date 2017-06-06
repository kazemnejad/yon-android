package io.yon.android.view.widget;

import android.content.Context;
import android.support.v7.view.SupportMenuInflater;
import android.view.Menu;

/**
 * Created by amirhosein on 5/31/17.
 */

class StateAwareMenuInflater extends SupportMenuInflater {

    private final OnInflateListener mListener;

    StateAwareMenuInflater(Context context, OnInflateListener listener) {
        //noinspection RestrictedApi
        super(context);
        mListener = listener;
    }

    @Override
    public void inflate(int menuRes, Menu menu) {
        //noinspection RestrictedApi
        super.inflate(menuRes, menu);
        mListener.onInflate();
    }

    interface OnInflateListener {
        void onInflate();
    }
}