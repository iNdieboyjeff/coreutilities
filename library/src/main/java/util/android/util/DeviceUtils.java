package util.android.util;

import android.app.UiModeManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;

/**
 * Created by jeffsutton on 20/07/15.
 */
public class DeviceUtils {
    public static final String FIRETV_DEVICE_MODEL = "AFTB";
    public static final String FIRETV_STICK_DEVICE_MODEL = "AFTM";
    public static final String FIREPHONE_DEVICE_MODEL = "SD4930UR";
    public static final String AMAZON = "Amazon";

    public static boolean isKindleFire() {
        return Build.MANUFACTURER.equals(AMAZON)
                && (Build.MODEL.equals("Kindle Fire") || Build.MODEL.startsWith("KF")
                || Build.MODEL.startsWith("AFT") || Build.MODEL.startsWith(FIREPHONE_DEVICE_MODEL));
    }

    public static boolean isGoogleTV(Context context) {
        final PackageManager pm = context.getPackageManager();
        return pm.hasSystemFeature("com.google.android.tv");
    }

    public static boolean isAndroidTV(Context context) {
        if (isFireTV(context) || isLycaTVBox(context) || isGoogleTV(context)) {
            return true;
        }

        UiModeManager uiModeManager = (UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
        if (uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION) {
            return true;
        } else {
            // Check if the telephony hardware feature is available.
            if (context.getPackageManager().hasSystemFeature("android.hardware.telephony")) {
                return false;
                // Check if android.hardware.touchscreen feature is available.
            } else return !context.getPackageManager().hasSystemFeature("android.hardware.touchscreen");
        }

    }

    public static boolean isFireTV(Context context) {
        return (Build.MODEL.equalsIgnoreCase(FIRETV_DEVICE_MODEL) ||
                Build.MODEL.equalsIgnoreCase(FIRETV_STICK_DEVICE_MODEL));
    }

    public static boolean isFireTVBox(Context context) {
        return Build.MANUFACTURER.equals(AMAZON)
                && Build.MODEL.equals(FIRETV_DEVICE_MODEL);
    }

    public static boolean isFireTVStick(Context context) {
        return Build.MANUFACTURER.equals(AMAZON)
                && Build.MODEL.equals(FIRETV_STICK_DEVICE_MODEL);
    }

    public static boolean isFirePhone(Context context) {
        return Build.MANUFACTURER.equals(AMAZON)
                && Build.MODEL.startsWith(FIREPHONE_DEVICE_MODEL);
    }

    public static boolean isKindleTablet() {
        return Build.MANUFACTURER.equals(AMAZON)
                && (Build.MODEL.equals("Kindle Fire") || Build.MODEL.startsWith("KF"));
    }

    public static boolean isLycaTVBox(Context context) {
        return Build.MODEL.equalsIgnoreCase("eztv3") || Build.MODEL.startsWith("LycaTV");
    }

    public static boolean isBlackberry() {
        return Build.BRAND.toLowerCase().contains("blackberry")
                || System.getProperty("os.name").equals("qnx");
    }

    public static boolean isNexusDevice() {
        return Build.MODEL.toLowerCase().contains("nexus");
    }

    public static String getDeviceTypeID(Context context) {
        if (DeviceUtils.isGoogleTV(context) || DeviceUtils.isAndroidTV(context) || DeviceUtils.isFireTV(context)) {
            return "TV";
        }
        double size = DisplayUtils.getSmallestWidth(context);
        if (size < 600) {
            return "Mobile";
        } else if (size >= 600 && size < 720) {
            return "7-inch Tablet";
        } else if (size >= 720) {
            return "10-inch Tablet";
        } else {
            return "Unknown";
        }
    }
}
