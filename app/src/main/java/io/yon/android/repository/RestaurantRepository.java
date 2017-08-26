package io.yon.android.repository;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.yon.android.R;
import io.yon.android.model.Eatable;
import io.yon.android.model.Map;
import io.yon.android.model.MenuSection;
import io.yon.android.model.OpenTimeSlot;
import io.yon.android.model.OpenTimeSlotSection;
import io.yon.android.model.OpeningInterval;
import io.yon.android.model.Restaurant;
import io.yon.android.model.Table;
import io.yon.android.model.Tag;
import io.yon.android.model.UserReview;
import io.yon.android.util.calendar.PersianCalendar;

/**
 * Created by amirhosein on 8/10/2017 AD.
 */

public class RestaurantRepository {
    public final static int MIN_START_POINT = 7 * 3600;

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

    public Observable<Lce<List<OpenTimeSlotSection>>> getRestaurantOpenHours(Context context, PersianCalendar date) {
        return Observable.just(createOpenHour())
                .delay(700, TimeUnit.MILLISECONDS)
                .map(Lce::data)
                .map(l -> Lce.data(generateOpenTimeSlots(context, date, l.getData())))
                .startWith(Lce.loading())
                .onErrorReturn(Lce::error);
    }

    private static List<OpeningInterval> createOpenHour() {
        List<OpeningInterval> openHour = new ArrayList<>();
        openHour.add(new OpeningInterval(8 * 3600, 10 * 3600 - 26 * 60));
        openHour.add(new OpeningInterval(12 * 3600, 15 * 3600 - 15 * 60));
        openHour.add(new OpeningInterval(17 * 3600 + 13 * 60, 21 * 3600 + 21 * 60));
        return openHour;
    }

    private static List<OpenTimeSlotSection> generateOpenTimeSlots(Context context, PersianCalendar date, List<OpeningInterval> openHours) {
        date.setTimeZone(TimeZone.getTimeZone("Asia/Tehran"));
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        int initialTimeStamp = (int) (date.getTimeInMillis() / 1000L);

        ArrayList<OpenTimeSlot> slots = new ArrayList<>();
        if (openHours.size() == 0)
            return new ArrayList<>();


        openHours.sort((a, b) -> a.getStart() < b.getStart() ? -1 : a.getStart() == b.getStart() ? 0 : 1);

        int point = getStartPoint(openHours.get(0));

        int i = 0;
        while (i < openHours.size()) {
            OpeningInterval interval = openHours.get(i);
            while (true) {
                OpenTimeSlot slot = new OpenTimeSlot(initialTimeStamp, point);
                if (point >= interval.getStart()) {
                    if (point < interval.getEnd())
                        slot.setEnable(true);
                    else {
                        i++;
                        break;
                    }
                }

                slots.add(slot);
                point += 30 * 60;
            }
        }

        return generateSlotSections(context, slots);
    }

    private static List<OpenTimeSlotSection> generateSlotSections(Context context, List<OpenTimeSlot> slots) {
        ArrayList<OpenTimeSlotSection> sections = new ArrayList<>();
        sections.add(new OpenTimeSlotSection(context.getString(R.string.breakfast), 4, 11));
        sections.add(new OpenTimeSlotSection(context.getString(R.string.lunch), 11, 17));
        sections.add(new OpenTimeSlotSection(context.getString(R.string.dinner), 17, 23));

        int i = 0;
        for (OpenTimeSlotSection section : sections) {
            while (i < slots.size()) {
                OpenTimeSlot slot = slots.get(i);
                if (slot.getDatetime().get(Calendar.HOUR_OF_DAY) >= section.getStartHour()
                        && slot.getDatetime().get(Calendar.HOUR_OF_DAY) < section.getEndHour())
                    section.getOpenTimeSlots().add(slot);
                else
                    break;

                i++;
            }
        }

        return sections;
    }


    private static int getStartPoint(OpeningInterval interval) {
        int start = 0;
        while (start < interval.getStart())
            start += 30 * 60;

        return start < interval.getEnd() ? start : -2;
    }

    public static Restaurant createRestaurant() {
        Restaurant r = new Restaurant();
        r.setId(5);
        r.setName("رستوران اصغر جوجه");
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
        Map m = createMap("محوطه باز");
        m.getTables().forEach(table -> table.setId(String.valueOf(new Random().nextInt())));
        maps.add(m);
        maps.add(createMap("همکف"));
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
        tables.add(makeTable("table1", 0.5f, 0.5f));
        tables.add(makeTable("table0", 1.7f, 0.5f));
        tables.add(makeTable("table99", 2.9f, 0.5f));
        tables.add(makeTable("table5", 4.1f, 0.5f));

        Table t2 = makeTable("table8", 4.1f, 2f);
        t2.setShape(5);
        t2.setCount(8);
        tables.add(t2);

        Table t = makeTable("table88", 0.5f, 2f);
        t.setAngle(45);
        tables.add(t);

        m.setTables(tables);

        return m;
    }

    private static Table makeTable(String id, float x, float y) {
        Table t = new Table();
        t.setId(id);
        t.setX(x);
        t.setY(y);
        t.setShape(1);
        t.setCount(4);
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
        reviews.add(new UserReview("اصغر", "لورم ایپسوم متن ساختگی با تولید سادگی نامفهوم از صنعت چاپ و با استفاده از طراحان گرافیک است. چاپگرها و متون بلکه روزنامه و مجله", 1.4f, 1502792548));
        reviews.add(new UserReview("تقی", "نوع با هدف بهبود ابزارهای کاربردی می باشد. کتابهای زیادی در شصت و سه درصد گذشته، حال و آینده شناخت فراوان جامعه و متخصصان را می طلبد تا با نرم افزارها شناخت بیشتری!", 4.5f, 1502792548));
        reviews.add(new UserReview("نقی", "رم ایپسوم متن ساختگی با تولید سادگی نامفهوم از صنعت چاپ و با استفاده از !", 4.0f, 1502792548));
        reviews.add(new UserReview("مموتی", "سینه‌ی مرغشونو لولو برده بود:|", 2.6f, 1502792548));
        reviews.add(new UserReview("ترامپ", "طلبد تا با نرم افزارها شناخت بیشتری را برای طراحان رایانه ای علی الخصوص طراحان خلاقی و فرهنگ پیشرو در زبان فارسی ایجاد کرد. در این صورت می توان امید داشت که تمام و د", 0f, 1502792548));
        reviews.add(new UserReview("نتانیاهو", "موجود در ارائه راهکارها و شرایط سخت تایپ به پایان رسد وزمان مورد نیاز شامل حروفچینی دستاوردهای اصلی و جوابگوی سوالات پیوسته اهل دنیای موجود طراحی اساسا مورد استفاده قرار گیرد.", 3.2f, 1502792548));
        reviews.add(new UserReview("شاهزاده عربستان", "صت و سه درصد گذشته، حال و آینده شناخت فراوان جامعه و متخصصان را می طلبد تا با نرم افزارها شناخت بیشتری را برای طراحان رایانه ای علی الخصوص طراحان خلاقی و ", 5f, 1502792548));

        return reviews;
    }

}
