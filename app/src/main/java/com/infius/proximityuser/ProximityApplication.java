package com.infius.proximityuser;

import android.app.Application;
import android.os.AsyncTask;

import com.infius.proximityuser.network.VolleyManager;

public class ProximityApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                VolleyManager.init(ProximityApplication.this);
            }
        });
    }
}
