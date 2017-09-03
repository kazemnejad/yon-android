package io.yon.android.presenter;

import android.app.Application;

import com.waylonbrown.lifecycleawarerx.LifecycleBinder;

import java.util.List;

import io.reactivex.Observable;
import io.yon.android.contract.ZoneSearchContract;
import io.yon.android.model.Zone;
import io.yon.android.repository.Lce;
import io.yon.android.repository.ZoneRepository;
import io.yon.android.util.RxUtils;
import io.yon.android.view.MvpView;

/**
 * Created by amirhosein on 9/1/2017 AD.
 */

public class ZoneSearchPresenter extends Presenter implements ZoneSearchContract.Presenter {

    private ZoneSearchContract.View view;

    private Observable<Lce<List<Zone>>> zonesObservable;

    private long lastRequestTime = -1;

    public ZoneSearchPresenter(Application application) {
        super(application);
    }

    @Override
    public void bindView(MvpView view) {
        this.view = (ZoneSearchContract.View) view;
    }

    @Override
    public void loadZones(String query) {
        if (query.length() == 0 && lastRequestTime == -1)
            return;

        if (zonesObservable == null)
            zonesObservable = ZoneRepository.getInstance()
                    .search(query)
                    .compose(RxUtils.applySchedulers())
                    .cache();

        long requestTime = System.currentTimeMillis();
        lastRequestTime = requestTime;

        zonesObservable.takeWhile(LifecycleBinder.notDestroyed(view))
                .compose(LifecycleBinder.subscribeWhenReady(view, new Lce.Observer<>(
                        lce -> {
                            if (lastRequestTime > requestTime)
                                return;

                            if (lce.isLoading())
                                view.showLoading();
                            else if (lce.hasError()) {
                                zonesObservable = null;
                                view.showError(lce.getError());
                            } else
                                view.showData(lce.getData());
                        }
                )));
    }

    public void loadZones(boolean skipCache, String query) {
        if (query.length() == 0)
            return;

        if (skipCache)
            zonesObservable = null;

        loadZones(query);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        view = null;
    }
}
