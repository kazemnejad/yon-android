package io.yon.android.presenter;

import android.app.Application;

import com.waylonbrown.lifecycleawarerx.LifecycleBinder;

import java.util.List;

import io.reactivex.Observable;
import io.yon.android.contract.TestContract;
import io.yon.android.model.Restaurant;
import io.yon.android.repository.Lce;
import io.yon.android.repository.RestaurantRepository;
import io.yon.android.util.RxUtils;
import io.yon.android.view.MvpView;

/**
 * Created by amirhosein on 8/27/2017 AD.
 */

public class TestPresenter extends Presenter implements TestContract.Presenter {

    private TestContract.View view;

    private Observable<Lce<List<Restaurant>>> rlObservable;

    public TestPresenter(Application application) {
        super(application);
    }

    @Override
    public void bindView(MvpView view) {
        this.view = (TestContract.View) view;
    }

    @Override
    public void loadTestRestaurantList() {
        if (rlObservable == null)
            rlObservable = RestaurantRepository.getInstance()
                    .getTestRestaurantList()
                    .compose(RxUtils.applySchedulers())
                    .cache();

        rlObservable.takeWhile(LifecycleBinder.notDestroyed(view))
                .compose(LifecycleBinder.subscribeWhenReady(view, new Lce.Observer<>(
                        lce -> {
                            if (lce.isLoading())
                                view.showRlLoading();
                            else if (!lce.hasError())
                                view.showRl(lce.getData());
                        }
                )));
    }
}
