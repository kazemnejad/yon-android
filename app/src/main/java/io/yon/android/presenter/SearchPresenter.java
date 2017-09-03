package io.yon.android.presenter;

import android.app.Application;

import com.waylonbrown.lifecycleawarerx.LifecycleBinder;

import java.util.List;

import io.reactivex.Observable;
import io.yon.android.contract.SearchContract;
import io.yon.android.model.SearchResultSection;
import io.yon.android.repository.ContentRepository;
import io.yon.android.repository.Lce;
import io.yon.android.util.RxUtils;
import io.yon.android.view.MvpView;

/**
 * Created by amirhosein on 8/30/2017 AD.
 */

public class SearchPresenter extends Presenter implements SearchContract.Presenter {

    private SearchContract.View view;

    private Observable<Lce<List<SearchResultSection>>> searchObservable;

    private long lastRequestTime = -1;

    public SearchPresenter(Application application) {
        super(application);
    }

    @Override
    public void bindView(MvpView view) {
        this.view = (SearchContract.View) view;
    }

    @Override
    public void loadSearchResult(String query) {
        if (query.length() == 0 && lastRequestTime == -1)
            return;

        if (searchObservable == null)
            searchObservable = ContentRepository.getInstance()
                    .search(query)
                    .compose(RxUtils.applySchedulers())
                    .cache();

        long requestTime = System.currentTimeMillis();
        lastRequestTime = requestTime;

        searchObservable.takeWhile(LifecycleBinder.notDestroyed(view))
                .compose(LifecycleBinder.subscribeWhenReady(view, new Lce.Observer<>(
                        lce -> {
                            if (lastRequestTime > requestTime)
                                return;

                            if (lce.isLoading())
                                view.showLoading();
                            else if (lce.hasError()) {
                                searchObservable = null;
                                view.showError(lce.getError());
                            } else
                                view.showResult(lce.getData());
                        }
                )));
    }

    public void loadSearchResult(String query, boolean skipCache) {
        if ("".equals(query))
            return;

        if (skipCache) {
            searchObservable = null;
        }

        loadSearchResult(query);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        view = null;
    }
}
