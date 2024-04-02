package com.ashraf.amr.apps.bloodbank.view.fragment.userCycle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.PasswordTransformationMethod;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ashraf.amr.apps.bloodbank.R;
import com.ashraf.amr.apps.bloodbank.adapter.SpinnerAdapter;
import com.ashraf.amr.apps.bloodbank.data.model.DateTxt;
import com.ashraf.amr.apps.bloodbank.data.model.client.Client;
import com.ashraf.amr.apps.bloodbank.data.model.client.ClientData;
import com.ashraf.amr.apps.bloodbank.data.model.generalResponse.GeneralResponse;
import com.ashraf.amr.apps.bloodbank.utils.HelperMethod;
import com.ashraf.amr.apps.bloodbank.view.activity.HomeCycleActivity;
import com.ashraf.amr.apps.bloodbank.view.activity.UserCycleActivity;
import com.ashraf.amr.apps.bloodbank.view.fragment.BaseFragment;
import com.ashraf.amr.apps.bloodbank.view.fragment.homeCycle.HomeContainerFragment;
import com.ashraf.amr.apps.bloodbank.view.fragment.homeCycle.post.PostsAndFavoritesListFragment;
import com.ashraf.amr.mirtoast.ToastCreator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.jaeger.library.StatusBarUtil;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.ashraf.amr.apps.bloodbank.data.api.RetrofitClient.getClient;
import static com.ashraf.amr.apps.bloodbank.data.local.SharedPreferencesManger.LoadData;
import static com.ashraf.amr.apps.bloodbank.data.local.SharedPreferencesManger.USER_PASSWORD;
import static com.ashraf.amr.apps.bloodbank.data.local.SharedPreferencesManger.loadUserData;
import static com.ashraf.amr.apps.bloodbank.utils.Constants.PROFILE_FILE;
import static com.ashraf.amr.apps.bloodbank.utils.Constants.PROFILE_VALUE;
import static com.ashraf.amr.apps.bloodbank.utils.GeneralRequest.getSpinnerData;
import static com.ashraf.amr.apps.bloodbank.utils.GeneralRequest.userData;
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.closeKeypad;
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.disappearKeypad;
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.keyboardVisibility;
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.progressDialog;
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.replaceFragment;
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.showCalender;
import static com.ashraf.amr.apps.bloodbank.view.activity.HomeCycleActivity.homeLine;
import static com.ashraf.amr.apps.bloodbank.view.activity.HomeCycleActivity.moreLine;
import static com.ashraf.amr.apps.bloodbank.view.activity.HomeCycleActivity.navBar;
import static com.ashraf.amr.apps.bloodbank.view.activity.HomeCycleActivity.notificationLine;
import static com.ashraf.amr.apps.bloodbank.view.activity.HomeCycleActivity.profileLine;
import static com.ashraf.amr.apps.bloodbank.view.activity.HomeCycleActivity.toolBar;
import static com.ashraf.amr.mirvalidation.Validation.validationConfirmPassword;
import static com.ashraf.amr.mirvalidation.Validation.validationEmail;
import static com.ashraf.amr.mirvalidation.Validation.validationPassword;
import static com.ashraf.amr.mirvalidation.Validation.validationPhone;

public class RegistersAndEditProfileFragment extends BaseFragment {

    @BindView(R.id.registers_and_edit_profile_fragment_tv_title)
    TextView registersAndEditProfileFragmentTvTitle;
    @BindView(R.id.registers_and_edit_profile_fragment_til_user_name)
    TextInputLayout registersAndEditProfileFragmentTilUserName;
    @BindView(R.id.registers_and_edit_profile_fragment_til_email)
    TextInputLayout registersAndEditProfileFragmentTilEmail;
    @BindView(R.id.registers_and_edit_profile_fragment_til_phone)
    TextInputLayout registersAndEditProfileFragmentTilPhone;
    @BindView(R.id.registers_and_edit_profile_fragment_til_password)
    TextInputLayout registersAndEditProfileFragmentTilPassword;
    @BindView(R.id.registers_and_edit_profile_fragment_til_confirm_password)
    TextInputLayout registersAndEditProfileFragmentTilConfirmPassword;
    @BindView(R.id.registers_and_edit_profile_fragment_tv_brd)
    TextView registersAndEditProfileFragmentTvBrd;
    @BindView(R.id.registers_and_edit_profile_fragment_tv_last_donation_date)
    TextView registersAndEditProfileFragmentTvLastDonationDate;
    @BindView(R.id.registers_and_edit_profile_fragment_sp_blood_types)
    Spinner registersAndEditProfileFragmentSpBloodTypes;
    @BindView(R.id.registers_and_edit_profile_fragment_sp_governments)
    Spinner registersAndEditProfileFragmentSpGovernments;
    @BindView(R.id.registers_and_edit_profile_fragment_sp_city)
    Spinner registersAndEditProfileFragmentSpCity;
    @BindView(R.id.registers_and_edit_profile_fragment_ll_container_city)
    LinearLayout registersAndEditProfileFragmentLlContainerCity;
    @BindView(R.id.registers_and_edit_profile_fragment_btn_start_call)
    Button registersAndEditProfileFragmentBtnStartCall;
    @BindView(R.id.registers_and_edit_profile_fragment_til_brd)
    LinearLayout registersAndEditProfileFragmentTilBrd;
    @BindView(R.id.registers_and_edit_profile_fragment_til_last_donation_date)
    LinearLayout registersAndEditProfileFragmentTilLastDonationDate;
    @BindView(R.id.registers_and_edit_profile_fragment_ll_passwords)
    LinearLayout registersAndEditProfileFragmentLlPasswords;
    @BindView(R.id.registers_and_edit_profile_fragment_et_pass)
    EditText registersAndEditProfileFragmentEtPass;
    @BindView(R.id.registers_and_edit_profile_fragment_et_confirm_pass)
    EditText registersAndEditProfileFragmentEtConfirmPass;
    private Unbinder unbinder;
    @BindView(R.id.registers_and_edit_profile_fragment_ll_sub_view_bg)
    LinearLayout registersAndEditProfileFragmentLlSubViewBg;
    @BindView(R.id.registers_and_edit_profile_fragment_rl_tool_bar)
    RelativeLayout registersAndEditProfileFragmentRlToolBar;
    @BindView(R.id.registers_and_edit_profile_fragment_tiet_user_name)
    TextInputEditText registersAndEditProfileFragmentTietUsername;
    @BindView(R.id.registers_and_edit_profile_fragment_tiet_email)
    TextInputEditText registersAndEditProfileFragmentTietEmail;
    @BindView(R.id.registers_and_edit_profile_fragment_tiet_phone)
    TextInputEditText registersAndEditProfileFragmentTietPhone;
    @BindView(R.id.registers_and_edit_profile_fragment_tiet_password)
    TextInputEditText registersAndEditProfileFragmentTietPassword;
    @BindView(R.id.registers_and_edit_profile_fragment_tiet_confirm_password)
    TextInputEditText registersAndEditProfileFragmentTietConfirmPassword;
    @BindView(R.id.registers_and_edit_profile_fragment_sv_container)
    ScrollView registersAndEditProfileFragmentSvContainer;
    @BindView(R.id.registers_and_edit_profile_fragment_ll_blood_type_container)
    LinearLayout registersAndEditProfileFragmentLlBloodTypeContainer;
    @BindView(R.id.registers_and_edit_profile_fragment_ll_governorate_container)
    LinearLayout registersAndEditProfileFragmentLlGovernorateContainer;

    private SpinnerAdapter bloodTypesAdapter, governmentsAdapter, citiesAdapter;
    private int bloodTypesSelectedId = 0, governmentSelectedId = 0, citiesSelectedId = 0;
    private DateTxt birthdayDate, lastDonationDate;

    private ClientData clientData;
    public boolean PROFILE = false;
    private boolean profileValue = false;
    private static Animation slide_up, slide_down;
    private AlertDialog passwordAlertDialog;
    private boolean isKeyboardShowing;
    private boolean show;

    public RegistersAndEditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getActivity() != null) {
            SharedPreferences profilePreferences = getActivity().getSharedPreferences(PROFILE_FILE, MODE_PRIVATE);
            profileValue = profilePreferences.getBoolean(PROFILE_VALUE, false);
//            Window w = getActivity().getWindow();
//            if (profileValue) {
//                w.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//            } else {
//                w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registers_and_edit_profile, container, false);
        unbinder = ButterKnife.bind(this, view);
        setUpActivity();
        //StatusBarUtil.setTranslucent(getActivity());
        clientData = loadUserData(getActivity());



        setDates();
//        profileValue = true; // profile
//        profileValue = false; // register
        //Spinner spinner = (Spinner) findViewById(R.id.spinner);

        if (profileValue) { // Profile
            show = true;

            slide_down = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_down);
            slide_up = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_up);

            //keyboardVisibility( registersAndEditProfileFragmentLlSubViewBg , getActivity() );
            isKeyboardShowing = false;
            view.getViewTreeObserver().addOnGlobalLayoutListener(
                    new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {

                            Rect r = new Rect();
                            view.getWindowVisibleDisplayFrame(r);
                            int screenHeight = view.getRootView().getHeight();
                            // r.bottom is the position above soft keypad or device button.
                            // if keypad is shown, the r.bottom is smaller than that before.
                            int keypadHeight = screenHeight - r.bottom;
                            //Log.d(TAG, "keypadHeight = " + keypadHeight);
                            if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                                // keyboard is opened
                                if (!isKeyboardShowing) {
                                    isKeyboardShowing = true;
                                    navBar.startAnimation(slide_down);
                                    navBar.setVisibility(View.GONE);
                                }
                            }
                            else {
                                // keyboard is closed
                                if (isKeyboardShowing) {
                                    isKeyboardShowing = false;
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            navBar.startAnimation(slide_up);
                                            navBar.setVisibility(View.VISIBLE);
                                        }
                                    }, 5);
                                }
                            }
                        }
                    });

            if (show) {
                if (progressDialog == null) {
                    HelperMethod.showProgressDialog(getActivity(), Objects.requireNonNull(getActivity()).getString(R.string.wait));
                } else {
                    if (!progressDialog.isShowing()) {
                        HelperMethod.showProgressDialog(getActivity(), Objects.requireNonNull(getActivity()).getString(R.string.wait));
                    }
                }
            }

            bloodTypesAdapter = new SpinnerAdapter(getActivity());
            getClient().getBloodTypes().enqueue(new Callback<GeneralResponse>() {
                @Override
                public void onResponse(@NonNull Call<GeneralResponse> call, @NonNull Response<GeneralResponse> response) {
                    try {
//                        if (show) {
//                            HelperMethod.dismissProgressDialog();
//                        }
                        assert response.body() != null;
                        if (response.body().getStatus() == 1) {
//                            if (view != null) {
//                                view.setVisibility(View.VISIBLE);
//                            }
                            bloodTypesAdapter.setData(response.body().getData(), getString(R.string.select_blood_type));
                            registersAndEditProfileFragmentSpBloodTypes.setAdapter(bloodTypesAdapter);
                            registersAndEditProfileFragmentSpBloodTypes.setSelection(clientData.getClient().getBloodType().getId());
                        }
                    } catch (Exception e) {
                        //ToastCreator.onCreateErrorToast(activity,e.getMessage());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GeneralResponse> call, @NonNull Throwable t) {
//                    if (show) {
//                        HelperMethod.dismissProgressDialog();
//                    }
                }
            });

            governmentsAdapter = new SpinnerAdapter(getActivity());
            citiesAdapter = new SpinnerAdapter(getActivity());
            getClient().getGovernorates().enqueue(new Callback<GeneralResponse>() {
                @Override
                public void onResponse(@NonNull Call<GeneralResponse> call, @NonNull Response<GeneralResponse> response) {
                    try {
//                        if (show) {
//                            HelperMethod.dismissProgressDialog();
//                        }
                        assert response.body() != null;
                        if (response.body().getStatus() == 1) {
//                            if (view != null) {
//                                view.setVisibility(View.VISIBLE);
//                            }
                            governmentsAdapter.setData(response.body().getData(), getString(R.string.select_government));
                            registersAndEditProfileFragmentSpGovernments.setAdapter(governmentsAdapter);
                            registersAndEditProfileFragmentSpGovernments.setSelection(clientData.getClient().getCity().getGovernorate().getId());
                            registersAndEditProfileFragmentSpGovernments.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if(position != 0){
                                        getCities();
                                    }else{
                                        //binding.fragmentClientProfileRlDistrict.setVisibility(View.GONE);
                                        registersAndEditProfileFragmentSpCity.setAdapter(null);
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                        }
                    } catch (Exception e) {
                        //ToastCreator.onCreateErrorToast(activity,e.getMessage());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GeneralResponse> call, @NonNull Throwable t) {
//                    if (show) {
//                        HelperMethod.dismissProgressDialog();
//                    }
                }
            });

            setUserData();

            StatusBarUtil.setColorNoTranslucent(getActivity(), getResources().getColor(R.color.colorPrimaryDark));

            toolBar.setVisibility(View.VISIBLE);

            registersAndEditProfileFragmentRlToolBar.setVisibility(View.GONE);

            if (navBar != null) {
                navBar.setVisibility(View.VISIBLE);
            }

            HelperMethod.customToolbar(true, getString(R.string.edit_data), false, null);



//            registersAndEditProfileFragmentTietPassword.setEnabled(false);
//            registersAndEditProfileFragmentTietConfirmPassword.setEnabled(false);
            registersAndEditProfileFragmentLlPasswords.setVisibility(View.VISIBLE);
            registersAndEditProfileFragmentTilPassword.setVisibility(View.GONE);
            registersAndEditProfileFragmentTilConfirmPassword.setVisibility(View.GONE);

//            registersAndEditProfileFragmentLlPasswords.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    changePassword();
//                }
//            });
//            registersAndEditProfileFragmentEtPass
//            registersAndEditProfileFragmentEtConfirmPass
            registersAndEditProfileFragmentEtPass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changePassword();
                }
            });
            registersAndEditProfileFragmentEtConfirmPass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changePassword();
                }
            });
//            registersAndEditProfileFragmentTietPass.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    changePassword();
//                }
//            });
//            registersAndEditProfileFragmentTietConfirmPass.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    changePassword();
//                }
//            });

//            registersAndEditProfileFragmentTietPassword.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    changePassword();
//                }
//            });
//
////            registersAndEditProfileFragmentTietConfirmPassword
////            registersAndEditProfileFragmentTilConfirmPassword
//            registersAndEditProfileFragmentTilConfirmPassword.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    changePassword();
//                }
//            });

            registersAndEditProfileFragmentBtnStartCall.setBackgroundResource(R.drawable.shape_et_2);
            registersAndEditProfileFragmentBtnStartCall.setTextColor(getResources().getColor(R.color.white));


            registersAndEditProfileFragmentTvTitle.setBackgroundColor(getResources().getColor(R.color.txt_color2));
            registersAndEditProfileFragmentLlSubViewBg.setBackgroundResource(0);
            registersAndEditProfileFragmentLlSubViewBg.setBackgroundColor(getResources().getColor(R.color.ptofile));

        } else { // register

            //StatusBarUtil.setTransparent(getActivity());
            //StatusBarUtil.setColorNoTranslucent(getActivity(), getResources().getColor(R.color.colorPrimaryDark));
            StatusBarUtil.setColorNoTranslucent(getActivity(), getResources().getColor(R.color.user_cycle_top));
            Objects.requireNonNull(getActivity()).getWindow().setNavigationBarColor(getResources().getColor(R.color.user_cycle_bottom));

            setSpinner();

            registersAndEditProfileFragmentLlPasswords.setVisibility(View.GONE);
            registersAndEditProfileFragmentTilPassword.setVisibility(View.VISIBLE);
            registersAndEditProfileFragmentTilConfirmPassword.setVisibility(View.VISIBLE);

            registersAndEditProfileFragmentRlToolBar.setVisibility(View.VISIBLE);

            registersAndEditProfileFragmentTietPassword.setEnabled(true);
            registersAndEditProfileFragmentTietConfirmPassword.setEnabled(true);
            registersAndEditProfileFragmentBtnStartCall.setBackgroundResource(R.drawable.shape_et);
            registersAndEditProfileFragmentBtnStartCall.setTextColor(getResources().getColor(R.color.txt_color));

//            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) registersAndEditProfileFragmentRlToolBar.getLayoutParams();
//            params.topMargin = HelperMethod.getStatusBarHeight(Objects.requireNonNull(getActivity()));
//
//            ViewGroup.MarginLayoutParams params2 = (ViewGroup.MarginLayoutParams) registersAndEditProfileFragmentSvContainer.getLayoutParams();
//            params2.bottomMargin = HelperMethod.getNavigationBarHeight(Objects.requireNonNull(getActivity()));
        }

        Objects.requireNonNull(registersAndEditProfileFragmentTilPassword.getEditText()).setTypeface(Typeface.DEFAULT);
        registersAndEditProfileFragmentTilPassword.getEditText().setTransformationMethod(new PasswordTransformationMethod());
        Objects.requireNonNull(registersAndEditProfileFragmentTilConfirmPassword.getEditText()).setTypeface(Typeface.DEFAULT);
        registersAndEditProfileFragmentTilConfirmPassword.getEditText().setTransformationMethod(new PasswordTransformationMethod());

        registersAndEditProfileFragmentSpBloodTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                registersAndEditProfileFragmentLlBloodTypeContainer.setBackgroundResource(R.drawable.shape_et);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        registersAndEditProfileFragmentSpGovernments.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                registersAndEditProfileFragmentLlGovernorateContainer.setBackgroundResource(R.drawable.shape_et);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        registersAndEditProfileFragmentSpCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                registersAndEditProfileFragmentLlContainerCity.setBackgroundResource(R.drawable.shape_et);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return view;
    }

    private void getCities() {
        getClient().getCities(governmentsAdapter.selectedId).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(@NonNull Call<GeneralResponse> call, @NonNull Response<GeneralResponse> response) {
                try {
                    if (show) {
                        HelperMethod.dismissProgressDialog();
                    }
                    assert response.body() != null;
                    if (response.body().getStatus() == 1) {
                        if (registersAndEditProfileFragmentLlContainerCity != null) {
                            registersAndEditProfileFragmentLlContainerCity.setVisibility(View.VISIBLE);
                        }
                        citiesAdapter.setData(response.body().getData(), getString(R.string.select_city));
                        registersAndEditProfileFragmentSpCity.setAdapter(citiesAdapter);
                        registersAndEditProfileFragmentSpCity.setSelection(clientData.getClient().getCity().getId());
                    }
                } catch (Exception e) {
                    //ToastCreator.onCreateErrorToast(activity,e.getMessage());
                }

            }

            @Override
            public void onFailure(@NonNull Call<GeneralResponse> call, @NonNull Throwable t) {
                if (show) {
                    HelperMethod.dismissProgressDialog();
                }
            }
        });
    }

    private void changePassword() {
        String pass = LoadData(getActivity(), USER_PASSWORD);
        LayoutInflater changePasswordDialogInflater = LayoutInflater.from(getActivity());
        View dialogBinding = changePasswordDialogInflater.inflate(R.layout.dialog_change_password, null);
        final AlertDialog.Builder changePasswordAlertDialogBuilder = new AlertDialog.Builder(getActivity());
        TextInputEditText oldPassword = dialogBinding.findViewById(R.id.dialog_change_password_tiet_old_password);
        dialogBinding.findViewById(R.id.dialog_change_password_btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //closeKeypad(Objects.requireNonNull(getActivity()));
                disappearKeypad(getActivity() , dialogBinding);
                if (Objects.requireNonNull(oldPassword.getText()).toString().trim().isEmpty()) {
                    oldPassword.setError(getString(R.string.dialog_change_password_et_error));
                } else {
                    if (oldPassword.getText().toString().equals(pass)) {
                        registersAndEditProfileFragmentLlPasswords.setVisibility(View.GONE);
                        registersAndEditProfileFragmentTilPassword.setVisibility(View.VISIBLE);
                        registersAndEditProfileFragmentTilConfirmPassword.setVisibility(View.VISIBLE);

                        passwordAlertDialog.dismiss();
                    } else {
                        registersAndEditProfileFragmentLlPasswords.setVisibility(View.VISIBLE);
                        registersAndEditProfileFragmentTilPassword.setVisibility(View.GONE);
                        registersAndEditProfileFragmentTilConfirmPassword.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), getString(R.string.dialog_change_password_et_wrong) , Toast.LENGTH_LONG).show();
                        //oldPassword.setError(getString(R.string.dialog_change_password_et_wrong));
                    }
                }
            }
        });
        //confirmAlertDialogBuilder.setCancelable(false);
        changePasswordAlertDialogBuilder.setView(dialogBinding);
        passwordAlertDialog = changePasswordAlertDialogBuilder.create();
        Objects.requireNonNull(passwordAlertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        passwordAlertDialog.show();
    }

    private void setSpinner() {
        bloodTypesAdapter = new SpinnerAdapter(getActivity());
        getSpinnerData(getActivity(), registersAndEditProfileFragmentSpBloodTypes, bloodTypesAdapter, getString(R.string.select_blood_type),
                getClient().getBloodTypes(), null, bloodTypesSelectedId, true);

        governmentsAdapter = new SpinnerAdapter(getActivity());
        citiesAdapter = new SpinnerAdapter(getActivity());
        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    getSpinnerData(getActivity(), registersAndEditProfileFragmentSpCity, citiesAdapter, getString(R.string.select_city)
                            , getClient().getCities(governmentsAdapter.selectedId), registersAndEditProfileFragmentLlContainerCity, citiesSelectedId, true);
                } else {
                    registersAndEditProfileFragmentLlContainerCity.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        };

        getSpinnerData(getActivity(), registersAndEditProfileFragmentSpGovernments, governmentsAdapter, getString(R.string.select_government),
                getClient().getGovernorates(), governmentSelectedId, listener);
    }

    private void setUserData() {
        bloodTypesSelectedId = clientData.getClient().getBloodType().getId();
        registersAndEditProfileFragmentSpBloodTypes.setSelection(bloodTypesSelectedId);
        governmentSelectedId = clientData.getClient().getCity().getGovernorate().getId();
        citiesSelectedId = clientData.getClient().getCity().getId();

        Objects.requireNonNull(registersAndEditProfileFragmentTilUserName.getEditText()).setText(clientData.getClient().getName());
        Objects.requireNonNull(registersAndEditProfileFragmentTilEmail.getEditText()).setText(clientData.getClient().getEmail());
        Objects.requireNonNull(registersAndEditProfileFragmentTilPhone.getEditText()).setText(clientData.getClient().getPhone());
        Objects.requireNonNull(registersAndEditProfileFragmentTilPassword.getEditText()).setText(LoadData(getActivity(), USER_PASSWORD));
        Objects.requireNonNull(registersAndEditProfileFragmentTilConfirmPassword.getEditText()).setText(LoadData(getActivity(), USER_PASSWORD));
        registersAndEditProfileFragmentEtPass.setText(LoadData(getActivity(), USER_PASSWORD));
        registersAndEditProfileFragmentEtConfirmPass.setText(LoadData(getActivity(), USER_PASSWORD));

        registersAndEditProfileFragmentTvBrd.setText(clientData.getClient().getBirthDate());
        registersAndEditProfileFragmentTvLastDonationDate.setText(clientData.getClient().getDonationLastDate());

        registersAndEditProfileFragmentTvTitle.setText(getString(R.string.profile));
        registersAndEditProfileFragmentBtnStartCall.setText(getString(R.string.save));

    }

    private void setDates() {
        DecimalFormat mFormat = new DecimalFormat("00");
        Calendar calander = Calendar.getInstance();
        String cDay = mFormat.format(Double.valueOf(String.valueOf(calander.get(Calendar.DAY_OF_MONTH))));
        String cMonth = mFormat.format(Double.valueOf(String.valueOf(calander.get(Calendar.MONTH + 1))));
        String cYear = String.valueOf(calander.get(Calendar.YEAR));

        lastDonationDate = new DateTxt(cDay, cMonth, cYear, cDay + "-" + cMonth + "-" + cYear);
        birthdayDate = new DateTxt("01", "01", "1990", "01-01-1990");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.registers_and_edit_profile_fragment_tv_brd, R.id.registers_and_edit_profile_fragment_tv_last_donation_date, R.id.registers_and_edit_profile_fragment_btn_start_call, R.id.registers_and_edit_profile_fragment_ll_sub_view})
    public void onViewClicked(View view) {
        disappearKeypad(getActivity(), view);
        switch (view.getId()) {
            case R.id.registers_and_edit_profile_fragment_tv_brd:
                closeKeypad(Objects.requireNonNull(getActivity()));
                showCalender(getActivity(), getString(R.string.select_date), registersAndEditProfileFragmentTvBrd, birthdayDate);
                registersAndEditProfileFragmentTvBrd.setError(null);
                break;
            case R.id.registers_and_edit_profile_fragment_tv_last_donation_date:
                registersAndEditProfileFragmentTvLastDonationDate.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen._15ssp));
                closeKeypad(Objects.requireNonNull(getActivity()));
                showCalender(getActivity(), getString(R.string.select_date), registersAndEditProfileFragmentTvLastDonationDate, lastDonationDate);
                registersAndEditProfileFragmentTvLastDonationDate.setError(null);
                break;
            case R.id.registers_and_edit_profile_fragment_btn_start_call:
                closeKeypad(Objects.requireNonNull(getActivity()));
                onValidation();
                break;
            case R.id.registers_and_edit_profile_fragment_ll_sub_view:
                //closeKeypad(Objects.requireNonNull(getActivity()));
                break;
        }
    }

    private void onValidation() {

        //List<EditText> editTexts = new ArrayList<>();
        //List<TextInputLayout> textInputLayouts = new ArrayList<>();
        //List<Spinner> spinners = new ArrayList<>();

//        textInputLayouts.add(registersAndEditProfileFragmentTilUserName);
//        textInputLayouts.add(registersAndEditProfileFragmentTilEmail);
//        textInputLayouts.add(registersAndEditProfileFragmentTilPhone);
//        textInputLayouts.add(registersAndEditProfileFragmentTilPassword);
//        textInputLayouts.add(registersAndEditProfileFragmentTilConfirmPassword);

//        spinners.add(registersAndEditProfileFragmentSpBloodTypes);
//        spinners.add(registersAndEditProfileFragmentSpGovernments);
//        spinners.add(registersAndEditProfileFragmentSpCity);

//        if (!validationAllEmpty(editTexts, textInputLayouts, spinners, getString(R.string.empty)) &&
//                registersAndEditProfileFragmentTvBrd.getText().toString().equals("") &&
//                registersAndEditProfileFragmentTvLastDonationDate.getText().toString().equals("")) {
//
//            registersAndEditProfileFragmentTilBrd.setError(getString(R.string.empty));
//            registersAndEditProfileFragmentTilLastDonationDate.setError(getString(R.string.empty));
//            ToastCreator.onCreateErrorToast(Objects.requireNonNull(getActivity()), getString(R.string.empty));
//            return;
//        } else {
//            registersAndEditProfileFragmentTilBrd.setErrorEnabled(false);
//            registersAndEditProfileFragmentTilLastDonationDate.setErrorEnabled(false);
//        }
//        registersAndEditProfileFragmentTietUsername
//        registersAndEditProfileFragmentTietEmail
//        registersAndEditProfileFragmentTietPhone
//        registersAndEditProfileFragmentTietPassword
//        registersAndEditProfileFragmentTietConfirmPassword

        if (Objects.requireNonNull(registersAndEditProfileFragmentTietUsername.getText()).toString().isEmpty() ||
                Objects.requireNonNull(registersAndEditProfileFragmentTietEmail.getText()).toString().trim().isEmpty() ||
                Objects.requireNonNull(registersAndEditProfileFragmentTvBrd.getText()).toString().trim().isEmpty() ||
                Objects.requireNonNull(registersAndEditProfileFragmentTvLastDonationDate.getText()).toString().trim().isEmpty() ||
                registersAndEditProfileFragmentSpBloodTypes.getSelectedItemPosition() == 0 ||
                registersAndEditProfileFragmentSpGovernments.getSelectedItemPosition() == 0 ||
                Objects.requireNonNull(registersAndEditProfileFragmentTietPhone.getText()).toString().trim().isEmpty() ||
                Objects.requireNonNull(registersAndEditProfileFragmentTietPassword.getText()).toString().trim().isEmpty() ||
                Objects.requireNonNull(registersAndEditProfileFragmentTietConfirmPassword.getText()).toString().trim().isEmpty()) {

            if (Objects.requireNonNull(registersAndEditProfileFragmentTietUsername.getText()).toString().isEmpty()) {
                registersAndEditProfileFragmentTietUsername.setError(getString(R.string.new_name_require));
            }

            if (Objects.requireNonNull(registersAndEditProfileFragmentTietEmail.getText()).toString().isEmpty()) {
                registersAndEditProfileFragmentTietEmail.setError(getString(R.string.new_email_require));
            }

            if (Objects.requireNonNull(registersAndEditProfileFragmentTvBrd.getText()).toString().isEmpty()) {
                registersAndEditProfileFragmentTvBrd.setError(getString(R.string.invalid_brd));
            }

            if (Objects.requireNonNull(registersAndEditProfileFragmentTvLastDonationDate.getText()).toString().isEmpty()) {
                registersAndEditProfileFragmentTvLastDonationDate.setError(getString(R.string.invalid_last_date));
                registersAndEditProfileFragmentTvLastDonationDate.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen._13ssp));
            }

            if (registersAndEditProfileFragmentSpBloodTypes.getSelectedItemPosition() == 0) {
                registersAndEditProfileFragmentLlBloodTypeContainer.setBackgroundResource(R.drawable.shape_et_error);
            }

            if (registersAndEditProfileFragmentSpGovernments.getSelectedItemPosition() == 0) {
                registersAndEditProfileFragmentLlGovernorateContainer.setBackgroundResource(R.drawable.shape_et_error);
            }

            if (registersAndEditProfileFragmentSpCity.getSelectedItemPosition() == 0) {
                registersAndEditProfileFragmentLlContainerCity.setBackgroundResource(R.drawable.shape_et_error);
            }

            if (Objects.requireNonNull(registersAndEditProfileFragmentTietPhone.getText()).toString().isEmpty()) {
                registersAndEditProfileFragmentTietPhone.setError(getString(R.string.phone_require));
            }

            if (Objects.requireNonNull(registersAndEditProfileFragmentTietPassword.getText()).toString().isEmpty()) {
                registersAndEditProfileFragmentTietPassword.setError(getString(R.string.new_password_require));
            }

            if (Objects.requireNonNull(registersAndEditProfileFragmentTietConfirmPassword.getText()).toString().isEmpty()) {
                registersAndEditProfileFragmentTietConfirmPassword.setError(getString(R.string.confirm_password_require));
            }

            return;
        }

        if (registersAndEditProfileFragmentSpCity.getSelectedItemPosition() == 0) {
            registersAndEditProfileFragmentLlContainerCity.setBackgroundResource(R.drawable.shape_et_error);
            return;
        }

        if (!validationEmail(getActivity(), registersAndEditProfileFragmentTilEmail)) {

            return;
        }

        if (!validationPhone(Objects.requireNonNull(getActivity()), registersAndEditProfileFragmentTietPhone)) {
            return;
        }

        if (!validationPassword(registersAndEditProfileFragmentTietPassword, 6, getString(R.string.invalid_password))) {
            return;
        }

        if (!validationConfirmPassword(getActivity(), registersAndEditProfileFragmentTietPassword, registersAndEditProfileFragmentTietConfirmPassword)) {
            return;
        }

        onCall();
    }

    private void onCall() {
        String name = Objects.requireNonNull(registersAndEditProfileFragmentTilUserName.getEditText()).getText().toString();
        String email = Objects.requireNonNull(registersAndEditProfileFragmentTilEmail.getEditText()).getText().toString();
        String phone = Objects.requireNonNull(registersAndEditProfileFragmentTilPhone.getEditText()).getText().toString();
        String password = Objects.requireNonNull(registersAndEditProfileFragmentTilPassword.getEditText()).getText().toString();
        String passwordConfirmation = Objects.requireNonNull(registersAndEditProfileFragmentTilConfirmPassword.getEditText()).getText().toString();

        String birth_date = registersAndEditProfileFragmentTvBrd.getText().toString();
        String donationLastDate = registersAndEditProfileFragmentTvLastDonationDate.getText().toString();

        int cityId = citiesAdapter.selectedId;
        int bloodTypeId = bloodTypesAdapter.selectedId;

        Call<Client> clientCall;
        boolean auth;

        if (!profileValue) { //PROFILE //Register
            auth = true;
            clientCall = getClient().onSignUp(name, email, birth_date, cityId, phone, donationLastDate, password, passwordConfirmation, bloodTypeId);
            //ToastCreator.onCreateSuccessToast(Objects.requireNonNull(getActivity()), getString(R.string.registered));
            userData(getActivity(), clientCall, password, true, auth, "Register");
        } else { // profile update
            auth = false;
            clientCall = getClient().editClientData(name, email, birth_date, registersAndEditProfileFragmentSpCity.getSelectedItemPosition() , phone, donationLastDate, password
                    , passwordConfirmation, bloodTypeId, clientData.getApiToken());
            //ToastCreator.onCreateSuccessToast(Objects.requireNonNull(getActivity()), getString(R.string.data_edited));
            userData(getActivity(), clientCall, password, true, auth, "Update");
        }

    }

    @Override
    public void onBack() {
        if (profileValue) { //PROFILE
            replaceFragment(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame
                    , new HomeContainerFragment());

            homeLine.setVisibility(View.VISIBLE);
            profileLine.setVisibility(View.INVISIBLE);
            notificationLine.setVisibility(View.INVISIBLE);
            moreLine.setVisibility(View.INVISIBLE);
//            replaceFragment(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame
//                    , new PostsAndFavoritesListFragment());
            //startActivity(new Intent(getActivity(), UserCycleActivity.class));

            //replaceFragment(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), R.id.frame, new LoginFragment());

        } else {
            replaceFragment(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), R.id.frame, new LoginFragment());

//            replaceFragment(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame
//                    , new HomeContainerFragment());
        }
    }

}
