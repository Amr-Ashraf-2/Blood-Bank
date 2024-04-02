package com.ashraf.amr.apps.bloodbank.view.fragment.splashCycle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.ashraf.amr.apps.bloodbank.view.activity.UserCycleActivity;
import com.ashraf.amr.apps.bloodbank.R;
import com.ashraf.amr.apps.bloodbank.view.activity.HomeCycleActivity;
import com.ashraf.amr.apps.bloodbank.view.fragment.BaseFragment;


import java.util.Objects;

import static com.ashraf.amr.apps.bloodbank.data.local.SharedPreferencesManger.LoadBoolean;
import static com.ashraf.amr.apps.bloodbank.data.local.SharedPreferencesManger.REMEMBER;
import static com.ashraf.amr.apps.bloodbank.data.local.SharedPreferencesManger.loadUserData;
import static com.ashraf.amr.apps.bloodbank.utils.Constants.FIRST_RUN_FILE;
import static com.ashraf.amr.apps.bloodbank.utils.Constants.FIRST_START_VALUE;
import static com.ashraf.amr.apps.bloodbank.utils.Constants.SPLASH_DISPLAY_LENGTH;
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.replaceFragment;

public class SplashFragment extends BaseFragment {

    private boolean firstStart = false;


    public SplashFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(getActivity()!=null){
            //Window w = getActivity().getWindow();
            //w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            //getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_splash, container, false);


        SharedPreferences firstRunPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences(FIRST_RUN_FILE, Context.MODE_PRIVATE);
        firstStart = firstRunPreferences.getBoolean(FIRST_START_VALUE,true);


//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (LoadBoolean(getActivity(), REMEMBER) && loadUserData(getActivity()) != null) {
//                    Intent intent = new Intent(getActivity(), HomeCycleActivity.class);
//                    Objects.requireNonNull(getActivity()).startActivity(intent);
//                    getActivity().finish();
//                } else {
//                    replaceFragment(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), R.id.frame, new SliderFragment());
//                }
//
//            }
//        }, 3000);

        new Handler().postDelayed(() -> {
            if(firstStart){
                showSliderForOnce();
                if(getActivity()!=null){
                    replaceFragment(getActivity().getSupportFragmentManager(), R.id.frame , new SliderFragment());
                }
            }else{
                //                if (rememberMe){
                //                    startActivity(new Intent(getActivity(), HomeCycleActivity.class));
                //                }else {
                //                    startActivity(new Intent(getActivity(), UserCycleActivity.class));
                //                }

                if (LoadBoolean(getActivity(), REMEMBER) && loadUserData(getActivity()) != null) {
                    //                    Intent intent = new Intent(getActivity(), HomeCycleActivity.class);
                    //                    Objects.requireNonNull(getActivity()).startActivity(intent);
                    //                    getActivity().finish();
                    startActivity(new Intent(getActivity(), HomeCycleActivity.class));
                    getActivity().finish();
                } else {
                    //                    replaceFragment(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), R.id.frame, new SliderFragment());
                    startActivity(new Intent(getActivity(), UserCycleActivity.class));
                    getActivity().finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);


        return inflate;
    }

    private void showSliderForOnce(){
        if (getActivity()!=null){
            getActivity().getSharedPreferences(FIRST_RUN_FILE, Context.MODE_PRIVATE)
                    .edit()
                    .putBoolean(FIRST_START_VALUE,false)
                    .apply();
        }
    }

//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        //super.onSaveInstanceState(outState);
//    }

    @Override
    public void onBack() {
        if(getActivity()!=null){
//            getActivity().moveTaskToBack(true);
//            getActivity().finish();
            getActivity().finishAndRemoveTask();
        }
        //Objects.requireNonNull(getActivity()).finish();
    }
}
