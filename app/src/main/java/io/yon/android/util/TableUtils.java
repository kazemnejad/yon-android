package io.yon.android.util;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import java.util.List;

import io.yon.android.R;
import io.yon.android.model.Map;
import io.yon.android.model.Table;

/**
 * Created by amirhosein on 9/3/2017 AD.
 */

public class TableUtils {

    public static void setSelectedTable(TextView btnShowSelectedTable, Table selectedTable) {
        Context context = btnShowSelectedTable.getContext();
        if (selectedTable != null) {
            btnShowSelectedTable.setClickable(true);
            btnShowSelectedTable.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            btnShowSelectedTable.setText(context.getString(R.string.show_table_with_name, selectedTable.getName()));
        } else {
            btnShowSelectedTable.setClickable(false);
            btnShowSelectedTable.setTextColor(ContextCompat.getColor(context, R.color.black_54));
            btnShowSelectedTable.setText(R.string.no_selected_table);
        }
    }

    public static Map findMapByTable(Table target, List<Map> maps) {
        for (Map map : maps)
            for (Table table : map.getTables())
                if (target.getId().equals(table.getId()))
                    return map;

        return maps.size() > 0 ? maps.get(0) : null;
    }
}
