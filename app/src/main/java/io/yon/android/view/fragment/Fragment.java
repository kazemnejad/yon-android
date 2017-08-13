package io.yon.android.view.fragment;


import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by amirhosein on 7/17/17.
 */

public abstract class Fragment extends android.support.v4.app.Fragment implements LifecycleRegistryOwner {
    LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

    public static <T extends Fragment> T setArguments(T fragment, Bundle bundle) {
        if (fragment != null)
            fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return mLifecycleRegistry;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getResourceLayoutId() == -1)
            return super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(getResourceLayoutId(), container, false);
        findViews(view);
        return view;
    }

    protected int getResourceLayoutId() {
        return -1;
    }

    protected void findViews(View v) {}

    protected <T> T getParentActivity() {
        FragmentActivity activity = getActivity();
        if (activity == null)
            return null;

        try {
            return (T) activity;
        } catch (Exception exp) {
            return null;
        }
    }
}
