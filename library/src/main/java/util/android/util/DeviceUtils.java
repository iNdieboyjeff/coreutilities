package util.android.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * Created by jeffsutton on 20/07/15.
 */
public class DeviceUtils {
    public static boolean isKindleFire() {
        return Build.MANUFACTURER.equals("Amazon")
                && (Build.MODEL.equals("Kindle Fire") || Build.MODEL.startsWith("KF")
                || Build.MODEL.startsWith("AFT") || Build.MODEL.startsWith("SD4930UR"));
    }

    public static boolean isGoogleTV(Context context) {
        final PackageManager pm = context.getPackageManager();
        return pm.hasSystemFeature("com.google.android.tv");
    }

    public static boolean isAndroidTV(Context context) {
        if (isFireTV(context) || isLycaTVBox(context)) {
            return true;
        }
        // Check if the telephony hardware feature is available.
        if (context.getPackageManager().hasSystemFeature("android.hardware.telephony")) {
            return false;
            // Check if android.hardware.touchscreen feature is available.
        } else if (context.getPackageManager().hasSystemFeature("android.hardware.touchscreen")) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isFireTV(Context context) {
        return Build.MANUFACTURER.equals("Amazon")
                && Build.MODEL.startsWith("AFT");
    }

    public static boolean isFireTVBox(Context context) {
        return Build.MANUFACTURER.equals("Amazon")
                && Build.MODEL.equals("AFTB");
    }

    public static boolean isFireTVStick(Context context) {
        return Build.MANUFACTURER.equals("Amazon")
                && Build.MODEL.equals("AFTM");
    }

    public static boolean isFirePhone(Context context) {
        return Build.MANUFACTURER.equals("Amazon")
                && Build.MODEL.startsWith("SD4930UR");
    }

    public static boolean isKindleTablet() {
        return Build.MANUFACTURER.equals("Amazon")
                && (Build.MODEL.equals("Kindle Fire") || Build.MODEL.startsWith("KF"));
    }

    public static boolean isLycaTVBox(Context context) {
        return Build.MODEL.equalsIgnoreCase("eztv3") || Build.MODEL.startsWith("LycaTV");
    }

    public static boolean isBlackberry() {
        if (Build.BRAND.toLowerCase().contains("blackberry")
                || System.getProperty("os.name").equals("qnx")) {
            return true;
        }
        return false;
    }
}
