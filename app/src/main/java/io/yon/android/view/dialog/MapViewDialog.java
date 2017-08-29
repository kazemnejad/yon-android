package io.yon.android.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import io.yon.android.R;
import io.yon.android.model.Map;
import io.yon.android.model.Table;
import io.yon.android.util.ViewUtils;
import io.yon.android.view.widget.RestaurantMapView;

/**
 * Created by amirhosein on 8/23/2017 AD.
 */

public class MapViewDialog extends AppCompatDialog {

    private Map map;
    private Table selectedTable;

    public MapViewDialog(Context context, Map map, Table selectedTable) {
        super(context, R.style.Dialog);
        this.map = map;
        this.selectedTable = selectedTable;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_map_view);

        RestaurantMapView mapView = (RestaurantMapView) findViewById(R.id.map_view);
        mapView.setMap(map, null, selectedTable);
        mapView.setOnTableClickListener((s, v, d) -> {
        });

        Button close = (Button) findViewById(R.id.btn_close);
        close.setOnClickListener(v -> cancel());

        View clickDisabler = findViewById(R.id.click_disabler);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) clickDisabler.getLayoutParams();
        params.height = (int) getMapViewHeight(map);
        clickDisabler.setLayoutParams(params);
    }

    private float getMapViewHeight(Map map) {
        int viewWidth = ViewUtils.getScreenWidth(getContext());

        float k = 1.0f * viewWidth / map.getWidth();
        float unitSize = Math.min(k, ViewUtils.px(getContext(), 60));

        return unitSize * map.getHeight();
    }
}
