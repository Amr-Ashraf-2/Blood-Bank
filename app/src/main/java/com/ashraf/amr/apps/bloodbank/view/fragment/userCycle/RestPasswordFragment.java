package com.ashraf.amr.apps.bloodbank.view.fragment.userCycle;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.ashraf.amr.apps.bloodbank.R;
import com.ashraf.amr.apps.bloodbank.data.model.generalResponse.GeneralResponse;
import com.ashraf.amr.apps.bloodbank.view.fragment.BaseFragment;
import com.ashraf.amr.mirtoast.ToastCreator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.jaeger.library.StatusBarUtil;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ashraf.amr.apps.bloodbank.data.api.RetrofitClient.getClient;
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.closeKeypad;
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.disappearKeypad;
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.dismissProgressDialog;
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.replaceFragment;
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.showProgressDialog;
import static com.ashraf.amr.mirtoast.ToastCreator.onCreateErrorToast;


public class RestPasswordFragment extends BaseFragment {

    public String phone;
    @BindView(R.id.reset_password_fragment_til_pin_code)
    TextInputLayout resetPasswordFragmentTilPinCode;
    @BindView(R.id.reset_password_fragment_til_password)
    TextInputLayout resetPasswordFragmentTilPassword;
    @BindView(R.id.reset_password_fragment_til_password_confirmation)
    TextInputLayout resetPasswordFragmentTilPasswordConfirmation;
    @BindView(R.id.reset_password_fragment_tiet_pin_code)
    TextInputEditText resetPasswordFragmentTietPinCode;
    @BindView(R.id.reset_password_fragment_tiet_password)
    TextInputEditText resetPasswordFragmentTietPassword;
    @BindView(R.id.reset_password_fragment_tiet_password_confirmation)
    TextInputEditText resetPasswordFragmentTietPasswordConfirmation;
    private Unbinder unbinder;
    private int activationCode = 0;

    public RestPasswordFragment() {
        // Required empty public constructor
    }

//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        if (getActivity() != null) {
//            Window w = getActivity().getWindow();
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }
//    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rest_password, container, false);
        unbinder = ButterKnife.bind(this, view);
        //StatusBarUtil.setTransparent(getActivity());
        StatusBarUtil.setColorNoTranslucent(getActivity(), getResources().getColor(R.color.user_cycle_top));
        Objects.requireNonNull(getActivity()).getWindow().setNavigationBarColor(getResources().getColor(R.color.user_cycle_bottom));


        SharedPreferences activationSharedPreferences = getActivity().getSharedPreferences("RANDOM_FILE",Context.MODE_PRIVATE);
        activationCode = activationSharedPreferences.getInt("ACTIVATION_CODE", 0);


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.reset_password_fragment_btn_change, R.id.reset_password_fragment_rel_sub_view})
    public void onViewClicked(View view) {
        disappearKeypad(getActivity(), getView());
        switch (view.getId()) {
            case R.id.reset_password_fragment_btn_change:
                closeKeypad(Objects.requireNonNull(getActivity()));
                onCall();
                break;
            case R.id.reset_password_fragment_rel_sub_view:
                //closeKeypad(Objects.requireNonNull(getActivity()));
                break;
        }
    }

    private void onCall() {
        String pin_code = Objects.requireNonNull(resetPasswordFragmentTilPinCode.getEditText()).getText().toString();
        String password = Objects.requireNonNull(resetPasswordFragmentTilPassword.getEditText()).getText().toString();
        String password_confirmation = Objects.requireNonNull(resetPasswordFragmentTilPasswordConfirmation.getEditText()).getText().toString();

        if (Objects.requireNonNull(resetPasswordFragmentTietPinCode.getText()).toString().trim().isEmpty() ||
            Objects.requireNonNull(resetPasswordFragmentTietPassword.getText()).toString().trim().isEmpty() ||
            Objects.requireNonNull(resetPasswordFragmentTietPasswordConfirmation.getText()).toString().trim().isEmpty()) {

            if (Objects.requireNonNull(resetPasswordFragmentTietPinCode.getText()).toString().trim().isEmpty()){
                resetPasswordFragmentTietPinCode.setError(getString(R.string.enter_pin_code));
            }

            if (Objects.requireNonNull(resetPasswordFragmentTietPassword.getText()).toString().trim().isEmpty()){
                resetPasswordFragmentTietPassword.setError(getString(R.string.enter_password));
            }

            if (Objects.requireNonNull(resetPasswordFragmentTietPasswordConfirmation.getText()).toString().trim().isEmpty()){
                resetPasswordFragmentTietPasswordConfirmation.setError(getString(R.string.enter_password_confirm));
            }

            return;
        }

//         resetPasswordFragmentTietPinCode
//         resetPasswordFragmentTietPassword
//         resetPasswordFragmentTietPasswordConfirmation

//        if (pin_code.isEmpty() && password.isEmpty() && password_confirmation.isEmpty()) {
//
//            return;
//        }

//        if (pin_code.isEmpty()) {
//            onCreateErrorToast(Objects.requireNonNull(getActivity()), getString(R.string.enter_pin_code));
//
//            return;
//        }

//        if (password.isEmpty()) {
//            onCreateErrorToast(Objects.requireNonNull(getActivity()), getString(R.string.enter_password));
//
//            return;
//        }

        if (resetPasswordFragmentTietPassword.length() <= 3){
            resetPasswordFragmentTietPassword.setError(getString(R.string.weak_password));
            return;
        }else {
            resetPasswordFragmentTietPassword.setError(null);
        }

//        if (!validationLength(getActivity(), password, getString(R.string.weak_password), 3)) {
//                resetPasswordFragmentTietPassword.setError(getString(R.string.weak_password));
//            return;
//        }

//        if (password_confirmation.isEmpty()) {
//            onCreateErrorToast(Objects.requireNonNull(getActivity()), getString(R.string.enter_password_confirm));
//
//            return;
//        }

        if (!resetPasswordFragmentTietPassword.getText().toString().trim().equals(resetPasswordFragmentTietPasswordConfirmation.getText().toString().trim())){
            resetPasswordFragmentTietPassword.setError(getString(R.string.invalid_confirm_password));
            resetPasswordFragmentTietPasswordConfirmation.setError(getString(R.string.invalid_confirm_password));
            return;
        }else {
            resetPasswordFragmentTietPassword.setError(null);
            resetPasswordFragmentTietPasswordConfirmation.setError(null);
        }

//        if (!validationConfirmPassword(getActivity(), password, password_confirmation)) {
//
//            return;
//        }

        // Change Password

        showProgressDialog(getActivity(), getString(R.string.wait));
        Call<GeneralResponse> call = getClient().newPassword(pin_code, password, password_confirmation, phone);
        call.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(@NonNull Call<GeneralResponse> call, @NonNull Response<GeneralResponse> response) {
                try {
                    assert response.body() != null;
                    if (response.body().getStatus() == 1) {
                        //LoginFragment loginFragment = new LoginFragment();
                        replaceFragment(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), R.id.frame, new LoginFragment());
                        ToastCreator.onCreateSuccessToast(getActivity(), response.body().getMsg());

                    } else {
                        onCreateErrorToast(Objects.requireNonNull(getActivity()), response.body().getMsg());
                    }

                    dismissProgressDialog();

                } catch (Exception e) {
                   // onCreateErrorToast(Objects.requireNonNull(getActivity()), e.toString());
                }

            }

            @Override
            public void onFailure(@NonNull Call<GeneralResponse> call, @NonNull Throwable t) {
                dismissProgressDialog();
                onCreateErrorToast(Objects.requireNonNull(getActivity()), getString(R.string.error));
            }
        });

    }

    @Override
    public void onBack() {
        //Objects.requireNonNull(getActivity()).finish();
        closeKeypad(Objects.requireNonNull(getActivity()));
        replaceFragment(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), R.id.frame, new ForgetPasswordFragment());
    }
}
