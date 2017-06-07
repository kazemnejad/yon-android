package io.yon.android.view;

/**
 * Created by amirhosein on 6/6/17.
 */

public interface LeView extends MvpView {
    void showError(Throwable error);
    void showLoading();
}
