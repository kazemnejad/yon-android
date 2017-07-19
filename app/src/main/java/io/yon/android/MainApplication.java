package io.yon.android;

import android.app.Application;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import io.yon.android.api.WebService;
import io.yon.android.util.Auth;

/**
 * Created by amirhosein on 5/27/17.
 */

public class MainApplication extends Application {

    private OnSharedPreferenceChangeListener listener = (sharedPreferences, key) -> {
        if (Config.Field.Token.equals(key))
            WebService.init(Auth.getTokenNoException(MainApplication.this));
    };

    @Override
    public void onCreate() {
        super.onCreate();
        initialize();
    }

    private void initialize(){
        // init Logger class with Android Adapter
        Logger.addLogAdapter(new AndroidLogAdapter());

        // init WebService
        WebService.init(Auth.getTokenNoException(getApplicationContext()));

        // listen to token changes
        Config.get(this).registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Config.get(this).unregisterOnSharedPreferenceChangeListener(listener);
    }
}
