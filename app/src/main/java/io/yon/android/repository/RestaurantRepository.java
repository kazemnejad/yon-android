package io.yon.android.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.yon.android.model.Eatable;
import io.yon.android.model.Map;
import io.yon.android.model.MenuSection;
import io.yon.android.model.Restaurant;
import io.yon.android.model.Table;
import io.yon.android.model.Tag;
import io.yon.android.model.UserReview;

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

    public Observable<Lce<List<MenuSection>>> getRestaurantMenu(int restaurantId) {
        return Observable.just(createMenu())
                .delay(1700, TimeUnit.MILLISECONDS)
                .map(Lce::data)
                .startWith(Lce.loading())
                .onErrorReturn(Lce::error);
    }

    public Observable<Lce<List<UserReview>>> getRestaurantUserReviews(int restaurantId) {
        return Observable.just(createUserReviews())
                .delay(2400, TimeUnit.MILLISECONDS)
                .map(Lce::data)
                .startWith(Lce.loading())
                .onErrorReturn(Lce::error);
    }

    private static Restaurant createRestaurant() {
        Restaurant r = new Restaurant();
        r.setId(5);
        r.setName("رستوران");
        r.setRate(3.4f);
        r.setPrice(4.9f);
        r.setAvatarUrl("http://162.243.174.32/restaurant_avatars/1166.jpeg");
        r.setBannerUrl("http://www.pizzaeast.com/system/files/032016/56fd2c58ebeeb56aa00d9df6/large/24.3.16_pizzaeast2052.jpg?1459432756");

        ArrayList<Tag> tags = new ArrayList<>();
        tags.add(makeTag("هندی"));
        tags.add(makeTag("چینی"));
        tags.add(makeTag("فست‌فود"));
        tags.add(makeTag("سنتی"));
        tags.add(makeTag("دریایی"));
        tags.add(makeTag("اصغر"));
        tags.add(makeTag("سالاد‌ بار"));
        tags.add(makeTag("اتاق سیگار"));
        tags.add(makeTag("سیگار ممنوع"));
        tags.add(makeTag("حجاب اسلامی"));
        r.setTags(tags);

        ArrayList<Map> maps = new ArrayList<>();
        maps.add(createMap("همکف"));
        maps.add(createMap("محوطه باز"));
        r.setMaps(maps);

        r.setLongitude(35.70252);
        r.setLatitude(51.3958805);

        r.setAddress("سعادت‌آباد، میدان سرو، پیام");

        java.util.Map<String, String> info = new HashMap<>();
        info.put("phone", "۰۲۱ ۲۲۸۷ ۹۰۷۳");
        info.put("price_range", "از ۵،۰۰۰ تا ۴۰،۰۰۰ تومان");
        info.put("parking_space", "در خیابان مجاور فضای پارک موجود است.");
        info.put("opening_hour", "صبح: ۱۰:۳۰ تا ۱۲:۴۵\n" + "ظهر: ۱۳:۳۰ تا ۱۷:۲۰\n" + "شب: ۱۷:۰۰ تا ۲۳:۰۰\n");
        info.put("desc", "این رستورات رستوران بسیار با کیفیت و خوبی است و از آشپزان بسیار حازق و با کلاس از سراسر دنیا استفاده می\u200Cکند. و این متن صرفا برای خوش\u200Cنمک است.");
        r.setInfo(info);

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

    private static List<MenuSection> createMenu() {
        List<String> in1 = Arrays.asList("گوشت", "قارج", "پنیر", "گوجه");
        List<String> in2 = Arrays.asList("سوسیس", "کالباس", "گربه");
        List<String> in3 = Arrays.asList("گربه پا به ماه", "سوسیس", "زباله", "سس");

        Eatable e1 = new Eatable("پیتزای مخصوص", 7000, 4.3f, "https://www.reyhoon.com/images/foods/1400/%D8%A8%DB%8C%DA%A9%D9%86-%D8%A8%D8%B1%D9%87.jpg", in1);
        Eatable e2 = new Eatable("برگر دبل", 21400, 3.9f, "https://www.reyhoon.com/images/foods/1400/%D8%A8%DB%8C%DA%A9%D9%86-%D9%85%D8%AE%D8%B5%D9%88%D8%B5.jpg", in2);
        Eatable e3 = new Eatable("ساندویچ گوشت اعلا", 33600, 4.1f, "https://www.reyhoon.com/images/foods/1400/%D8%B3%D8%A7%D9%86%D8%AF%D9%88%DB%8C%DA%86-%D9%85%D8%AE%D9%84%D9%88%D8%B7.jpg", in3);

        ArrayList<MenuSection> menu = new ArrayList<>();

        MenuSection ms = new MenuSection();
        ms.setEatables(Arrays.asList(e1, e2, e3));
        ms.setName("غذای اصلی");
        menu.add(ms);

        ms = new MenuSection();
        ms.setEatables(Arrays.asList(e2, e3));
        ms.setName("پیش‌غذا");
        menu.add(ms);

        ms = new MenuSection();
        ms.setEatables(Arrays.asList(e1, e3, e2));
        ms.setName("دسر‌ها");
        menu.add(ms);

        ms = new MenuSection();
        ms.setEatables(Collections.singletonList(e2));
        ms.setName("نوشیدنی");
        menu.add(ms);

        return menu;
    }

    private static List<UserReview> createUserReviews() {
        ArrayList<UserReview> reviews = new ArrayList<>();
        reviews.add(new UserReview("اصغر", "کیفیت غذا بسیار داغون بود.", 1.4f, 2312321));
        reviews.add(new UserReview("تقی", "هممم!", 4.5f, 2312321));
        reviews.add(new UserReview("نقی", "مممم!", 4.0f, 2312321));
        reviews.add(new UserReview("مموتی", "سینه‌ی مرغشونو لولو برده بود:|", 2.6f, 2312321));
        reviews.add(new UserReview("ترامپ", "با مموتی قرار شد یه دیوار درستی بخوریم.", 0f, 2312321));
        reviews.add(new UserReview("نتانیاهو", "ترامپ قراره دنگ منم بده *ـــ*", 3.2f, 2312321));
        reviews.add(new UserReview("شاهزاده عربستان", "آقا این پیش‌خدمتش خیلی دافه 3>", 5f, 2312321));

        return reviews;
    }

}
