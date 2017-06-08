package io.yon.android;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

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
        Logger.addLogAdapter(new AndroidLogAdapter());
    }
}
