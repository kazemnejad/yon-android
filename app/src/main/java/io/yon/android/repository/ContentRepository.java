package io.yon.android.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;
import io.yon.android.Config;
import io.yon.android.api.WebService;
import io.yon.android.api.response.BannersShowcaseItem;
import io.yon.android.api.response.CompactRecomShowcaseItem;
import io.yon.android.api.response.SearchResponse;
import io.yon.android.api.response.ShowcaseItem;
import io.yon.android.api.response.ShowcaseResponse;
import io.yon.android.api.response.SimpleSectionShowcaseItem;
import io.yon.android.api.response.SingleBannerShowcaseItem;
import io.yon.android.api.response.TagRecomShowcaseItem;
import io.yon.android.api.response.ZoneRecomShowcaseItem;
import io.yon.android.model.SearchResultSection;
import io.yon.android.model.Zone;
import okhttp3.ResponseBody;
import retrofit2.Response;

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
                .compose(removeResponseWrapper())
                .compose(polishData());
    }

    public Observable<Lce<List<Object>>> getShowcaseFromCache(Context context) {
        return showcaseCache(context.getApplicationContext())
                .map(Lce::data)
                .startWith(Lce.loading())
                .onErrorReturn(Lce::error)
                .compose(polishData());
    }

    public Observable<Lce<List<SearchResultSection>>> search(String query) {
        return WebService.getInstance()
                .search(query)
                .map(toSearchResultSections())
                .map(Lce::data)
                .startWith(Lce.loading())
                .onErrorReturn(Lce::error);
    }

    private Observable<ShowcaseResponse> showcaseCache(Context context) {
        return Observable.create(e -> {
            try {
                SharedPreferences pref = Config.get(context.getApplicationContext());

                ShowcaseResponse response = loadDataFromCache(pref);
                if (response == null)
                    throw new NullPointerException();

                e.onNext(response);
                e.onComplete();
            } catch (Exception exp) {
                e.onError(exp);
            }
        });
    }

    private Response<ResponseBody> saveDataToCache(SharedPreferences pref, Response<ResponseBody> r) {
        if (r == null || !r.isSuccessful())
            return r;

        try {
            String resBody = r.body().string();
            pref.edit().putString(Config.Field.ShowCase, resBody).apply();
        } catch (Exception e) {
            Logger.e(e, "Unable to save results to cache");
        }

        return r;
    }

    private ShowcaseResponse loadDataFromCache(SharedPreferences pref) throws IOException, ClassNotFoundException {
        String resBody = pref.getString(Config.Field.ShowCase, "");
        return WebService.getBodyFromJson(resBody, ShowcaseResponse.class);
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

    private static ObservableTransformer<Lce<Response<ResponseBody>>, Lce<ShowcaseResponse>> removeResponseWrapper() {
        return upstream -> upstream.map(lce -> {
            if (lce.hasError())
                return Lce.error(lce.getError());
            else if (lce.isLoading())
                return Lce.loading();
            else if (lce.getData() != null)
                try {
                    return Lce.data(WebService.getBodyFromJson(lce.getData().body().string(), ShowcaseResponse.class));
                } catch (Exception exp) {
                    return Lce.error(exp);
                }

            return Lce.error(new NullPointerException());
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

    private static Function<SearchResponse, List<SearchResultSection>> toSearchResultSections() {
        return searchResponse -> {
            ArrayList<SearchResultSection> sections = new ArrayList<>();
            if (searchResponse.getZones() != null && searchResponse.getZones().size() > 0)
                sections.add(new SearchResultSection<>("منطقه‌ها", searchResponse.getZones()));

            if (searchResponse.getTags() != null && searchResponse.getTags().size() > 0)
                sections.add(new SearchResultSection<>("ویژگی‌ها", searchResponse.getTags()));

            if (searchResponse.getRestaurants() != null && searchResponse.getRestaurants().size() > 0)
                sections.add(new SearchResultSection<>("رستوران‌ها", searchResponse.getRestaurants()));

            return sections;
        };
    }

    public static SearchResponse createSearchResponse() {
        SearchResponse response = new SearchResponse();
        response.setTags(TagRepository.createTags());
        response.setRestaurants(RestaurantRepository.createRestaurantList());
        response.setZones(createZones());
        return response;
    }

    public static List<Zone> createZones() {
        ArrayList<Zone> zones = new ArrayList<>();
        zones.add(new Zone("انقلاب", 0, 0));
        zones.add(new Zone("جمهوری", 0, 0));
        zones.add(new Zone("ولیعصر", 0, 0));
        zones.add(new Zone("سعادت‌آباد", 0, 0));
        return zones;
    }

    private interface ItemValidator<T> {
        boolean validate(T i);
    }

    private interface ItemExtractor<T> {
        Object extract(T i);
    }
}
