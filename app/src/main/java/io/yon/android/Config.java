package io.yon.android;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by amirhosein on 6/8/17.
 */

public class Config {
    private static final String MAIN = "main";

    private static SharedPreferences get(Context context, String name) {
        return context.getApplicationContext().getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public static SharedPreferences get(Context context) {
        return get(context, MAIN);
    }

    public static abstract class Field {
        public final static String Token = "token";
    }
}
