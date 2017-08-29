package io.yon.android.presenter;

import android.app.Application;

import com.waylonbrown.lifecycleawarerx.LifecycleBinder;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.yon.android.contract.TagContract;
import io.yon.android.contract.TestContract;
import io.yon.android.model.Restaurant;
import io.yon.android.model.Tag;
import io.yon.android.repository.Lce;
import io.yon.android.repository.RestaurantRepository;
import io.yon.android.util.RxUtils;
import io.yon.android.view.MvpView;

/**
 * Created by amirhosein on 8/30/2017 AD.
 */

public class TagPresenter extends Presenter implements TagContract.Presenter {

    private List<Tag> selectedTags = new ArrayList<>();

    private TagContract.View view;

    private Observable<Lce<List<Restaurant>>> restaurantsObservable;

    private long lastRequestTime;

    public TagPresenter(Application application) {
        super(application);
    }

    public List<Tag> getSelectedTags() {
        return selectedTags;
    }

    @Override
    public void bindView(MvpView view) {
        this.view = (TagContract.View) view;
    }

    @Override
    public void loadRestaurants(List<Tag> tags) {
        if (restaurantsObservable == null)
            restaurantsObservable = RestaurantRepository.getInstance()
                    .getRestaurantsByTags(tags)
                    .compose(RxUtils.applySchedulers())
                    .cache();

        long finalLastRequestTime = System.currentTimeMillis();
        lastRequestTime = finalLastRequestTime;

        restaurantsObservable.takeWhile(LifecycleBinder.notDestroyed(view))
                .compose(LifecycleBinder.subscribeWhenReady(view, new Lce.Observer<>(
                        lce -> {
                            if (lastRequestTime > finalLastRequestTime)
                                return;

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

    public void loadRestaurants(List<Tag> tags, boolean skipCache) {
        if (skipCache)
            restaurantsObservable = null;

        loadRestaurants(tags);
    }
}
