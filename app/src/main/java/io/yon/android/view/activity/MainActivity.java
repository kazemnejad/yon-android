package io.yon.android.view.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import io.yon.android.R;
import io.yon.android.model.Banner;
import io.yon.android.util.RxBus;
import io.yon.android.util.ViewUtils;
import io.yon.android.view.adapter.VaryingShowcaseAdapter;

public class MainActivity extends Activity {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionMenu(true);

        setTitle(R.string.app_name);

        ArrayList<Object> data = new ArrayList<>();
        data.add(makeBanners());
        data.add("sss");

        int height = ViewUtils.px(this, 200);
        boolean isPreLollipop = Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;
        View toolbarShadow = findViewById(R.id.toolbar_shadow);
        AppBarLayout appBar = (AppBarLayout) findViewById(R.id.appbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final View[] banners = {findViewById(R.id.banners)};

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.mother_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new VaryingShowcaseAdapter(data, new RxBus()));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean lastState = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int scrolled = recyclerView.computeVerticalScrollOffset();
                setAlpha(scrolled);

                boolean isTaller = scrolled > height;
                boolean isChanged = lastState != isTaller;

                if (!isChanged)
                    return;

                lastState = isTaller;
                if (isPreLollipop)
                    toolbarShadow.setVisibility(isTaller ? View.VISIBLE : View.INVISIBLE);

                ViewCompat.setElevation(appBar, ViewUtils.px(MainActivity.this, isTaller ? 4 : 0));
            }

            void setAlpha(int scrolled) {
                if (banners[0] == null)
                    banners[0] = findViewById(R.id.banners);

                banners[0].setAlpha((float) (height - scrolled) / height);
            }
        });
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
