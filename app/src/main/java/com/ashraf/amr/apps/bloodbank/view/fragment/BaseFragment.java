package com.ashraf.amr.apps.bloodbank.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ashraf.amr.apps.bloodbank.view.activity.BaseActivity;
import com.ashraf.amr.apps.bloodbank.view.activity.HomeCycleActivity;

public class BaseFragment extends Fragment {

    public BaseActivity baseActivity;
    public HomeCycleActivity homeCycleActivity;

//    public void setUpActivity() {
//        baseActivity = (BaseActivity) getActivity();
//        assert baseActivity != null;
//        baseActivity.baseFragment = this;
//
//        try {
//            homeCycleActivity = (HomeCycleActivity) getActivity();
//        } catch (Exception e) {
//           // ToastCreator.onCreateErrorToast(getActivity(),e.getMessage());
//            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }

    public void setUpActivity() {
        baseActivity = (BaseActivity) getActivity();

        assert baseActivity != null;
        baseActivity.baseFragment = this;
    }


    public void onBack() {
        baseActivity.superBackPressed();
    }

    @Override
    public void onStart() {
        super.onStart();
        setUpActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setUpActivity();
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}







