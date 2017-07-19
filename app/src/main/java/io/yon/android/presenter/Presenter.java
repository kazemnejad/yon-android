package io.yon.android.presenter;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import io.yon.android.view.MvpView;

/**
 * Created by amirhosein on 6/6/17.
 */

public abstract class Presenter extends AndroidViewModel {
    public Presenter(Application application) {
        super(application);
    }

    public void bindView(MvpView view) {
    }
}
