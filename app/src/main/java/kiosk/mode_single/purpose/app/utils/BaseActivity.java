package kiosk.mode_single.purpose.app.utils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

/**
 */

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();
    public KioskMode kioskMode;
    protected View mDecorView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        kioskMode = KioskMode.getKioskMode();
        mDecorView = getWindow().getDecorView();
        hideSystemUI();

    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        //move current activity to front if required
        Log.i(TAG, "onUserLeaveHint()...");
        kioskMode.moveTaskToFront(this);
    }

    private void hideSystemUI() {
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}
