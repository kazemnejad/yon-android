package io.yon.android.view;

/**
 * Created by amirhosein on 6/6/17.
 */

public interface LceView<T> extends MvpView {
    void showError(Throwable error);

    void showLoading();

    void showData(T data);
}
