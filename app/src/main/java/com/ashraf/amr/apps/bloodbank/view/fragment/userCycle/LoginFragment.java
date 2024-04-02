package com.ashraf.amr.apps.bloodbank.view.fragment.userCycle;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ashraf.amr.mirtoast.ToastCreator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.jaeger.library.StatusBarUtil;
import com.ashraf.amr.apps.bloodbank.R;
import com.ashraf.amr.apps.bloodbank.data.model.client.Client;
import com.ashraf.amr.apps.bloodbank.utils.HelperMethod;
import com.ashraf.amr.apps.bloodbank.view.fragment.BaseFragment;
import com.rw.keyboardlistener.KeyboardUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;

import static android.content.Context.MODE_PRIVATE;
import static androidx.core.app.ActivityCompat.finishAffinity;
import static com.ashraf.amr.apps.bloodbank.data.api.RetrofitClient.getClient;
import static com.ashraf.amr.apps.bloodbank.utils.Constants.PROFILE_FILE;
import static com.ashraf.amr.apps.bloodbank.utils.Constants.PROFILE_VALUE;
import static com.ashraf.amr.apps.bloodbank.utils.GeneralRequest.userData;
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.closeKeypad;
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.replaceFragment;
import static com.ashraf.amr.mirvalidation.Validation.cleanError;
import static com.ashraf.amr.mirvalidation.Validation.validationPassword;
import static com.ashraf.amr.mirvalidation.Validation.validationPhone;

public class LoginFragment extends BaseFragment {

    @BindView(R.id.login_fragment_til_phone)
    TextInputLayout loginFragmentTilPhone;
    @BindView(R.id.login_fragment_til_password)
    TextInputLayout loginFragmentTilPassword;
    @BindView(R.id.login_fragment_cb_remember)
    CheckBox loginFragmentCbRemember;
    @BindView(R.id.login_fragment_tv_register)
    TextView loginFragmentTvRegister;
    @BindView(R.id.login_fragment_tiet_phone)
    TextInputEditText loginFragmentTietPhone;
    @BindView(R.id.login_fragment_tiet_password)
    TextInputEditText loginFragmentTietPassword;
    private Unbinder unbinder;


    private List<TextInputLayout> textInputLayoutList = new ArrayList<>();

    public LoginFragment() {
        // Required empty public constructor
    }

//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        if(getActivity()!=null){
//            Window w = getActivity().getWindow();
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//
//            w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        }
//    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, view);
        //StatusBarUtil.setTranslucent(getActivity());
        //StatusBarUtil.setTransparent(getActivity());

        StatusBarUtil.setColorNoTranslucent(getActivity(), getResources().getColor(R.color.user_cycle_top));
        Objects.requireNonNull(getActivity()).getWindow().setNavigationBarColor(getResources().getColor(R.color.user_cycle_bottom));

        addKeyboardToggleListener();

        textInputLayoutList.add(loginFragmentTilPhone);
        textInputLayoutList.add(loginFragmentTilPassword);

        Objects.requireNonNull(loginFragmentTilPassword.getEditText()).setTypeface(Typeface.DEFAULT);
        loginFragmentTilPassword.getEditText().setTransformationMethod(new PasswordTransformationMethod());

//        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) loginFragmentTvRegister.getLayoutParams();
//        params.bottomMargin = HelperMethod.getNavigationBarHeight(getActivity());



        return view;
    }

    private void addKeyboardToggleListener() {
        KeyboardUtils.addKeyboardToggleListener(getActivity(), isVisible -> {
            try {
                if (!isVisible) {
                    loginFragmentTvRegister.setVisibility(View.VISIBLE);
                } else {
                    loginFragmentTvRegister.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                //ToastCreator.onCreateErrorToast(Objects.requireNonNull(getActivity()), e.toString());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.login_fragment_tv_forget_password, R.id.login_fragment_btn_login, R.id.login_fragment_tv_register, R.id.login_fragment_rl_sub_view})
    public void onViewClicked(View view) {
        HelperMethod.disappearKeypad(getActivity(), view);
        switch (view.getId()) {
            case R.id.login_fragment_tv_forget_password:
                closeKeypad(Objects.requireNonNull(getActivity()));
                replaceFragment(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), R.id.frame, new ForgetPasswordFragment());
                break;
            case R.id.login_fragment_btn_login:
                closeKeypad(Objects.requireNonNull(getActivity()));
                onValidData();
                break;
            case R.id.login_fragment_tv_register:
                closeKeypad(Objects.requireNonNull(getActivity()));
                getActivity().getSharedPreferences(PROFILE_FILE,MODE_PRIVATE)
                        .edit()
                        .putBoolean(PROFILE_VALUE,false)
                        .apply(); // profile
                replaceFragment(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), R.id.frame, new RegistersAndEditProfileFragment());
                break;
            case R.id.login_fragment_rl_sub_view:
                //closeKeypad(Objects.requireNonNull(getActivity()));
                break;
        }
    }

    private void onValidData() {

        cleanError(textInputLayoutList);

//        if (!validationTextInputLayoutListEmpty(textInputLayoutList, getString(R.string.empty))) {
//            return;
//        }

        if (Objects.requireNonNull(loginFragmentTietPhone.getText()).toString().trim().isEmpty() ||
            Objects.requireNonNull(loginFragmentTietPassword.getText()).toString().trim().isEmpty()) {

            if (loginFragmentTietPhone.getText().toString().isEmpty()){
                loginFragmentTietPhone.setError(getString(R.string.phone_require));
            }

            if (Objects.requireNonNull(loginFragmentTietPassword.getText()).toString().isEmpty()){
                loginFragmentTietPassword.setError(getString(R.string.password_require));
            }

            return;
        }

        if (!validationPhone(Objects.requireNonNull(getActivity()), loginFragmentTietPhone)) {
            return;
        }

        if (!validationPassword(loginFragmentTietPassword, 6, getString(R.string.invalid_password))) {
            return;
        }

        onCall();
    }

    private void onCall() {
        String phone = Objects.requireNonNull(loginFragmentTilPhone.getEditText()).getText().toString();
        String password = Objects.requireNonNull(loginFragmentTilPassword.getEditText()).getText().toString();

        boolean remember = loginFragmentCbRemember.isChecked();

        Call<Client> clientCall = getClient().onLogin(phone, password);


        userData(getActivity(), clientCall, password, remember, true, "Login");
    }

    @Override
    public void onBack() {
        //Objects.requireNonNull(getActivity()).finish();
        closeKeypad(Objects.requireNonNull(getActivity()));
        if(getActivity()!=null){
            //getActivity().moveTaskToBack(true);
            //finishAndRemoveTask();
            //finishAffinity();
            //System.exit(0);
            //Constants.killAll();
            //getActivity().moveTaskToBack(true);
            getActivity().finishAffinity();
            getActivity().finishAndRemoveTask();
            getActivity().finish();
            //System.exit(0);
//            getActivity().moveTaskToBack(true);
//            getActivity().finish();
        }
    }
}
