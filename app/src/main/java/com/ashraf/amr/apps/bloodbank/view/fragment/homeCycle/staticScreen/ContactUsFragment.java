package com.ashraf.amr.apps.bloodbank.view.fragment.homeCycle.staticScreen;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ashraf.amr.apps.bloodbank.R;
import com.ashraf.amr.apps.bloodbank.data.model.client.ClientData;
import com.ashraf.amr.apps.bloodbank.data.model.contactUs.ContactUs;
import com.ashraf.amr.apps.bloodbank.data.model.setting.Setting;
import com.ashraf.amr.apps.bloodbank.utils.network.InternetState;
import com.ashraf.amr.apps.bloodbank.view.fragment.BaseFragment;
import com.ashraf.amr.apps.bloodbank.view.fragment.homeCycle.MoreFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ashraf.amr.apps.bloodbank.data.api.RetrofitClient.getClient;
import static com.ashraf.amr.apps.bloodbank.data.local.SharedPreferencesManger.loadUserData;
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.closeKeypad;
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.customToolbar;
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.disappearKeypad;
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.dismissProgressDialog;
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.replaceFragment;
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.showProgressDialog;
import static com.ashraf.amr.apps.bloodbank.view.activity.HomeCycleActivity.navBar;
import static com.ashraf.amr.apps.bloodbank.view.activity.HomeCycleActivity.toolBar;
import static com.ashraf.amr.mirtoast.ToastCreator.onCreateErrorToast;
import static com.ashraf.amr.mirtoast.ToastCreator.onCreateSuccessToast;

public class ContactUsFragment extends BaseFragment {


    @BindView(R.id.contact_us_fragment_tv_phone_number)
    TextView contactUsFragmentTvPhoneNumber;
    @BindView(R.id.contact_us_fragment_tv_email_address)
    TextView contactUsFragmentTvEmailAddress;
    @BindView(R.id.contact_us_fragment_til_message_title)
    TextInputLayout contactUsFragmentTilMessageTitle;
    @BindView(R.id.contact_us_fragment_til_message_content)
    TextInputLayout contactUsFragmentTilMessageContent;
    @BindView(R.id.fragment_contact_us_ll_phone)
    LinearLayout fragmentContactUsLlPhone;
    @BindView(R.id.fragment_contact_us_ll_email)
    LinearLayout fragmentContactUsLlEmail;
    @BindView(R.id.contact_us_fragment_tv_phone)
    TextView contactUsFragmentTvPhone;
    @BindView(R.id.contact_us_fragment_tv_email)
    TextView contactUsFragmentTvEmail;
    @BindView(R.id.contact_us_fragment_tiet_message_title)
    TextInputEditText contactUsFragmentTietMessageTitle;
    @BindView(R.id.contact_us_fragment_tiet_message_content)
    TextInputEditText contactUsFragmentTietMessageContent;
    private Unbinder unbinder;
    private ClientData clientData;

    private String facebookUrl = null,
            instagramUrl = null,
            twitterUrl = null,
            youtubeUrl = null;

    public ContactUsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        unbinder = ButterKnife.bind(this, view);
        clientData = loadUserData(getActivity());
        settings();
        setUpActivity();
        toolBar.setVisibility(View.VISIBLE);
        customToolbar(true, getString(R.string.contact_us), true, v -> onBack());


        if (navBar != null) {
            navBar.setVisibility(View.GONE);
        }

        fragmentContactUsLlPhone.setOnClickListener(v -> {
            if (!contactUsFragmentTvPhoneNumber.getText().toString().isEmpty()) {
                closeKeypad(Objects.requireNonNull(getActivity()));
                Intent phoneIntent = new Intent();
                phoneIntent.setAction(Intent.ACTION_DIAL);
                Uri phoneUri = Uri.parse("tel:" + contactUsFragmentTvPhoneNumber.getText().toString());
                phoneIntent.setData(phoneUri);
                if (getActivity() != null) {
                    getActivity().startActivity(phoneIntent);
                }
            }
        });

        fragmentContactUsLlEmail.setOnClickListener(v -> {
            if (!contactUsFragmentTvEmailAddress.getText().toString().isEmpty()) {
                closeKeypad(Objects.requireNonNull(getActivity()));
                Intent emailIntent = new Intent();
                emailIntent.setAction(Intent.ACTION_VIEW);
                Uri emailUri = Uri.parse("mailto:" + contactUsFragmentTvEmailAddress.getText().toString());
                emailIntent.setData(emailUri);
                if (getActivity() != null) {
                    getActivity().startActivity(emailIntent);
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.contact_us_fragment_rel_instagram, R.id.contact_us_fragment_rel_facebook, R.id.contact_us_fragment_rel_youtube, R.id.contact_us_fragment_rel_twitter, R.id.contact_us_fragment_btn_send, R.id.contact_us_fragment_rel_sub_view})
    public void onViewClicked(View view) {
        disappearKeypad(getActivity(), getView());
        switch (view.getId()) {
            case R.id.contact_us_fragment_rel_instagram:
                closeKeypad(Objects.requireNonNull(getActivity()));
                // Go To Instegram App
                if (instagramUrl != null) {
                    Uri uri = Uri.parse(instagramUrl); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
                break;
            case R.id.contact_us_fragment_rel_facebook:
                closeKeypad(Objects.requireNonNull(getActivity()));
                // Go To Facebook App
                if (facebookUrl != null) {
                    Uri uri = Uri.parse(facebookUrl); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
                break;
            case R.id.contact_us_fragment_rel_youtube:
                closeKeypad(Objects.requireNonNull(getActivity()));
                // Go To Youtube App
                if (youtubeUrl != null) {
                    Uri uri = Uri.parse(youtubeUrl); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
                break;
            case R.id.contact_us_fragment_rel_twitter:
                closeKeypad(Objects.requireNonNull(getActivity()));
                // Go To Twitter App
                if (twitterUrl != null) {
                    Uri uri = Uri.parse(twitterUrl); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
                break;
            case R.id.contact_us_fragment_btn_send:
                closeKeypad(Objects.requireNonNull(getActivity()));
                onContactUs();
                break;
            case R.id.contact_us_fragment_rel_sub_view:
                //closeKeypad(Objects.requireNonNull(getActivity()));
                break;
        }
    }

    private void settings() {
        if (InternetState.isConnected(getActivity())) {
            showProgressDialog(getActivity(), getString(R.string.wait));
            Call<Setting> call;

            call = getClient().getSettings(clientData.getApiToken());
            call.enqueue(new Callback<Setting>() {
                @Override
                public void onResponse(@NonNull Call<Setting> call, @NonNull Response<Setting> response) {
                    try {
                        assert response.body() != null;
                        if (response.body().getStatus() == 1) {

                            contactUsFragmentTvPhoneNumber.setText(response.body().getData().getPhone());
                            contactUsFragmentTvEmailAddress.setText(response.body().getData().getEmail());

                            facebookUrl = response.body().getData().getFacebookUrl();
                            instagramUrl = response.body().getData().getInstagramUrl();
                            twitterUrl = response.body().getData().getTwitterUrl();
                            youtubeUrl = response.body().getData().getYoutubeUrl();
                        } else {
                            onCreateErrorToast(Objects.requireNonNull(getActivity()), response.body().getMsg());

                        }

                    } catch (Exception e) {
                       // Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    dismissProgressDialog();
                }

                @Override
                public void onFailure(@NonNull Call<Setting> call, @NonNull Throwable t) {
                    dismissProgressDialog();
                    onCreateErrorToast(Objects.requireNonNull(getActivity()), getString(R.string.error));
                }
            });


        } else {
            dismissProgressDialog();
            onCreateErrorToast(Objects.requireNonNull(getActivity()), getResources().getString(R.string.error_inter_net));
        }
    }

    private void onContactUs() {
       // String title = Objects.requireNonNull(contactUsFragmentTilMessageTitle.getEditText()).getText().toString().trim();
       // String message = Objects.requireNonNull(contactUsFragmentTilMessageContent.getEditText()).getText().toString().trim();
//        contactUsFragmentTietMessageTitle
//        contactUsFragmentTietMessageContent

        if (Objects.requireNonNull(contactUsFragmentTietMessageTitle.getText()).toString().isEmpty() ||
                Objects.requireNonNull(contactUsFragmentTietMessageContent.getText()).toString().isEmpty()){

            if (Objects.requireNonNull(contactUsFragmentTietMessageTitle.getText()).toString().isEmpty()){
                contactUsFragmentTietMessageTitle.setError(getString(R.string.enter_message_title));
            }

            if (Objects.requireNonNull(contactUsFragmentTietMessageContent.getText()).toString().isEmpty()){
                contactUsFragmentTietMessageContent.setError(getString(R.string.enter_message_content));
            }


            return;
        }

        if (InternetState.isConnected(getActivity())) {
            showProgressDialog(getActivity(), getString(R.string.wait));

            Call<ContactUs> call;


            call = getClient().contactUs(contactUsFragmentTietMessageTitle.getText().toString()
                    , contactUsFragmentTietMessageContent.getText().toString()
                    , clientData.getApiToken());


            call.enqueue(new Callback<ContactUs>() {
                @Override
                public void onResponse(@NonNull Call<ContactUs> call, @NonNull Response<ContactUs> response) {

                    dismissProgressDialog();
                    try {
                        assert response.body() != null;
                        if (response.body().getStatus() == 1) {

                            onCreateSuccessToast(Objects.requireNonNull(getActivity()), response.body().getMsg());
                            contactUsFragmentTietMessageTitle.setText("");
                            contactUsFragmentTietMessageContent.setText("");

                        } else {
                            onCreateErrorToast(Objects.requireNonNull(getActivity()), response.body().getMsg());
                        }


                    } catch (Exception e) {
                       // Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(@NonNull Call<ContactUs> call, @NonNull Throwable t) {
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
        replaceFragment(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, new MoreFragment());
    }


}
