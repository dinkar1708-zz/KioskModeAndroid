package kiosk.mode_single.purpose.app.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;

/**
 * requires REORDER_TASKS and SYSTEM_ALERT_WINDOW permissions
 * requires launcher app set as LAUNCHER,DEFAULT and HOME in manifest
 */
public class KioskMode {
    private static final String TAG = KioskMode.class.getSimpleName();

    private static KioskMode kioskMode;
    private WindowManager windowManager;
    private CustomViewGroup interceptView;

    /**
     * prevent creating object multiple time
     */
    private KioskMode() {
    }

    /**
     * get object
     *
     * @return object of this class
     */
    public static KioskMode getKioskMode() {
        if (kioskMode == null) {
            kioskMode = new KioskMode();
        }
        return kioskMode;
    }

    /**
     * get locked status from db
     *
     * @param context activity context
     * @return device is locked or not
     */
    public boolean isLocked(Context context) {
        return MySharedPreferences.isAppInKioskMode(context);
    }

    /**
     * lock and unlock the components and save the value
     *
     * @param context  activity context
     * @param isLocked device is locked or not
     */
    public void lockUnlock(Context context, boolean isLocked) {
        if (isLocked) {
            lockStatusBar(context);
        } else {
            unLockStatusBar();
        }
        MySharedPreferences.setKioskMode(context, isLocked);
    }

    /**
     * call this from onUserLeaveHint() from every activity
     * Ask that the task associated with a given task ID be moved to the
     * front of the stack, so it is now visible to the user.
     *
     * @param activity to move in front
     */
    public void moveTaskToFront(Activity activity) {
        boolean isLocked = isLocked(activity);
        if (!isLocked) {
            return;
        }
        ((ActivityManager) activity.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE))
                .moveTaskToFront(activity.getTaskId(), 1);
    }

    /**
     * lock status bar, add view in window manager
     *
     * @param context app context
     */
    private void lockStatusBar(Context context) {
        windowManager = ((WindowManager) context.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE));
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        params.gravity = Gravity.TOP;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                // this is to enable the notification to receive touch events
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                // Draws over status bar
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int result = 0;
        if (resId > 0) {
            result = context.getResources().getDimensionPixelSize(resId);
        }
        params.height = result;
        params.format = PixelFormat.TRANSPARENT;
        interceptView = new CustomViewGroup(context);
        try {
            windowManager.addView(interceptView, params);
        } catch (RuntimeException e) {
            //not require to log on file
            e.printStackTrace();
        }
    }

    /**
     * unlock the status bar, remove the view added in window manager
     */
    private void unLockStatusBar() {
        Log.i(TAG, "unLockStatusBar().....");
        if (windowManager == null || interceptView == null || (!interceptView.isShown())) {
            return;
        }
        try {
            windowManager.removeView(interceptView);
        } catch (RuntimeException e) {
            //not require to log on file
            e.printStackTrace();
        }
    }
}
