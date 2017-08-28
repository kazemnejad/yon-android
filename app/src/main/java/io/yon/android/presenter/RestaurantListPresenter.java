package io.yon.android.presenter;

import android.app.Application;

import com.waylonbrown.lifecycleawarerx.LifecycleBinder;

import java.util.List;

import io.reactivex.Observable;
import io.yon.android.contract.RestaurantListContract;
import io.yon.android.model.Restaurant;
import io.yon.android.repository.Lce;
import io.yon.android.repository.RestaurantListRepository;
import io.yon.android.util.RxUtils;
import io.yon.android.view.MvpView;

/**
 * Created by amirhosein on 8/28/2017 AD.
 */

public class RestaurantListPresenter extends Presenter implements RestaurantListContract.Presenter {

    private RestaurantListContract.View view;

    private Observable<Lce<List<Restaurant>>> restaurantsObservable;

    public RestaurantListPresenter(Application application) {
        super(application);
    }

    @Override
    public void bindView(MvpView view) {
        this.view = (RestaurantListContract.View) view;
    }

    @Override
    public void loadRestaurantList(int id) {
        if (restaurantsObservable == null)
            restaurantsObservable = RestaurantListRepository.getInstance()
                    .getRestaurantList(id)
                    .compose(RxUtils.applySchedulers())
                    .cache();

        restaurantsObservable.takeWhile(LifecycleBinder.notDestroyed(view))
                .compose(LifecycleBinder.subscribeWhenReady(view, new Lce.Observer<>(
                        lce -> {
                            if (lce.isLoading())
                                view.showRlLoading();
                            else if (lce.hasError()) {
                                restaurantsObservable = null;
                                view.showRlError(lce.getError());
                            } else
                                view.showRl(lce.getData());
                        }
                )));
    }
}
