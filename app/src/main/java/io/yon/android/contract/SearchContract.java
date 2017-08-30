package io.yon.android.contract;

import java.util.List;

import io.yon.android.model.SearchResultSection;
import io.yon.android.view.MvpView;

/**
 * Created by amirhosein on 8/30/2017 AD.
 */

public class SearchContract extends Contract {
    public interface Presenter {
        void loadSearchResult(String query);
    }

    public interface View extends MvpView {
        void showLoading();

        void showError(Throwable e);

        void showResult(List<SearchResultSection> result);
    }
}
