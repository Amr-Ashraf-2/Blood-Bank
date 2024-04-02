package com.ashraf.amr.apps.bloodbank.view.activity;

import android.os.Bundle;

import com.ashraf.amr.apps.bloodbank.R;
import com.ashraf.amr.apps.bloodbank.view.fragment.userCycle.LoginFragment;

import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.replaceFragment;

public class UserCycleActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_cycle);

        replaceFragment(getSupportFragmentManager(), R.id.frame, new LoginFragment());
    }
}
