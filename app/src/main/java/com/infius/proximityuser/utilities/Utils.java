package com.infius.proximityuser.utilities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;

import com.android.volley.VolleyError;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.infius.proximityuser.ProximityApplication;
import com.infius.proximityuser.R;
import com.infius.proximityuser.activities.AddGuestActivity;
import com.infius.proximityuser.activities.AuthActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 56;
    private static final int REQUEST_CODE_READ_CONTACT = 57;

    public static boolean isValidPhoneNo(String phoneNo) {
        Pattern p = Pattern.compile(AppConstants.MOB_NO_REG_EX);
        Matcher m = p.matcher(phoneNo);
        if (m.find()) {
            return true;
        }
        return false;
    }

    public static boolean isValidEmail(String email) {
        Pattern p = Pattern.compile(AppConstants.EMAIL_REG_EX);
        Matcher m = p.matcher(email);
        if (m.find()) {
            return true;
        }
        return false;
    }

    public static Dialog getProgressDialog(Activity activity) {

        try {
            if (activity != null) {
                Dialog progressDialog = new Dialog(activity);
                progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                progressDialog.setCancelable(false);
                progressDialog.setContentView(R.layout.lyt_progress_bar);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                return progressDialog;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Bitmap generateQR(String inputData, int qrWidth, int qrHeight) throws Exception {
        Map hints;
        hints = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        if (!TextUtils.isEmpty(inputData)) {
            BitMatrix result;
            try {
                result = new MultiFormatWriter().encode(inputData, BarcodeFormat.QR_CODE, qrWidth, qrHeight, hints);
            } catch (IllegalArgumentException iae) {
                // Unsupported format
                return null;
            }

            int width = result.getWidth();
            int height = result.getHeight();
            int[] pixels = new int[width * height];

            for (int y = 0; y < height; y++) {
                int offset = y * width;
                // if (y < ymin || y > ymax) {
                for (int x = 0; x < width; x++) {
                    pixels[offset + x] = result.get(x, y) ? AppConstants.QR_KEY_BLACK : AppConstants.QR_KEY_WHITE;
                }

            }

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } else {
            return null;
        }
    }

    public static int convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = (int) (dp * (metrics.densityDpi / 160f));
        return px;
    }

    public static boolean isVersionMarshmallowAndAbove() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
    }

    public static void requestWriteExternalPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
    }

    public static void requestReadContactsPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_READ_CONTACT);

    }

    public static boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if (grantResults.length < 1) {
            return false;
        }
        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static String removeSpaceAndBracket(String phoneNo) {
        if (!TextUtils.isEmpty(phoneNo)) {
            phoneNo = phoneNo.trim();
            phoneNo = phoneNo.replaceAll("[^\\d+]", "");
            phoneNo = phoneNo.replaceAll(" ", "");
            return phoneNo;
        } else {
            return "";
        }
    }

    public static String filterMobileNumber(Activity activity, String phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            phoneNumber = phoneNumber.trim();
            int startIndex = AppConstants.START_INDEX_ZERO;
            phoneNumber = phoneNumber.replaceAll("[^\\d+]", "");
            if (phoneNumber.startsWith(activity.getString(R.string.mobile_number_prefix_91))) {
                startIndex = AppConstants.START_INDEX_THREE;
            } else if (phoneNumber.startsWith(activity.getString(R.string.mobile_number_prefix_91_without_plus))) {
                startIndex = AppConstants.START_INDEX_TWO;
            } else if (phoneNumber.startsWith(activity.getString(R.string.mobile_number_prefix_0))) {
                startIndex = AppConstants.START_INDEX_ONE;
            }
            phoneNumber = phoneNumber
                    .substring(startIndex, phoneNumber.length()).trim()
                    .replaceAll(" ", "");
            return phoneNumber;
        } else {
            return "";
        }
    }

    private static final int MIN_DISK_CACHE_SIZE = 30 * 1024 * 1024; // 30 MB
    private static final int MAX_DISK_CACHE_SIZE = 100 * 1024 * 1024; // 100 MB

    public static boolean isOnBoardingCompleted(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(
                AppConstants.COMPLETED_ONBOARDING_PREF_NAME, true);
    }

    public static boolean isUserLoggedIn(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(AppConstants.SP_IS_LOGGEDIN, false);
    }

    public static void saveString(Context context, String key, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString(key, value).commit();
    }

    public static String readString(Context context, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(key, "");
    }


    public static long diskCacheSizeBytes(File dir, long minSize) {
        long size = minSize;
        try {
            StatFs statFs = new StatFs(dir.getAbsolutePath());
            long availableBytes = ((long) statFs.getBlockCount()) * statFs.getBlockSize();
            size = availableBytes / 50;
        } catch (IllegalArgumentException e) {
            Log.d("DeviceUtils", "Unable to calculate 2% of available disk space, defaulting to minimum");
        }

        // Bound inside min/max size for disk cache.
        return Math.max(Math.min(size, MAX_DISK_CACHE_SIZE), MIN_DISK_CACHE_SIZE);
    }

    public static int getOSVersion() {
        return Build.VERSION.SDK_INT;
    }

    public static void login(AppCompatActivity activity) {
        Intent intent = new Intent(activity, AuthActivity.class);
        activity.startActivityForResult(intent, AppConstants.REQUEST_CODE_LOGIN);
    }

    public static int getScreenHeight(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        return height;
    }

    public static String getTimeStamp(String dateInput, String timeInput) {

        String inputPattern = "dd/MM/yyyy";
        String outputPattern = "yyyy-MM-dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(dateInput);
            str = outputFormat.format(date) + "T" + timeInput + ".000";
//            str = outputFormat.format(date) + "T" + "04:24"+".000";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String getBase64Image(Context context, Uri selectedImage) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        // Get the cursor
        Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        // Move to first row
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String imgDecodableString = cursor.getString(columnIndex);
        cursor.close();
//        Bitmap bitmap =  BitmapFactory.decodeFile(imgDecodableString);

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), selectedImage);
            return getBase64Image(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String getBase64Image(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] byteArrayImage = baos.toByteArray();
        String encodedImage = Base64.encodeToString(byteArrayImage, Base64.NO_WRAP);
        return encodedImage;
    }

    public static void logout(Activity activity) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        prefs.edit().putBoolean(AppConstants.SP_IS_LOGGEDIN, false).commit();
        saveString(activity, AppConstants.KEY_TOKEN, "");

        Intent intent = new Intent(activity, AuthActivity.class);
        activity.startActivityForResult(intent, AppConstants.REQUEST_CODE_LOGIN);
    }

    public static String getErrorMessage(VolleyError error) {
        String message = "Something went wrong";
        if (error != null && error.networkResponse != null && error.networkResponse.data != null) {
            try {
                message = new String(error.networkResponse.data, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return message;
    }

    public static void handleSesionExpire(final Activity activity, String message) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(activity, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(activity);
        }
        builder.setTitle(activity.getString(R.string.session_expire))
                .setMessage(message)
                .setPositiveButton(R.string.login, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        logout(activity);
                        activity.finish();
                    }
                })
                .setNegativeButton(R.string.home, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public static void showAlertDialog(Activity activity, String title, String message) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(activity, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(activity);
        }
        builder.setTitle(title)
                .setMessage(message)
                .setNeutralButton(activity.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}
