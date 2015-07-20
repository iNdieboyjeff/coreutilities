/**
 * Copyright � 2013 Jeff Sutton.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package util.android.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewConfiguration;
import android.widget.TextView;

import java.util.Locale;

public final class AndroidUtil {

    /**
     * Android 4.2. Constant Value: 17 (0x00000010)
     */
    public static final int ANDROID_VERSION_CODE_JELLY_BEAN_MR2 = 0x00000011;

    /**
     * June 2012: Android 4.1. Constant Value: 16 (0x00000010)
     */
    public static final int ANDROID_VERSION_CODE_JELLY_BEAN = 0x00000010;

    /**
     * October 2011: Android 4.0. Constant Value: 14 (0x0000000e)
     */
    public static final int ANDROID_VERSION_CODE_ICS = 0x0000000e;

    /**
     * February 2011: Android 3.0. Constant Value: 11 (0x0000000b)
     */
    public static final int ANDROID_VERSION_CODE_HONEYCOMB = 0x0000000b;

    /**
     * November 2010: Android 2.3 Constant Value: 9 (0x00000009)
     */
    public static final int ANDROID_VERSION_CODE_GINGERBREAD = 0x00000009;

    public static final String VERSION_NAME = "1.5";



    /**
     * <p>
     * Generate a suitable user agent string for the current context. App name and version will be taken from the
     * manifest.
     * </p>
     *
     * <p>
     * User agent string will take the form:
     * <code>{app-name}/{version} (Linux; U; Android {version}; {locale};
     * {device-model}; {screen-type};)</code>
     * </p>
     *
     * @param context
     * @return String
     */
    public static final String generateUserAgentString(Context context) {
        return generateUserAgentString(getAppName(context), getAppVersion(context), context);
    }

    @SuppressLint("NewApi")
    public static boolean hasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            hasNavigationBar = !ViewConfiguration.get(context).hasPermanentMenuKey();
        } else {
            hasNavigationBar = false;
        }

        hasNavigationBar = hasNavigationBar && !isKindleFire();
        Log.i("AndroidUtil", "hasNavigationBar(" + hasNavigationBar + ")");
        return hasNavigationBar;
    }



    /**
     * <p>
     * Generate a suitable user agent string for the current context. App name and version will be taken from supplied
     * arguments.
     * </p>
     *
     * <p>
     * User agent string will take the form:
     * <code>{app-name}/{version} (Linux; U; Android {version}; {locale};
     * {device-model}; {screen-type};)</code>
     * </p>
     *
     * @param app
     *            - app name to display
     * @param version
     *            - app version code to display
     * @return String - UserAgent string
     */
    public static final String generateUserAgentString(String app, String version, Context context) {
        String UA = app + "/" + version + " (Linux; U; Android " + Build.VERSION.RELEASE + "; "
                + Locale.getDefault().getLanguage() + "-" + Locale.getDefault().getCountry() + "; " + Build.MODEL
                + "; " + getDeviceTypeID(context) + ";)";
        return UA;
    }

    /**
     * Return the current Android SDK version number.
     *
     * @return int
     */
    public static int getAndroidVersion() {
        return Build.VERSION.SDK_INT;
    }

    public static boolean isNexusDevice() {
        Log.i("Android Device", Build.MODEL);
        if (Build.MODEL.toLowerCase().contains("nexus"))
            return true;
        return false;
    }

    /**
     * Get the name of this app as specified in manifest.
     *
     * @param context
     * @return String
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            String appname = packageManager.getApplicationLabel(context.getApplicationInfo()).toString();
            return appname;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Unknown";
    }

    /**
     * Get the version of this app as specified in the manifest.
     *
     * @param context
     * @return String
     */
    public static String getAppVersion(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            String version = packageManager.getPackageInfo(context.getPackageName(), 0).versionName;
            if (version == null)
                version = Integer.toString(packageManager.getPackageInfo(context.getPackageName(), 0).versionCode);
            if (version != null)
                return version;
        } catch (Exception e) {

        }
        return "Unknown";
    }

    /**
     * Generate a String identifying current device as either Mobile, 7" Tablet or 10" Tablet.
     *
     * @param context
     * @return String
     */
    public static String getDeviceTypeID(Context context) {
        if (DeviceUtils.isGoogleTV(context) || DeviceUtils.isAndroidTV(context) || DeviceUtils.isFireTV(context)) {
            return "TV";
        }
        double size = tabletSize(context);
        if (size < 7) {
            return "Mobile";
        } else if (size >= 6 && size < 9) {
            return "7\" Tablet";
        } else if (size >= 9) {
            return "10\" Tablet";
        } else {
            return "Unknown";
        }
    }

    public static String getResourceString(Context context, int string) {
        return context.getResources().getString(string);
    }

    @Deprecated
    public static float getSmallestWidth(Context activity) {
        return DisplayUtils.getSmallestWidth(activity);
    }

    public static boolean isMyServiceRunning(Context c, String name) {
        ActivityManager manager = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (name.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * <p>
     * Determine if this device is a tablet.
     * </p>
     *
     * <p>
     * A device is considered to be a tablet if it's screen size is larger than 9".
     * </p>
     *
     * @param context
     * @return boolean
     * @deprecated - Use {@link #getSmallestWidth} instead
     * @see #getSmallestWidth
     */
    public static boolean isTablet(Context context) {

        return tabletSize(context) > 6.8;
    }

    public static void openActivity(Context context, Class<?> activity) {
        openActivity(context, activity, null);
    }

    public static void openActivity(Context context, Class<?> activity, Bundle b) {
        Intent intent = new Intent(context, activity);
        if (b != null)
            intent.putExtras(b);

        context.startActivity(intent);
    }

    public static void openActivity(Context context, Class<?> activity, Bundle b, int flags) {
        Intent intent = new Intent(context, activity);
        intent.addFlags(flags);
        if (b != null)
            intent.putExtras(b);
        context.startActivity(intent);
    }

    public static void openActivityForResult(Activity context, Class<?> activity, Bundle b, int requestCode) {
        Intent intent = new Intent(context, activity);
        if (b != null)
            intent.putExtras(b);
        context.startActivityForResult(intent, requestCode);
    }

    /*
     * ACTIVITY
     */
    public static void openBrowser(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(intent);
    }




    /*
     * UI & WIDGETS
     */
    public static void setStrikeThrough(TextView tv) {
        tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    public static boolean supportsGingerbread() {
        if (getAndroidVersion() >= ANDROID_VERSION_CODE_GINGERBREAD) {
            return true;
        }
        return false;
    }

    /*
     * SUPPORT & VERSION
     */
    public static boolean supportsHoneycomb() {
        if (getAndroidVersion() >= ANDROID_VERSION_CODE_HONEYCOMB) {
            return true;
        }
        return false;
    }

    public static boolean supportsLegacyVideo() {
        Log.d("Build Model", Build.MODEL);
        if ((!AndroidUtil.supportsHoneycomb())
            // || Build.MANUFACTURER.equalsIgnoreCase("Amazon")
            // || Build.MODEL.equalsIgnoreCase("Kindle Fire")
                ) {
            return true;
        }
        return false;
    }


// ==== DEPRECATED METHODS BELOW THE LINE ===== MOSTLY MOVED TO OTHER CLASSES =========

    @Deprecated
    public static boolean isKindleFire() {
        return DeviceUtils.isKindleFire();
    }

    @Deprecated
    public static boolean isGoogleTV(Context context) {
        return DeviceUtils.isGoogleTV(context);
    }

    @Deprecated
    public static boolean isAndroidTV(Context context) {
        return DeviceUtils.isAndroidTV(context);
    }

    @Deprecated
    public static boolean isFireTV(Context context) {
        return DeviceUtils.isFireTV(context);
    }

    @Deprecated
    public static boolean isFireTVBox(Context context) {
        return DeviceUtils.isFireTVBox(context);
    }

    @Deprecated
    public static boolean isFireTVStick(Context context) {
        return DeviceUtils.isFireTVStick(context);
    }

    @Deprecated
    public static boolean isFirePhone(Context context) {
        return DeviceUtils.isFirePhone(context);
    }

    @Deprecated
    public static boolean isKindleTablet() {
        return DeviceUtils.isKindleTablet();
    }

    @Deprecated
    public static boolean isLycaTVBox(Context context) {
        return DeviceUtils.isLycaTVBox(context);
    }

    @Deprecated
    public static boolean isBlackberry() {
        return DeviceUtils.isBlackberry();
    }

    /**
     * Determine the size of screen for this device.
     *
     * @param context
     * @return double
     * @deprecated
     */
    @Deprecated
    public static double tabletSize(Context context) {

        double size = 0;
        try {
            // Compute screen size
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            float screenWidth = dm.widthPixels / dm.xdpi;
            float screenHeight = dm.heightPixels / dm.ydpi;
            size = Math.sqrt(Math.pow(screenWidth, 2) + Math.pow(screenHeight, 2));
        } catch (Throwable t) {

        }

        return size;

    }

    /**
     * Convert a pixel value to device pixels.
     *
     * @param context
     * @param px
     * @return int
     * @deprecated
     */
    @Deprecated
    public static int pxToDp(Context context, int px) {
        return DisplayUtils.pxToDp(context, px);
    }

    @Deprecated
    public static int convertDpToPx(Context context, int dp) {
        return DisplayUtils.convertDpToPx(context, dp);

    }

    @Deprecated
    public static int convertPxToDp(Context context, int px) {
        return DisplayUtils.convertPxToDp(context, px);
    }

    /**
     * <p>
     * Force an activity to be fixed in portrait orientation
     * </p>
     *
     * @param activity
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Deprecated
    public static void forceLandscape(Activity activity) {
        DisplayUtils.forceLandscape(activity);
    }

    /**
     * <p>
     * Force an activity to be fixed in landscape orientation
     * </p>
     *
     * @param activity
     */
    @Deprecated
    public static void forcePortrait(Activity activity) {
        DisplayUtils.forcePortrait(activity);
    }

    /**
     * <p>
     * Allow activity to use sensor to determine own orientation
     * </p>
     *
     * @param activity
     */
    @Deprecated
    public static void forceSensor(Activity activity) {
        DisplayUtils.forceSensor(activity);
    }
}
