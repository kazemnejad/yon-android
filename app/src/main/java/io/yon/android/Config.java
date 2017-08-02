package io.yon.android;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by amirhosein on 6/8/17.
 */

public class Config {
    private static final String MAIN = "main";
    private static final String USER = "user";
    private static final String DATA_CACHE = "cache";

    private static SharedPreferences get(Context context, String name) {
        return context.getApplicationContext().getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public static SharedPreferences get(Context context) {
        return get(context, MAIN);
    }

    public static SharedPreferences getCache(Context context) {
        return get(context, DATA_CACHE);
    }

    public static SharedPreferences getUser(Context context) {
        return get(context, USER);
    }

    public static abstract class Field {
        public final static String Token = "token";

        public final static String FirstName = "fname";
        public final static String LastName = "lname";
        public final static String Email = "email";
        public final static String Avatar = "avatar";

        public final static String ShowCase = "showcase";
    }
}
