package com.ashraf.amr.apps.bloodbank.view.activity;


import androidx.appcompat.app.AppCompatActivity;

import com.ashraf.amr.apps.bloodbank.view.fragment.BaseFragment;

public class BaseActivity extends AppCompatActivity {

    public BaseFragment baseFragment;

    @Override
    public void onBackPressed() {
        baseFragment.onBack();
    }

    public void superBackPressed() {
        super.onBackPressed();
    }

}
