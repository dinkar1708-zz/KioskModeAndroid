# KioskModeAndroid
Overview
Simplest way to create a application in kiosk mode/ single purpose device application. i e. only the single application can be accessed from the device without any root or special permission.

Screen pinning on all version of android devices without root/any special permission.

Configuration-

Include files from this source code.

The following components of android device are disabled for this kiosk mode
Back button of device,   Home button of device,   recent task button of device and   Status bar of device

Basic crucks to achieve above -

*. Entry in manifiest -
```
<intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.HOME" />

```
*. Basics permissions
```
 <uses-permission android:name="android.permission.REORDER_TASKS" />
 <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
```
*. Ovirride back button
```
@Override
    public void onBackPressed() {
        if (!kioskMode.isLocked(this)) {
            super.onBackPressed();
        }
    }
```

*. override user leave hint method
```
@Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        //move current activity to front if required
        Log.i(TAG, "onUserLeaveHint()...");
        kioskMode.moveTaskToFront(this);
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
```
*. My kiosk mode singlton class to manage lock unlock -
```
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
```    

For detail configuration please visit-

http://dinkarcse.blogspot.in/2017/06/android-kiosk-modesingle-purpose-app.html

