package io.yon.android.view.widget;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;

/**
 * Created by amirhosein on 7/30/17.
 */

public class BitmapViewGroupTarget extends ViewTarget<ViewGroup, Bitmap> implements Transition.ViewAdapter {
    public BitmapViewGroupTarget(ViewGroup view) {
        super(view);
    }


    @Override
    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
        setBackground(resource);
    }

    @Nullable
    @Override
    public Drawable getCurrentDrawable() {
        return view.getBackground();
    }

    @Override
    public void setDrawable(Drawable drawable) {
        setBackground(drawable);
    }

    private void setBackground(Drawable drawable) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN)
            view.setBackground(drawable);
        else
            view.setBackgroundDrawable(drawable);
    }

    private void setBackground(Bitmap bitmap) {
        BitmapDrawable drawable = new BitmapDrawable(view.getContext().getResources(), bitmap);
        setBackground(drawable);
    }
}
