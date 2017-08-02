package io.yon.android.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.orhanobut.logger.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.yon.android.Config;
import io.yon.android.api.WebService;
import io.yon.android.api.response.BannersShowcaseItem;
import io.yon.android.api.response.CompactRecomShowcaseItem;
import io.yon.android.api.response.ShowcaseItem;
import io.yon.android.api.response.ShowcaseResponse;
import io.yon.android.api.response.SimpleSectionShowcaseItem;
import io.yon.android.api.response.SingleBannerShowcaseItem;
import io.yon.android.api.response.TagRecomShowcaseItem;
import io.yon.android.api.response.ZoneRecomShowcaseItem;

/**
 * Created by amirhosein on 8/2/17.
 */

public class ContentRepository {
    private static ContentRepository instance;

    public static ContentRepository getInstance() {
        if (instance == null)
            instance = new ContentRepository();

        return instance;
    }

    public Observable<Lce<List<Object>>> getShowcase(Context context) {
        SharedPreferences pref = Config.getCache(context.getApplicationContext());

        return WebService.getInstance()
                .getHomePage(-1, -1)
                .map(Lce::data)
                .startWith(Lce.loading())
                .onErrorReturn(Lce::error)
                /*.map(lce -> !lce.hasError() || !lce.isLoading() ? Lce.data(saveDataToCache(pref, lce.getData())) : lce)*/
                .compose(polishData());
    }

    public Observable<Lce<List<Object>>> getShowcaseFromCache(Context context) {
        return getHomePageFromCache(context.getApplicationContext())
                .map(Lce::data)
                .startWith(Lce.loading())
                .onErrorReturn(Lce::error)
                .compose(polishData());
    }

    private Observable<ShowcaseResponse> getHomePageFromCache(Context context) {
        return Observable.create(e -> {
            try {
                SharedPreferences pref = Config.get(context.getApplicationContext());
                e.onNext(loadDataFromCache(pref));
                e.onComplete();
            } catch (Exception exp) {
                e.onError(exp);
            }
        });
    }

    private ShowcaseResponse saveDataToCache(SharedPreferences pref, ShowcaseResponse r) {

        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(r);
            so.flush();
            pref.edit().putString(Config.Field.ShowCase, bo.toString()).apply();
        } catch (Exception e) {
            Logger.e(e, "Unable to save results to cache");
        }

        return r;
    }

    private ShowcaseResponse loadDataFromCache(SharedPreferences pref) throws IOException, ClassNotFoundException {
        byte b[] = pref.getString(Config.Field.ShowCase, "").getBytes();
        ByteArrayInputStream bi = new ByteArrayInputStream(b);
        ObjectInputStream si = new ObjectInputStream(bi);
        return (ShowcaseResponse) si.readObject();
    }

    private static ObservableTransformer<Lce<ShowcaseResponse>, Lce<List<Object>>> polishData() {
        return upstream -> upstream.map(lce -> {
            if (lce.hasError())
                return Lce.error(lce.getError());
            else if (lce.isLoading())
                return Lce.loading();
            else
                return Lce.data(convert(lce.getData()));
        });
    }

    private static List<Object> convert(ShowcaseResponse sr) {
        if (sr == null || sr.getItems() == null)
            return new ArrayList<>();

        ArrayList<Object> lst = new ArrayList<>();
        for (ShowcaseItem item : sr.getItems())
            if (item instanceof BannersShowcaseItem)
                addToListIf(lst, (BannersShowcaseItem) item, BannersShowcaseItem::getBanners, i -> i.getBanners() != null && i.getBanners().size() > 0);
            else if (item instanceof SimpleSectionShowcaseItem)
                addToListIf(lst, (SimpleSectionShowcaseItem) item, i -> i, i -> i.getRestaurants() != null && i.getRestaurants().size() > 0);
            else if (item instanceof CompactRecomShowcaseItem)
                addToListIf(lst, (CompactRecomShowcaseItem) item, CompactRecomShowcaseItem::getCompactRecommendation, i -> i.getCompactRecommendation() != null && i.getCompactRecommendation().size() > 0);
            else if (item instanceof TagRecomShowcaseItem)
                addToListIf(lst, (TagRecomShowcaseItem) item, TagRecomShowcaseItem::getTags, i -> i.getTags() != null && i.getTags().size() > 0);
            else if (item instanceof ZoneRecomShowcaseItem)
                addToListIf(lst, (ZoneRecomShowcaseItem) item, ZoneRecomShowcaseItem::getZones, i -> i.getZones() != null && i.getZones().size() > 0);
            else if (item instanceof SingleBannerShowcaseItem)
                addToListIf(lst, (SingleBannerShowcaseItem) item, SingleBannerShowcaseItem::getBanner, i -> i.getBanner() != null);


        return lst;
    }

    private static <T> void addToListIf(List<Object> lst, T item, ItemExtractor<T> extractor, ItemValidator<T> validator) {
        if (validator.validate(item))
            lst.add(extractor.extract(item));
    }

    private interface ItemValidator<T> {
        boolean validate(T i);
    }

    private interface ItemExtractor<T> {
        Object extract(T i);
    }
}
