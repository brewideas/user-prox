package com.infius.proximityuser.utilities;

import android.content.Context;
import android.preference.PreferenceManager;

import com.infius.proximityuser.activities.HomeActivity;


public class ProfileUtils {
    public static String getUserName(Context context) {
        return "Kshemendra";
    }

    public static String getPropertyName(Context context) {
        return "Q-803";
    }

    public static String getOwnershipType(Context context) {
        return "Tanent";
    }

    public static String getProfileImageUrl(Context context) {
        String url = "mock";
        url = PreferenceManager.getDefaultSharedPreferences(context).getString(AppConstants.SP_PROFILE_PIC_URL, "mock");
        return url;
    }
}
