package org.cordova.plugin.labs.kiosk;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.View;
import android.view.WindowManager;
import android.app.ActionBar;

import org.apache.cordova.*;
import android.widget.*;

import java.lang.Integer;
import java.util.Collections;
import java.util.Set;

public class KioskActivity extends CordovaActivity {

    public static volatile boolean running = false;
    public static volatile boolean kioskModeEnabled = false;
    public static volatile boolean inFullscreen = false;

    public static volatile Set<Integer> runningKeys = Collections.EMPTY_SET;

    protected void onStart() {
        super.onStart();
        System.out.println("KioskActivity started");
        running = true;
    }

    protected void onStop() {
        super.onStop();
        System.out.println("KioskActivity stopped");
        running = false;
    }

    public void onCreate(Bundle savedInstanceState) {
        System.out.println("KioskActivity paused");
        super.onCreate(savedInstanceState);
        super.init();

        if (running) {
            finish(); // prevent more instances of kiosk activity
        }

        loadUrl(launchUrl);

        if(inFullscreen) {
            // https://github.com/apache/cordova-plugin-statusbar/blob/master/src/android/StatusBar.java
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

            hideSystemUI();
            // Remember that you should never show the action bar if the
            // status bar is hidden, so hide that too if necessary.
            ActionBar actionBar = getActionBar();
            if (actionBar != null) actionBar.hide();
        }
    }

    private void hideSystemUI() {
        // https://github.com/hkalina/cordova-plugin-kiosk/issues/14
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && inFullscreen) {
            hideSystemUI();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        System.out.println("onKeyDown event: keyCode = " + event.getKeyCode());
        return !runningKeys.contains(event.getKeyCode()); // event not being propagated if not allowed
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(runningKeys.contains(event.getKeyCode())) {
            return super.dispatchKeyEvent(event);
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        ;  
    }

    @Override
    protected void onPause() {
        super.onPause();

        //if (kioskModeEnabled) {
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.moveTaskToFront(getTaskId(), 0);
        //}

    }
}