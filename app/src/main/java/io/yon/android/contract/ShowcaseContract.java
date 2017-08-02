package io.yon.android.contract;

import java.util.List;

import io.yon.android.view.MvpView;

/**
 * Created by amirhosein on 8/2/17.
 */

public class ShowcaseContract extends Contract {
    public interface Presenter {
        void fetchData(boolean clean);

        void fetchData();

        void reFetchData();
    }

    public interface View extends MvpView {
        void showLoading();

        void showReloading();

        void showError(Throwable e);

        void showReloadError(Throwable e);

        void showData(List<Object> data);
    }
}
