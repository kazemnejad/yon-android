package io.yon.android.utils;

import android.content.Context;

/**
 * Created by amirhosein on 6/1/17.
 */

public class ViewUtils {
    public static int px(Context context, int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    public static int dp(Context context, int px) {
        return (int) (px / context.getResources().getDisplayMetrics().density);
    }
}
