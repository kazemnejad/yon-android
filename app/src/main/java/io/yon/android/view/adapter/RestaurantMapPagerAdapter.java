package io.yon.android.view.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;

import io.yon.android.R;
import io.yon.android.model.Map;
import io.yon.android.model.Table;
import io.yon.android.view.widget.RestaurantMapView;

/**
 * Created by amirhosein on 8/12/2017 AD.
 */

public class RestaurantMapPagerAdapter extends PagerAdapter {

    private List<Map> maps;
    private Context context;
    private HashMap<String, Boolean> forbiddenTables;
    private RestaurantMapView.OnTableClickListener listener;
    private Table selectedTable;

    public RestaurantMapPagerAdapter(Context context, List<Map> maps) {
        this.context = context;
        this.maps = maps;
    }

    public RestaurantMapPagerAdapter(Context context, List<Map> maps, HashMap<String, Boolean> forbiddenTables) {
        this(context, maps);
        this.forbiddenTables = forbiddenTables;
    }

    public RestaurantMapPagerAdapter(Context context, List<Map> maps, HashMap<String, Boolean> forbiddenTables, RestaurantMapView.OnTableClickListener listener) {
        this(context, maps, forbiddenTables);
        this.listener = listener;
    }

    public RestaurantMapPagerAdapter(Context context, List<Map> maps, HashMap<String, Boolean> forbiddenTables, RestaurantMapView.OnTableClickListener listener, Table selectedTable) {
        this(context, maps, forbiddenTables, listener);
        this.selectedTable = selectedTable;
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

        Map map = maps.get(position);
        RestaurantMapView mapView = (RestaurantMapView) inflater.inflate(R.layout.item_restaurantr_map, container, false);
        mapView.setOnTableClickListener(listener);
        mapView.setMap(map, forbiddenTables, selectedTable);

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
