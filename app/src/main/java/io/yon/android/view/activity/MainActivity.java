package io.yon.android.view.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import io.yon.android.R;
import io.yon.android.api.response.SimpleSectionShocaseItem;
import io.yon.android.model.Banner;
import io.yon.android.model.RecommendationList;
import io.yon.android.model.Restaurant;
import io.yon.android.model.Tag;
import io.yon.android.model.Zone;
import io.yon.android.util.RxBus;
import io.yon.android.view.GlideApp;
import io.yon.android.view.adapter.ShowcaseAdapter;
import io.yon.android.view.widget.ShowcaseOnScrollListener;


public class MainActivity extends Activity {

    private RecyclerView recyclerView;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionMenu(true);
        setTitle(R.string.app_name);

        initView();
        fillDummyContent();
    }

    @Override
    protected void findViews() {
        recyclerView = (RecyclerView) findViewById(R.id.mother_recycler_view);
    }

    private void initView() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setItemPrefetchEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new ShowcaseOnScrollListener(this) {
            @Override
            protected View findViewById(int id) {
                return MainActivity.this.findViewById(id);
            }
        });

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            GlideApp.with(MainActivity.this).resumeRequests();
                        }
                        if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                            GlideApp.with(MainActivity.this).pauseRequests();
                        }
                        super.onScrollStateChanged(recyclerView, newState);

                    }
                });
            }
        });
        t.start();
    }

    private void fillDummyContent() {
        ArrayList<Object> data = new ArrayList<>();
        data.add(makeBanners());
        data.add(makeSimpleSection());
        data.add(makeRecommendations());
        data.add(makeRecommendedTags());
        data.add(makeSingleBanner());
        data.add(makeZones());
        data.add(makeRecommendations());
        data.add(makeSimpleSection());
        data.add(makeSingleBanner());
        data.add(makeSingleBanner());
        data.add(makeRecommendedTags());
        data.add(makeZones());

        recyclerView.setAdapter(new ShowcaseAdapter(this, new RxBus(), data));
    }

    private List<Banner> makeBanners() {
        String[] bannerUrls = {
                "http://www.julios.co.za/wp-content/uploads/2012/10/restaurant.jpeg",
                "https://www.soelden.com/urlaub/images/SD/WI/headerbilder/aktivitaeten_header_restaurant,method=scale,prop=data,id=1200-510.jpg",
                "http://s.eatthis-cdn.com/media/images/ext/320757027/sugary-restaurant-meals-chilis-honeycrispers.jpg",
                "http://www.brownstone-restaurant.com/img/kudos-2.jpg"
        };

        ArrayList<Banner> banners = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Banner banner = new Banner();
            banner.setTitle("Title " + String.valueOf(i + 1));
            banner.setSubTitle("Sub fucking title " + String.valueOf(i + 1));
            banner.setBannerUrl(bannerUrls[i % 4]);
            banners.add(banner);
        }

        return banners;
    }

    private SimpleSectionShocaseItem makeSimpleSection() {
        String[] avatarUrls = {
                "https://www.reyhoon.com/images/logo/1665.jpeg?_=1500713079",
                "https://www.reyhoon.com/images/logo/1166.jpeg?_=1488296020",
                "https://www.reyhoon.com/images/logo/410.jpg?_=1498976539",
                "https://www.reyhoon.com/images/logo/616.jpeg?_=1488286265"
        };
        String[] names = {
                "توران",
                "اصغر جوجه",
                "اکبر جوجه",
                "بوف"
        };
        String[] addresses = {
                "انفلاب، امیرآباد، میدان دانشجو",
                "سعادت‌آباد، بلوار پیام",
                "رسالت، فرجام، دانشگاه علم‌و‌صنعت",
                "تجریش، باهنر، فضیه"
        };
        String[] rates = {
                "۴.۲",
                "۴.۹",
                "۳.۱",
                "۳.۸"
        };

        SimpleSectionShocaseItem section = new SimpleSectionShocaseItem();
        section.setTitle(getString(R.string.recent_visits));
        ArrayList<Restaurant> rests = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Restaurant r = new Restaurant();
            r.setName(names[i]);
            r.setAvatarUrl(avatarUrls[i]);
            r.setAddress(addresses[i]);
            r.setRate(rates[i]);

            rests.add(r);
        }

        section.setRestaurants(rests);

        return section;
    }

    private List<RecommendationList> makeRecommendations() {
        String[] titles = {
                "مکزیکی",
                "هندی",
                "جدید",
                "مجلل",
                "دنج",
                "ارزان",
        };

        ArrayList<RecommendationList> list = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            RecommendationList r = new RecommendationList();
            List<Restaurant> restaurants = makeSimpleSection().getRestaurants();
            restaurants.remove(0);
            r.setRestaurants(restaurants);
            r.setTitle(titles[i]);

            list.add(r);
        }

        return list;
    }

    private List<Tag> makeRecommendedTags() {
        String[] names = {
                "ایتالیایی",
                "فست‌فود",
                "سنتی",
                "سوشی",
                "برگر",
                "اتاق سیگار"
        };

        String[] avatarUrls = {
                "http://viztangocafe.com/wp-content/uploads/2015/06/food2.jpg",
                "http://s.eatthis-cdn.com/media/images/ext/336492655/fast-food.jpg",
                "http://www.iranvisitor.com/images/content_images/persian-kebab-1.jpg",
                "http://touristmeetstraveler.com/wp-content/uploads/sushi.jpg",
                "http://lifecdn.dailyburn.com/life/wp-content/uploads/2014/10/Food-Tracker-4.jpg",
                "https://media-cdn.tripadvisor.com/media/photo-s/04/37/06/36/rook-ruimte-smoking-room.jpg"
        };

        ArrayList<Tag> tags = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Tag t = new Tag();
            t.setName(names[i]);
            t.setAvatarUrl(avatarUrls[i]);

            tags.add(t);
        }

        return tags;
    }

    private List<Zone> makeZones() {
        String[] names = {
                "انقلاب",
                "امیرآباد",
                "ظالقانی",
                "جمهوری",
                "سگ‌دونی",
                "بولشت‌دونی"
        };

        String[] avatarUrls = {
                "http://uupload.ir/files/hhki_screenshot_from_2017-08-01_12-09-30.png",
                "http://uupload.ir/files/xhi5_screenshot_from_2017-08-01_12-09-16.png",
                "http://uupload.ir/files/0pba_screenshot_from_2017-08-01_12-09-05.png",
                "http://uupload.ir/files/5o9f_screenshot_from_2017-08-01_12-08-53.png",
                "http://uupload.ir/files/nq9z_screenshot_from_2017-08-01_12-08-41.png",
                "http://uupload.ir/files/8m6g_screenshot_from_2017-08-01_12-08-29.png"
        };
        ArrayList<Zone> zones = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Zone z = new Zone();
            z.setName(names[i]);
            z.setAvatarUrl(avatarUrls[i]);

            zones.add(z);
        }

        return zones;
    }

    private Banner makeSingleBanner() {
        Banner banner = new Banner();
        banner.setTitle("کافه یونچه");
        banner.setSubTitle("انواع پاستا و سوسیس‌تخم‌مرغ‌های لذیذ");
        banner.setBannerUrl("http://uupload.ir/files/6oz8_3cf396ac-f45b-4ba9-8506-eca480a0d967.png");
        banner.setBackgroundUrl("http://food.fnr.sndimg.com/content/dam/images/food/fullset/2011/2/4/1/RX-FNM_030111-Lighten-Up-012_s4x3.jpg.rend.hgtvcom.616.462.suffix/1382539856907.jpeg");
        banner.setRate("۵");
        banner.setColorCode("#d8b700");
        return banner;
    }

    @Override
    public boolean onCreateOptionMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.create_new:
                Log.d("menu", "createNew");
                return true;

            case R.id.open:
                Log.d("menu", "opn");
                return true;
        }
        return false;
    }
}
