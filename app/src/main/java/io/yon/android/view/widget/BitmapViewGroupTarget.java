package io.yon.android.view.widget;


import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
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

public class BitmapViewGroupTarget extends ViewTarget<ViewGroup, Drawable> implements Transition.ViewAdapter {
    @Nullable
    private Animatable animatable;

    public BitmapViewGroupTarget(ViewGroup view) {
        super(view);
    }

    @Override
    public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
        if (transition == null || !transition.transition(resource, this)) {
            setResourceInternal(resource);
        } else {
            maybeUpdateAnimatable(resource);
        }
    }

    @Override
    public void onLoadStarted(@Nullable Drawable placeholder) {
        super.onLoadStarted(placeholder);
        setResourceInternal(null);
        setDrawable(placeholder);
    }

    /**
     * Sets the given {@link android.graphics.drawable.Drawable} on the view using {@link
     * android.widget.ImageView#setImageDrawable(android.graphics.drawable.Drawable)}.
     *
     * @param errorDrawable {@inheritDoc}
     */
    @Override
    public void onLoadFailed(@Nullable Drawable errorDrawable) {
        super.onLoadFailed(errorDrawable);
        setResourceInternal(null);
        setDrawable(errorDrawable);
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

    @Override
    public void onStart() {
        if (animatable != null) {
            animatable.start();
        }
    }

    @Override
    public void onStop() {
        if (animatable != null) {
            animatable.stop();
        }
    }

    private void setResourceInternal(@Nullable Drawable resource) {
        maybeUpdateAnimatable(resource);
        setBackground(resource);
    }

    private void maybeUpdateAnimatable(@Nullable Drawable resource) {
        if (resource instanceof Animatable) {
            animatable = (Animatable) resource;
            animatable.start();
        } else {
            animatable = null;
        }
    }
}
