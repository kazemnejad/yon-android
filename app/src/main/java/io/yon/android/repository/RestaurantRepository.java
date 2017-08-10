package io.yon.android.repository;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.yon.android.R;
import io.yon.android.model.Restaurant;
import io.yon.android.model.Tag;

/**
 * Created by amirhosein on 8/10/2017 AD.
 */

public class RestaurantRepository {
    private static RestaurantRepository instance;

    public static RestaurantRepository getInstance() {
        if (instance == null)
            instance = new RestaurantRepository();

        return instance;
    }

    public Observable<Lce<Restaurant>> getRestaurant() {
        return Observable.just(createRestaurant())
                .delay(1500, TimeUnit.MILLISECONDS)
                .map(Lce::data)
                .startWith(Lce.loading())
                .onErrorReturn(Lce::error);
    }


    private static Restaurant createRestaurant() {
        Restaurant r = new Restaurant();
        r.setName("رستوران");
        r.setRate(3.4f);
        r.setPrice(4.9f);
        r.setAvatarUrl("http://162.243.174.32/restaurant_avatars/1166.jpeg");
        r.setBannerUrl("http://www.pizzaeast.com/system/files/032016/56fd2c58ebeeb56aa00d9df6/large/24.3.16_pizzaeast2052.jpg?1459432756");

        ArrayList<Tag> tags = new ArrayList<>();
        tags.add(makeTag("ایتالیایی"));
        tags.add(makeTag("هندی"));
        tags.add(makeTag("چینی"));
        tags.add(makeTag("فست‌فود"));
        tags.add(makeTag("سالاد‌ بار"));
        tags.add(makeTag("سنتی"));
        tags.add(makeTag("اتاق سیگار"));
        tags.add(makeTag("سیگار ممنوع"));
        tags.add(makeTag("حجاب اسلامی"));
        tags.add(makeTag("اصغر"));
        r.setTags(tags);

        return r;
    }

    private static Tag makeTag(String name) {
        Tag t = new Tag();
        t.setName(name);

        return t;
    }
}
