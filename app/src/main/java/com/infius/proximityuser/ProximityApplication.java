package com.infius.proximityuser;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.infius.proximityuser.network.VolleyManager;

public class ProximityApplication extends Application {

    private static ProximityApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                VolleyManager.init(ProximityApplication.this);
            }
        });

        mInstance = this;
        Log.e("context", "" + (mInstance.getApplicationContext() == null));
    }

    public static synchronized ProximityApplication getInstance() {
        Log.e("context", "" + (mInstance.getApplicationContext() == null));
        return mInstance;
    }
}
