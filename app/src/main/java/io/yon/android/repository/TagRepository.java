package io.yon.android.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.yon.android.model.Tag;

/**
 * Created by amirhosein on 8/30/2017 AD.
 */

public class TagRepository {
    private static TagRepository instance;

    public static TagRepository getInstance() {
        if (instance == null)
            instance = new TagRepository();

        return instance;
    }

    public Observable<Lce<List<Tag>>> getTags() {
        return Observable.just(createTags())
                .delay(700, TimeUnit.MILLISECONDS)
                .map(Lce::data)
                .startWith(Lce.loading())
                .onErrorReturn(Lce::error);
    }

    private static List<Tag> createTags() {
        Tag t1 = RestaurantRepository.makeTag("هندی۴۵");
        Tag t4 = RestaurantRepository.makeTag("هندی۵۱");
        Tag t2 = RestaurantRepository.makeTag("هندی۵۲");
        Tag t3 = RestaurantRepository.makeTag("هندی۵۳");

        ArrayList<Tag> tags = new ArrayList<>();
        tags.add(RestaurantRepository.makeTag("هندی"));
        tags.add(t1);
        tags.add(RestaurantRepository.makeTag("هندی۲"));
        tags.add(t3);
        tags.add(RestaurantRepository.makeTag("هندی۳"));
        tags.add(RestaurantRepository.makeTag("هندی۴"));

        return tags;
    }
}
