package io.yon.android;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import io.yon.android.api.WebService;
import io.yon.android.util.AuthHelper;

/**
 * Created by amirhosein on 5/27/17.
 */

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initialize();
    }

    private void initialize(){
        // init Logger class with Android Adapter
        Logger.addLogAdapter(new AndroidLogAdapter());

        // init WebService
        WebService.init(AuthHelper.getTokenNoException(getApplicationContext()));
    }
}
