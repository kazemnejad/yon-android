package io.yon.android.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.yon.android.R;
import io.yon.android.model.Map;
import io.yon.android.model.Restaurant;
import io.yon.android.model.Table;
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
                .delay(700, TimeUnit.MILLISECONDS)
                .map(Lce::data)
                .map(lce -> {
                    Collections.reverse(lce.getData().getMaps());
                    return lce;
                })
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

        ArrayList<Map> maps = new ArrayList<>();
        maps.add(createMap("همکف"));
        maps.add(createMap("محوطه باز"));
        r.setMaps(maps);

        return r;
    }

    private static Tag makeTag(String name) {
        Tag t = new Tag();
        t.setName(name);

        return t;
    }

    private static Map createMap(String name) {
        Map m = new Map();
        m.setName(name);
        m.setWidth(4.6f);
        m.setHeight(2.5f);

        ArrayList<Table> tables = new ArrayList<>();
        tables.add(makeTable(0.5f, 0.5f));
        tables.add(makeTable(1.7f, 0.5f));
        tables.add(makeTable(2.9f, 0.5f));
        tables.add(makeTable(4.1f, 0.5f));
        Table t = makeTable(0.5f, 2f);
        t.setAngle(45);
        tables.add(t);

        m.setTables(tables);

        return m;
    }

    private static Table makeTable(float x, float y) {
        Table t = new Table();
        t.setX(x);
        t.setY(y);
        t.setShape(1);
//        t.setAngle(45f);

        return t;
    }
}
