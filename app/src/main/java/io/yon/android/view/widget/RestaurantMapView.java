package io.yon.android.view.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.orhanobut.logger.Logger;

import java.util.HashMap;

import io.yon.android.R;
import io.yon.android.model.Map;
import io.yon.android.model.Table;
import io.yon.android.util.ViewUtils;

/**
 * Created by amirhosein on 8/6/17.
 */

public class RestaurantMapView extends FrameLayout implements View.OnClickListener {
    private Map map;
    private int maxUnitSize = 0;
    private boolean isTablesClickable = false;
    private OnTableClickListener onTableClickListener;
    private boolean isDrawQueued = false;
    private HashMap<String, Boolean> forbiddenTables;

    private View selectedTableView = null;
    private Table selectedTable;

    public RestaurantMapView(@NonNull Context context) {
        super(context);
        maxUnitSize = ViewUtils.px(context, 60);
    }

    public RestaurantMapView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        maxUnitSize = ViewUtils.px(context, 60);
    }

    public RestaurantMapView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        maxUnitSize = ViewUtils.px(context, 60);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RestaurantMapView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setTablesClickable(boolean enable) {
        this.isTablesClickable = enable;
    }

    public void setOnTableClickListener(OnTableClickListener listener) {
        onTableClickListener = listener;
        isTablesClickable = listener != null;
    }

    public void setMap(Map map) {
        this.map = map;
        if (map.getTables() == null)
            return;

        if (ViewCompat.isLaidOut(this))
            addTables();
        else {
            ViewTreeObserver vto = getViewTreeObserver();
            if (vto.isAlive())
                vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (getWidth() != 0) {
                            ViewUtils.removeOnGlobalLayoutListener(getViewTreeObserver(), this);
                            addTables();
                        }
                    }
                });
        }
    }

    public void setMap(Map map, HashMap<String, Boolean> forbiddenTables) {
        this.forbiddenTables = forbiddenTables;
        setMap(map);
    }

    public void setMap(Map map, HashMap<String, Boolean> forbiddenTables, Table selectedTable) {
        this.selectedTable = selectedTable;
        setMap(map, forbiddenTables);
    }

    private void addTables() {
        // viewWidth is in px
        int viewWidth = getWidth();
        int viewHeight = getHeight();

        // mapWidth/Height is in meter
        float mapWidth = map.getWidth();
        float mapHeight = map.getHeight();

        float k = 1.0f * viewWidth / mapWidth;
        float unitSize = Math.min(k, maxUnitSize);

        float horizontalResidualSpace = Math.max(viewWidth - unitSize * mapWidth, 0);
        int marginLeft = (int) (1f * horizontalResidualSpace / 2);

        float verticalResidualSpace = Math.max(viewHeight - unitSize * mapHeight, 0);
        int marginTop = (int) (1f * verticalResidualSpace / 2);

        for (Table table : map.getTables()) {
            float tableWidth = TableConstants.width(table.getShape());
            float tableHeight = TableConstants.height(table.getShape());

            LayoutParams params = new LayoutParams(
                    (int) (unitSize * tableWidth),
                    (int) (unitSize * tableHeight)
            );

            ImageView iv = makeImageView(table);
            iv.setTranslationX(marginLeft + getPosition(unitSize, table.getX(), tableWidth));
            iv.setTranslationY(marginTop + getPosition(unitSize, table.getY(), tableHeight));

            addView(iv, params);

            if (isTablesClickable && isTableAvailable(table)) {
                float maskSize = Math.max(tableWidth, tableHeight);
                LayoutParams selectableParams = new LayoutParams(
                        (int) (unitSize * maskSize),
                        (int) (unitSize * maskSize)
                );

                View view = makeSelectableBackground(table);
                view.setTranslationX(marginLeft + getPosition(unitSize, table.getX(), maskSize));
                view.setTranslationY(marginTop + getPosition(unitSize, table.getY(), maskSize));
                view.setTag(new Object[]{table, iv});

                addView(view, selectableParams);

                if (selectedTable != null && selectedTableView == null && selectedTable.getId().equals(table.getId()))
                    addMaskOnTopOf(view, table.getAngle());
            }
        }

        reCalculateSize(Math.max((int) (unitSize * mapHeight), viewHeight));
    }

    private ImageView makeImageView(Table table) {
        ImageView iv = new ImageView(getContext());
        iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
        iv.setImageResource(TableConstants.resources(table.getShape()));
        iv.setRotation(table.getAngle());

        if (!isTableAvailable(table))
            iv.setAlpha(0.5f);

        return iv;
    }

    private View makeSelectableBackground(Table table) {
        View view = new View(getContext());
        view.setClickable(true);
        view.setOnClickListener(this);
        view.setBackgroundResource(R.drawable.round_button_light);
        view.setRotation(table.getAngle());
        view.setId(table.getId().hashCode());

        return view;
    }

    private boolean isTableAvailable(Table table) {
        return this.forbiddenTables == null || !this.forbiddenTables.containsKey(table.getId());
    }

    private int getPosition(float unitSize, float pos, float length) {
        return (int) (unitSize * (pos - length / 2));
    }

    private void reCalculateSize(int contentHeight) {
        ViewGroup.LayoutParams rawParams = getLayoutParams();
        if (rawParams instanceof LinearLayout.LayoutParams) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rawParams;
            LinearLayout.LayoutParams finalParams = new LinearLayout.LayoutParams(
                    params.width,
                    contentHeight
            );

            finalParams.setMargins(params.leftMargin, params.topMargin, params.rightMargin, params.bottomMargin);
            finalParams.gravity = params.gravity;
            setLayoutParams(finalParams);
        } else if (rawParams instanceof RelativeLayout.LayoutParams) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rawParams;
            RelativeLayout.LayoutParams finalParams = new RelativeLayout.LayoutParams(
                    params.width,
                    contentHeight
            );

            finalParams.setMargins(params.leftMargin, params.topMargin, params.rightMargin, params.bottomMargin);
            setLayoutParams(finalParams);
        } else if (rawParams instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) rawParams;
            FrameLayout.LayoutParams finalParams = new FrameLayout.LayoutParams(
                    params.width,
                    contentHeight
            );

            finalParams.setMargins(params.leftMargin, params.topMargin, params.rightMargin, params.bottomMargin);
            finalParams.gravity = params.gravity;
            setLayoutParams(finalParams);
        }
    }

    @Override
    public void onClick(View v) {
        if (onTableClickListener == null)
            return;

        try {
            Object[] tag = (Object[]) v.getTag();
            onTableClickListener.onClick(this, (ImageView) tag[1], (Table) tag[0]);
        } catch (Exception exp) {
            Logger.e(exp, "Unable to cast tag to table");
        }
    }

    public void setTableSelected(Table table) {
        selectedTable = table;

        if (!ViewCompat.isLaidOut(this))
            return;

        View view = findViewByTable(table);
        if (view != null)
            addMaskOnTopOf(view, table.getAngle());
    }

    private void addMaskOnTopOf(View view, float angle) {
        View mask = new View(getContext());
        mask.setLayoutParams(view.getLayoutParams());
        mask.setBackgroundResource(R.drawable.selected_table_background);
        mask.setRotation(angle);
        mask.setTranslationY(view.getTranslationY());
        mask.setTranslationX(view.getTranslationX());
        mask.setAlpha(0f);
        addView(mask);

        mask.animate()
                .alpha(1f)
                .setDuration(100)
                .setInterpolator(new AccelerateDecelerateInterpolator());

        selectedTableView = mask;
    }

    public void removeTableSelection() {
        if (selectedTableView != null) {
            selectedTableView.animate()
                    .alpha(0f)
                    .setDuration(100)
                    .setInterpolator(new AccelerateDecelerateInterpolator());
            removeView(selectedTableView);
        }
    }

    private View findViewByTable(Table table) {
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            try {
                Object[] tag = (Object[]) v.getTag();
                Table viewTable = (Table) tag[0];
                if (viewTable.getId().equals(table.getId()))
                    return v;
            } catch (Exception ignored) {
            }
        }

        return null;
    }

    private static class TableConstants {
        private static final float[] widths = {
                0.75f,
                1f,
                1.4f,
                0.75f,
                1,
                1.25f,
        };

        private static final float[] heights = {
                0.58f,
                1f,
                1f,
                0.58f,
                1,
                1.25f
        };

        public static final int[] resources = {
                R.drawable.table_square_2,
                R.drawable.table_square_4,
                R.drawable.table_square_6,
                R.drawable.table_circle_2,
                R.drawable.table_circle_4,
                R.drawable.table_circle_8,
        };

        public static float height(int shape) {
            try {
                return heights[shape];
            } catch (Exception exp) {
                return 1;
            }
        }

        public static float width(int shape) {
            try {
                return widths[shape];
            } catch (Exception exp) {
                return 1;
            }
        }

        public static int resources(int shape) {
            try {
                return resources[shape];
//                return R.color.solidPlaceHolder;
            } catch (Exception exp) {
                return R.color.solidPlaceHolder;
            }
        }
    }

    public interface OnTableClickListener {
        void onClick(RestaurantMapView mapView, View v, Table table);
    }
}
