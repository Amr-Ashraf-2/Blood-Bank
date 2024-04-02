package com.ashraf.amr.apps.bloodbank.view.fragment.homeCycle.staticScreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ashraf.amr.apps.bloodbank.R;
import com.ashraf.amr.apps.bloodbank.data.model.client.ClientData;
import com.ashraf.amr.apps.bloodbank.data.model.setting.Setting;
import com.ashraf.amr.apps.bloodbank.utils.network.InternetState;
import com.ashraf.amr.apps.bloodbank.view.fragment.BaseFragment;
import com.ashraf.amr.apps.bloodbank.view.fragment.homeCycle.MoreFragment;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ashraf.amr.apps.bloodbank.data.api.RetrofitClient.getClient;
import static com.ashraf.amr.apps.bloodbank.data.local.SharedPreferencesManger.loadUserData;
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.customToolbar;
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.dismissProgressDialog;
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.htmlReader;
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.replaceFragment;
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.showProgressDialog;
import static com.ashraf.amr.apps.bloodbank.view.activity.HomeCycleActivity.navBar;
import static com.ashraf.amr.apps.bloodbank.view.activity.HomeCycleActivity.toolBar;
import static com.ashraf.amr.mirtoast.ToastCreator.onCreateErrorToast;

public class AboutAppFragment extends BaseFragment {


    @BindView(R.id.about_app_fragment_iv_about_app)
    TextView aboutAppFragmentIvAboutApp;
    private Unbinder unbinder;
    private ClientData clientData;

    public AboutAppFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about_app, container, false);
        unbinder = ButterKnife.bind(this, view);
        setUpActivity();
        clientData = loadUserData(getActivity());
        toolBar.setVisibility(View.VISIBLE);
        customToolbar(true, getString(R.string.about_us), true, v -> onBack());

        if (navBar != null) {
            navBar.setVisibility(View.GONE);
        }

        settings();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
                            htmlReader(aboutAppFragmentIvAboutApp, response.body().getData().getAboutApp());

                        } else {
                            onCreateErrorToast(Objects.requireNonNull(getActivity()), response.body().getMsg());

                        }

                    } catch (Exception e) {
                        //Toast.makeText(getActivity(), e.getMessage() , Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBack() {
        replaceFragment(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, new MoreFragment());
    }


}
