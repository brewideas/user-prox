package com.infius.proximityuser.utilities;

import android.app.Activity;
import android.content.Context;
import android.preference.PreferenceManager;

import com.infius.proximityuser.activities.HomeActivity;
import com.infius.proximityuser.model.UserSessionDetails;


public class ProfileUtils {

    public static void saveProfileDetails(Activity activity, UserSessionDetails profileDetails) {
        Utils.saveString(activity, AppConstants.SP_NAME, profileDetails.getName());
        Utils.saveString(activity, AppConstants.SP_ROLE, profileDetails.getPrimaryRole());
        Utils.saveString(activity, AppConstants.SP_PROFILE_PIC_URL, profileDetails.getThumbnailUrl());
        Utils.saveString(activity, AppConstants.SP_EMAIL, profileDetails.getEmail());
        Utils.saveString(activity, AppConstants.SP_MOBILE, profileDetails.getMobile());
        Utils.saveString(activity, AppConstants.SP_SITE, profileDetails.getSite());
    }

    public static String getUserName(Context context) {
        return Utils.readString(context, AppConstants.SP_NAME);
    }

    public static String getPropertyName(Context context) {
        return Utils.readString(context, AppConstants.SP_SITE);
    }

    public static String getOwnershipType(Context context) {
        return Utils.readString(context, AppConstants.SP_ROLE);
    }

    public static String getMobile(Context context) {
        return Utils.readString(context, AppConstants.SP_MOBILE);
    }

    public static String getEmail(Context context) {
        return Utils.readString(context, AppConstants.SP_EMAIL);
    }

    public static String getProfileImageUrl(Context context) {
        return Utils.readString(context, AppConstants.SP_PROFILE_PIC_URL);
    }

}
