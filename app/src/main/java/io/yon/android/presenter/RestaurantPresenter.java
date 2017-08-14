package io.yon.android.presenter;

import android.app.Application;

import com.waylonbrown.lifecycleawarerx.LifecycleBinder;

import java.util.List;

import io.reactivex.Observable;
import io.yon.android.contract.RestaurantContract;
import io.yon.android.model.MenuSection;
import io.yon.android.model.Restaurant;
import io.yon.android.repository.Lce;
import io.yon.android.repository.RestaurantRepository;
import io.yon.android.util.RxUtils;
import io.yon.android.view.MvpView;

/**
 * Created by amirhosein on 8/10/2017 AD.
 */

public class RestaurantPresenter extends Presenter implements RestaurantContract.Presenter {

    private RestaurantContract.View view;

    private Observable<Lce<Restaurant>> restaurantObservable;
    private Observable<Lce<List<MenuSection>>> menuObservable;

    public RestaurantPresenter(Application application) {
        super(application);
    }

    @Override
    public void bindView(MvpView view) {
        this.view = (RestaurantContract.View) view;
    }

    @Override
    public void loadRestaurant(int id) {
        if (restaurantObservable == null)
            restaurantObservable = RestaurantRepository.getInstance()
                    .getRestaurant()
                    .compose(RxUtils.applySchedulers())
                    .cache();

        restaurantObservable.takeWhile(LifecycleBinder.notDestroyed(view))
                .compose(LifecycleBinder.subscribeWhenReady(view, new Lce.Observer<>(
                        lce -> {
                            if (lce.isLoading())
                                view.showLoading();
                            else if (lce.hasError()) {
                                restaurantObservable = null;
                                view.showError(lce.getError());
                            } else
                                view.showRestaurant(lce.getData());
                        }
                )));
    }

    public void loadRestaurant(int id, boolean skipCache) {
        if (skipCache)
            restaurantObservable = null;

        loadRestaurant(id);
    }

    @Override
    public void loadRestaurantMenu(int id) {
        if (menuObservable == null)
            menuObservable = RestaurantRepository.getInstance()
                    .getRestaurantMenu(id)
                    .compose(RxUtils.applySchedulers())
                    .cache();

        menuObservable.takeWhile(LifecycleBinder.notDestroyed(view))
                .compose(LifecycleBinder.subscribeWhenReady(view, new Lce.Observer<>(
                        lce -> {
                            if (lce.isLoading())
                                view.showLoading();
                            else if (lce.hasError()) {
                                menuObservable = null;
                                view.showError(lce.getError());
                            } else
                                view.showRestaurantMenu(lce.getData());
                        }
                )));
    }

    public void loadRestaurantMenu(int id, boolean skipCache) {
        if (skipCache)
            menuObservable = null;

        loadRestaurantMenu(id);
    }

    @Override
    public void loadRestaurantReview(int id) {

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        this.view = null;
    }
}
