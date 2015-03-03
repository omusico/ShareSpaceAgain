package com.salilgokhale.sharespace3;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by salilgokhale on 03/03/15.
 */
public class GlobalApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Required - Initialize the Parse SDK
        Parse.initialize(this, getString(R.string.parse_app_id),
                getString(R.string.parse_client_key));

        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
    }
}
