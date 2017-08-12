package io.yon.android.view.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.yon.android.R;
import io.yon.android.model.Map;
import io.yon.android.util.ViewUtils;
import io.yon.android.view.widget.RestaurantMapView;

/**
 * Created by amirhosein on 8/12/2017 AD.
 */

public class RestaurantMapPagerAdapter extends PagerAdapter {

    private List<Map> maps;
    private Context context;

    public RestaurantMapPagerAdapter(Context context, List<Map> maps) {
        this.context = context;
        this.maps = maps;
    }

    @Override
    public int getCount() {
        return maps != null ? maps.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);

        RestaurantMapView mapView = (RestaurantMapView) inflater.inflate(R.layout.item_restaurantr_map, container, false);
        Map map = maps.get(position);

        mapView.setMap(map);
        container.addView(mapView);

        return mapView;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return maps.get(position).getName();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
