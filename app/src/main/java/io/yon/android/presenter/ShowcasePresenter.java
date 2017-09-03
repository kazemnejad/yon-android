package io.yon.android.presenter;

import android.app.Application;

import com.waylonbrown.lifecycleawarerx.LifecycleBinder;

import java.util.List;

import io.reactivex.Observable;
import io.yon.android.Config;
import io.yon.android.api.response.ShowcaseResponse;
import io.yon.android.contract.ShowcaseContract;
import io.yon.android.model.Zone;
import io.yon.android.repository.ContentRepository;
import io.yon.android.repository.Lce;
import io.yon.android.util.RxUtils;
import io.yon.android.view.MvpView;

/**
 * Created by amirhosein on 8/2/17.
 */

public class ShowcasePresenter extends Presenter implements ShowcaseContract.Presenter {
    private Zone currentZone;

    private ShowcaseContract.View view;

    private Observable<Lce<ShowcaseResponse>> fetchObservable;
    private Observable<Lce<ShowcaseResponse>> reFetchObservable;

    private boolean cacheWasAvailable;

    public ShowcasePresenter(Application application) {
        super(application);
        cacheWasAvailable = Config.getCache(application).contains(Config.Field.ShowCase);
    }

    public Zone getCurrentZone() {
        return currentZone;
    }

    public void setCurrentZone(Zone currentZone) {
        this.currentZone = currentZone;
    }

    @Override
    public void bindView(MvpView view) {
        this.view = (ShowcaseContract.View) view;
    }

    @Override
    public void fetchData(boolean clean) {
        if (clean)
            fetchObservable = null;

        fetchData();
    }

    @Override
    public void fetchData() {
        if (fetchObservable == null) {
            Observable<Lce<List<Object>>> showcase = null;
//            if (cacheWasAvailable)
//                showcase = ContentRepository.getInstance().getShowcaseFromCache(getApplication());
//            else
//                showcase = ContentRepository.getInstance().getShowcase(getApplication(), currentZone);

            fetchObservable = ContentRepository.getInstance().getShowcase(getApplication(), currentZone)
                    .compose(RxUtils.applySchedulers())
                    .cache();
        }

        fetchObservable.takeWhile(LifecycleBinder.notDestroyed(view))
                .compose(LifecycleBinder.subscribeWhenReady(view, new Lce.Observer<>(
                        lce -> {
                            if (lce.isLoading())
                                view.showLoading();
                            else if (lce.hasError()) {
                                if (!cacheWasAvailable) {
                                    fetchObservable = null;
                                    view.showError(lce.getError());
                                    return;
                                }
                                // cancel wreak cache
                                clearCache();

                                // fallback to online solution
                                fetchObservable = null;
                                cacheWasAvailable = false;
                                fetchData();
                            } else {
                                view.showData(lce.getData().getProcessedResponse(), lce.getData().getLocation());
                                if (cacheWasAvailable)
                                    reFetchData();
                            }
                        }
                )));
    }

    @Override
    public void reFetchData() {
        if (reFetchObservable == null)
            reFetchObservable = ContentRepository.getInstance().getShowcase(getApplication(), currentZone)
                    .compose(RxUtils.applySchedulers())
                    .cache();

        reFetchObservable.takeWhile(LifecycleBinder.notDestroyed(view))
                .compose(LifecycleBinder.subscribeWhenReady(view, new Lce.Observer<>(
                        lce -> {
                            if (lce.isLoading()) {
                                view.showReloading();
                            } else if (lce.hasError()) {
                                reFetchObservable = null;
                                view.showReloadError(lce.getError());
                            } else {
                                reFetchObservable = null;
                                view.showData(lce.getData().getProcessedResponse(), lce.getData().getLocation());
                            }
                        }
                )));
//        reFetchObservable.subscribe(lce -> {
//            if (view == null)
//                return;
//
//            if (lce.isLoading()) {
//                view.showReloading();
//            } else if (lce.hasError()) {
//                reFetchObservable = null;
//                view.showReloadError(lce.getError());
//            } else {
//                reFetchObservable = null;
//                view.showData(lce.getData().getProcessedResponse(), lce.getData().getLocation());
//            }
//        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        view = null;
    }

    private void clearCache() {
        Config.getCache(getApplication())
                .edit()
                .remove(Config.Field.ShowCase)
                .apply();
    }
}
