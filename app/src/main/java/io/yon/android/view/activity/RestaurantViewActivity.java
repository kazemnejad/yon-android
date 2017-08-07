package io.yon.android.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import io.yon.android.R;
import io.yon.android.model.Map;
import io.yon.android.model.Table;
import io.yon.android.view.widget.RestaurantMapView;

/**
 * Created by amirhosein on 8/6/17.
 */

public class RestaurantViewActivity extends Activity {

    private RestaurantMapView mapView;

    public static void start(Context context) {
        context.startActivity(new Intent(context, RestaurantViewActivity.class));
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_restaurant_view;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setDisplayHomeAsUpEnabled(true);

        mapView = (RestaurantMapView) findViewById(R.id.map);
        mapView.setOnTableClickListener((v, table) -> Logger.d(table.getX()));
        mapView.setMap(createMap());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private Map createMap() {
        Map m = new Map();
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

    private Table makeTable(float x, float y) {
        Table t = new Table();
        t.setX(x);
        t.setY(y);
        t.setShape(1);
//        t.setAngle(45f);

        return t;
    }
}
