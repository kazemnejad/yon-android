package io.yon.android.view.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.orhanobut.logger.Logger;

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

        addTables();
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

            if (isTablesClickable) {
                View view = makeSelectableBackground(table);
                view.setTranslationX(marginLeft + getPosition(unitSize, table.getX(), tableWidth));
                view.setTranslationY(marginTop + getPosition(unitSize, table.getY(), tableHeight));
                view.setTag(new Object[]{table, iv});

                addView(view, params);
            }
        }

        reCalculateSize(Math.max((int) (unitSize * mapHeight), viewHeight));
    }

    private ImageView makeImageView(Table table) {
        ImageView iv = new ImageView(getContext());
        iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
        iv.setImageResource(TableConstants.resources(table.getShape()));
        iv.setRotation(table.getAngle());

        return iv;
    }

    private View makeSelectableBackground(Table table) {
        View view = new View(getContext());
        view.setClickable(true);
        view.setOnClickListener(this);
        view.setBackgroundResource(R.drawable.selectable_item_background);
        view.setRotation(table.getAngle());

        return view;
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
            onTableClickListener.onClick((ImageView) tag[1], (Table) tag[0]);
        } catch (Exception exp) {
            Logger.e(exp, "Unable to cast tag to table");
        }
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
//                return resources[shape];
                return R.color.solidPlaceHolder;
            } catch (Exception exp) {
                return R.color.solidPlaceHolder;
            }
        }
    }

    public interface OnTableClickListener {
        void onClick(View v, Table table);
    }
}
