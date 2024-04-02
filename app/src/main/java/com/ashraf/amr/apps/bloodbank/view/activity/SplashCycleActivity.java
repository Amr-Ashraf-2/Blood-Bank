package com.ashraf.amr.apps.bloodbank.view.activity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.ashraf.amr.apps.bloodbank.R;
import com.ashraf.amr.apps.bloodbank.view.fragment.splashCycle.SplashFragment;

import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.replaceFragment;

public class SplashCycleActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        View decorView = getWindow().getDecorView();
// Hide both the navigation bar and the status bar.
// SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
// a general rule, you should design your app to hide the status bar whenever you
// hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        setContentView(R.layout.activity_splash_cycle);





        replaceFragment(getSupportFragmentManager(), R.id.frame, new SplashFragment());
    }
}
