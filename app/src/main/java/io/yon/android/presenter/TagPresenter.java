package io.yon.android.presenter;

import android.app.Application;

import com.waylonbrown.lifecycleawarerx.LifecycleBinder;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.yon.android.contract.TagContract;
import io.yon.android.model.Restaurant;
import io.yon.android.model.Tag;
import io.yon.android.repository.Lce;
import io.yon.android.repository.RestaurantRepository;
import io.yon.android.repository.TagRepository;
import io.yon.android.util.RxUtils;
import io.yon.android.view.MvpView;

/**
 * Created by amirhosein on 8/30/2017 AD.
 */

public class TagPresenter extends Presenter implements TagContract.Presenter {

    private List<Tag> selectedTags = new ArrayList<>();

    private TagContract.View view;

    private Observable<Lce<List<Restaurant>>> restaurantsObservable;
    private Observable<Lce<List<Tag>>> tagObservable;

    private long lastRequestTime = -1;

    public TagPresenter(Application application) {
        super(application);
    }

    public List<Tag> getSelectedTags() {
        return selectedTags;
    }

    public void setInitialTag(Tag tag) {
        if (selectedTags.size() == 0 && lastRequestTime == -1)
            selectedTags.add(tag);
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
        if (tags.size() == 0)
            return;

        if (skipCache)
            restaurantsObservable = null;

        loadRestaurants(tags);
    }

    @Override
    public void loadTags() {
        if (tagObservable == null)
            tagObservable = TagRepository.getInstance()
                    .getTags()
                    .compose(RxUtils.applySchedulers())
                    .cache();

//        tagObservable.takeWhile(LifecycleBinder.notDestroyed(view))
//                .compose(LifecycleBinder.subscribeWhenReady(view, new Lce.Observer<>(
//                        lce -> {
//                            if (lce.isLoading())
//                                view.showLoading();
//                            else if (lce.hasError()) {
//                                lce.getError().printStackTrace();
//                                tagObservable = null;
//                                loadTags();
//                            } else {
//                                view.showTags(lce.getData());
//                            }
//                        }
//                )));
        tagObservable.subscribe(new Lce.Observer<>(
                lce -> {
                    if (view == null)
                        return;

                    if (lce.isLoading())
                        view.showLoading();
                    else if (lce.hasError()) {
                        lce.getError().printStackTrace();
                        tagObservable = null;
                        loadTags();
                    } else {
                        view.showTags(lce.getData());
                    }
                }
        ));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        view = null;
    }
}
