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
import io.yon.android.model.Banner;
import io.yon.android.util.RxBus;
import io.yon.android.view.adapter.VaryingShowcaseAdapter;
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnScrollListener(new ShowcaseOnScrollListener(this) {
            @Override
            protected View findViewById(int id) {
                return MainActivity.this.findViewById(id);
            }
        });
    }

    private void fillDummyContent() {
        ArrayList<Object> data = new ArrayList<>();
        data.add(makeBanners());
        data.add("sss");

        recyclerView.setAdapter(new VaryingShowcaseAdapter(data, new RxBus()));
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
