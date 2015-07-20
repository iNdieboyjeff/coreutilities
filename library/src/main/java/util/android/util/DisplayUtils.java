package util.android.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by jeffsutton on 20/07/15.
 */
public class DisplayUtils {

    /**
     * Convert a pixel value to device pixels.
     *
     * @param context
     * @param px
     * @return int
     */
    public static int pxToDp(Context context, int px) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, context.getResources()
                .getDisplayMetrics());
    }

    public static int convertDpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);

    }

    public static int convertPxToDp(Context context, int px) {
        return pxToDp(context, px);
    }

    public static float getSmallestWidth(Context activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(metrics);

        int widthInPixels = metrics.widthPixels;
        int heightInPixels = metrics.heightPixels;

        float scaleFactor = metrics.density;

        float widthDp = widthInPixels / scaleFactor;
        float heightDp = heightInPixels / scaleFactor;

        float smallestWidth = Math.min(widthDp, heightDp);
//        Log.i("AndroidUtil", "Smallest width: " + smallestWidth);
        return smallestWidth;
    }

    /**
     * <p>
     * Force an activity to be fixed in portrait orientation
     * </p>
     *
     * @param activity
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static void forceLandscape(Activity activity) {
        if (AndroidUtil.getAndroidVersion() < Build.VERSION_CODES.GINGERBREAD) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }
    }

    /**
     * <p>
     * Force an activity to be fixed in landscape orientation
     * </p>
     *
     * @param activity
     */
    public static void forcePortrait(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * <p>
     * Allow activity to use sensor to determine own orientation
     * </p>
     *
     * @param activity
     */
    public static void forceSensor(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }
}
