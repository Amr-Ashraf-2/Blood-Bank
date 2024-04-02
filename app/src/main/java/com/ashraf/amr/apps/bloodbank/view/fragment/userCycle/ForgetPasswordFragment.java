package com.ashraf.amr.apps.bloodbank.view.fragment.userCycle;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ashraf.amr.apps.bloodbank.R;
import com.ashraf.amr.apps.bloodbank.data.model.resetpassword.ResetPassword;
import com.ashraf.amr.apps.bloodbank.utils.HelperMethod;
import com.ashraf.amr.apps.bloodbank.utils.network.InternetState;
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
import static com.ashraf.amr.mirvalidation.Validation.validationEmail;

import java.util.Properties;
import javax.mail.Session;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Message;

public class ForgetPasswordFragment extends BaseFragment {


    @BindView(R.id.forget_password_fragment_til_phone)
    TextInputLayout forgetPasswordFragmentTilPhone;
    @BindView(R.id.forget_password_fragment_btn_send)
    Button forgetPasswordFragmentBtnLogin;
    @BindView(R.id.forget_password_fragment_tiet_phone)
    TextInputEditText forgetPasswordFragmentTietPhone;
    private Unbinder unbinder;

    public ForgetPasswordFragment() {
        // Required empty public constructor
    }

//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        if(getActivity()!=null){
//            Window w = getActivity().getWindow();
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }
//    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forget_password, container, false);
        unbinder = ButterKnife.bind(this, view);
        //StatusBarUtil.setTransparent(getActivity());
        StatusBarUtil.setColorNoTranslucent(getActivity(), getResources().getColor(R.color.user_cycle_top));
        Objects.requireNonNull(getActivity()).getWindow().setNavigationBarColor(getResources().getColor(R.color.user_cycle_bottom));

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.forget_password_fragment_btn_send, R.id.forget_password_fragment_rel_sub_view})
    public void onViewClicked(View view) {
        disappearKeypad(getActivity(), getView());
        switch (view.getId()) {
            case R.id.forget_password_fragment_btn_send:
                closeKeypad(Objects.requireNonNull(getActivity()));
                //replaceFragment(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), R.id.frame, new RestPasswordFragment());
                onCall();
                break;
            case R.id.forget_password_fragment_rel_sub_view:
                //closeKeypad(Objects.requireNonNull(getActivity()));
                break;
        }
    }

    private void onCall() {
        final String phone = Objects.requireNonNull(forgetPasswordFragmentTietPhone.getText()).toString();

//        if (phone.isEmpty()) {
//            onCreateErrorToast(Objects.requireNonNull(getActivity()), getString(R.string.enter_phone_number));
//            return;
//        }

        if (phone.trim().isEmpty()){
            forgetPasswordFragmentTietPhone.setError(getString(R.string.phone_require));
            return;
        }

//        if (!validationEmail(getActivity(), forgetPasswordFragmentTietPhone)) {
//            return;
//        }

        if (phone.length() < 11) {
            onCreateErrorToast(Objects.requireNonNull(getActivity()), getString(R.string.phone3));
            return;
        }

        if (InternetState.isConnected(getActivity())) {

            showProgressDialog(getActivity(), getString(R.string.wait));
            //Call<ResetPassword> call = ;

//            try {
//                int randomNumber = HelperMethod.random();
//
//                final String username = "bloodtypeapplication@gmail.com";
//                final String password = "rootingdoll200";
//                Properties props = new Properties();
//                props.put("mail.smtp.auth", "true");
//                props.put("mail.smtp.starttls.enable", "true");
//                props.put("mail.smtp.host", "smtp.gmail.com");
//                props.put("mail.smtp.port", "587 ");
//
//                Session session = Session.getInstance(props,
//                        new javax.mail.Authenticator(){
//
//                            protected PasswordAuthentication getPasswordAuthentication(){
//                                return new PasswordAuthentication(username,password);
//                            }
//                        });
//                try {
//                    Message message = new MimeMessage(session);
//                    message.setFrom(new InternetAddress("bloodtypeapplication@gmail.com"));
//                    message.setRecipients(Message.RecipientType.TO,
//                            InternetAddress.parse(forgetPasswordFragmentTietPhone.getText().toString()));
//                    message.setSubject(getString(R.string.email_subject));
//                    message.setText("Your Activation Code is " + randomNumber);
//                    Objects.requireNonNull(getActivity()).getSharedPreferences("RANDOM_FILE",Context.MODE_PRIVATE)
//                            .edit()
//                            .putInt("ACTIVATION_CODE",0)
//                            .apply();
//                    Transport.send(message);
//                    dismissProgressDialog();
//                    Toast.makeText(getActivity() ,"Please, Check your email",Toast.LENGTH_LONG).show();
//                    RestPasswordFragment restPasswordFragment = new RestPasswordFragment();
//                    restPasswordFragment.phone = phone;
//                    replaceFragment(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), R.id.frame, restPasswordFragment);
//                }catch (MessagingException ex){
//                    throw new RuntimeException(ex);
//                }
//
//            }catch (Exception e){
//                e.printStackTrace();
//            }



            getClient().forgetPassword(phone).enqueue(new Callback<ResetPassword>() {
                @Override
                public void onResponse(@NonNull Call<ResetPassword> call,@NonNull Response<ResetPassword> response) {
                    try {
                        assert response.body() != null;
                        if (response.body().getStatus() == 1) {
                            RestPasswordFragment restPasswordFragment = new RestPasswordFragment();
                            restPasswordFragment.phone = phone;
                            replaceFragment(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), R.id.frame, restPasswordFragment);
                            ToastCreator.onCreateSuccessToast(getActivity(), response.body().getMsg());
                        } else {
                            onCreateErrorToast(Objects.requireNonNull(getActivity()), response.body().getMsg());
                        }

                        dismissProgressDialog();

                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                        dismissProgressDialog();
                        //ToastCreator.onCreateErrorToast(Objects.requireNonNull(getActivity()), e.toString());
                    }

                }

                @Override
                public void onFailure(@NonNull Call<ResetPassword> call,@NonNull Throwable t) {
                    dismissProgressDialog();
                    onCreateErrorToast(Objects.requireNonNull(getActivity()), getString(R.string.error));
                }
            });
        } else {
            dismissProgressDialog();
            onCreateErrorToast(Objects.requireNonNull(getActivity()), getResources().getString(R.string.error_inter_net));
        }
    }

    @Override
    public void onBack() {
        //Objects.requireNonNull(getActivity()).finish();
        closeKeypad(Objects.requireNonNull(getActivity()));
        replaceFragment(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), R.id.frame, new LoginFragment());
    }

}
