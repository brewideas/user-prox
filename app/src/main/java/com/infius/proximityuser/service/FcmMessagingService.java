package com.infius.proximityuser.service;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.infius.proximityuser.ProximityApplication;
import com.infius.proximityuser.utilities.AppConstants;
import com.infius.proximityuser.utilities.Utils;

public class FcmMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Utils.saveString(ProximityApplication.getInstance().getApplicationContext(), AppConstants.FCM_TOKEN, s);
        Log.d(TAG, "fcm token: " + s);
    }

}
