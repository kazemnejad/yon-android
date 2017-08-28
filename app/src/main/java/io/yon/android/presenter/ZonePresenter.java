package io.yon.android.presenter;

import android.app.Application;

import com.waylonbrown.lifecycleawarerx.LifecycleBinder;

import java.util.List;

import io.reactivex.Observable;
import io.yon.android.contract.ZoneContract;
import io.yon.android.model.Restaurant;
import io.yon.android.model.Zone;
import io.yon.android.repository.Lce;
import io.yon.android.repository.RestaurantRepository;
import io.yon.android.util.RxUtils;
import io.yon.android.view.MvpView;

/**
 * Created by amirhosein on 8/28/2017 AD.
 */

public class ZonePresenter extends Presenter implements ZoneContract.Presenter {

    private ZoneContract.View view;

    private Observable<Lce<List<Restaurant>>> restaurantObservable;

    public ZonePresenter(Application application) {
        super(application);
    }

    @Override
    public void bindView(MvpView view) {
        this.view = (ZoneContract.View) view;
    }

    @Override
    public void loadRestaurantsInZone(Zone zone) {
        if (restaurantObservable == null)
            restaurantObservable = RestaurantRepository.getInstance()
                    .getRestaurantsByZone(zone)
                    .compose(RxUtils.applySchedulers())
                    .cache();

        restaurantObservable.takeWhile(LifecycleBinder.notDestroyed(view))
                .compose(LifecycleBinder.subscribeWhenReady(view, new Lce.Observer<>(
                        lce -> {
                            if (lce.isLoading())
                                view.showRlLoading();
                            else if (lce.hasError()) {
                                restaurantObservable = null;
                                view.showRlError(lce.getError());
                            } else
                                view.showRl(lce.getData());
                        }
                )));
    }
}
