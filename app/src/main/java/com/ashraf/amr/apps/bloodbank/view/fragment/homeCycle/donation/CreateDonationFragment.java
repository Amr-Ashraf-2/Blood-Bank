package com.ashraf.amr.apps.bloodbank.view.fragment.homeCycle.donation;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ashraf.amr.apps.bloodbank.data.model.donation.createNewDonation.CreateNewDonation;
import com.ashraf.amr.apps.bloodbank.data.model.generalResponse.GeneralResponse;
import com.ashraf.amr.apps.bloodbank.view.activity.MapsActivity;
import com.ashraf.amr.apps.bloodbank.R;
import com.ashraf.amr.apps.bloodbank.adapter.SpinnerAdapter;
import com.ashraf.amr.apps.bloodbank.data.model.client.ClientData;
import com.ashraf.amr.apps.bloodbank.utils.HelperMethod;
import com.ashraf.amr.apps.bloodbank.view.fragment.BaseFragment;
import com.ashraf.amr.apps.bloodbank.view.fragment.homeCycle.HomeContainerFragment;
import com.ashraf.amr.mirtoast.ToastCreator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
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
import static com.ashraf.amr.apps.bloodbank.utils.GeneralRequest.getSpinnerData;
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.closeKeypad;
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.customToolbar;
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.progressDialog;
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.replaceFragment;
import static com.ashraf.amr.apps.bloodbank.view.activity.HomeCycleActivity.homeLine;
import static com.ashraf.amr.apps.bloodbank.view.activity.HomeCycleActivity.moreLine;
import static com.ashraf.amr.apps.bloodbank.view.activity.HomeCycleActivity.navBar;
import static com.ashraf.amr.apps.bloodbank.view.activity.HomeCycleActivity.notificationLine;
import static com.ashraf.amr.apps.bloodbank.view.activity.HomeCycleActivity.profileLine;
import static com.ashraf.amr.apps.bloodbank.view.activity.HomeCycleActivity.toolBar;
import static com.ashraf.amr.apps.bloodbank.view.activity.MapsActivity.hospital_address;
import static com.ashraf.amr.apps.bloodbank.view.activity.MapsActivity.hospital_name;
import static com.ashraf.amr.mirvalidation.Validation.validationLength;
import static com.ashraf.amr.mirvalidation.Validation.validationPhone;

public class CreateDonationFragment extends BaseFragment {

    @BindView(R.id.create_donation_fragment_til_name)
    TextInputLayout createDonationFragmentTilName;
    @BindView(R.id.create_donation_fragment_til_age)
    TextInputLayout createDonationFragmentTilAge;
    @BindView(R.id.create_donation_fragment_til_bags_number)
    TextInputLayout createDonationFragmentTilBagsNumber;
    @BindView(R.id.create_donation_fragment_til_hospital_name)
    TextInputLayout createDonationFragmentTilHospitalName;
    @BindView(R.id.create_donation_fragment_til_hospital_address)
    TextInputLayout createDonationFragmentTilHospitalAddress;
    @BindView(R.id.create_donation_fragment_til_phone)
    TextInputLayout createDonationFragmentTilPhone;
    @BindView(R.id.create_donation_fragment_til_note)
    TextInputLayout createDonationFragmentTilNote;
    @BindView(R.id.create_donation_fragment_sp_blood_types)
    Spinner createDonationFragmentSpBloodTypes;
    @BindView(R.id.create_donation_fragment_sp_government)
    Spinner createDonationFragmentSpGovernment;
    @BindView(R.id.create_donation_fragment_sp_city)
    Spinner createDonationFragmentSpCity;
    @BindView(R.id.create_donation_fragment_rl_city_container)
    RelativeLayout createDonationFragmentRlCityContainer;
    @BindView(R.id.create_donation_fragment_tiet_name)
    TextInputEditText createDonationFragmentTietName;
    @BindView(R.id.create_donation_fragment_tiet_age)
    TextInputEditText createDonationFragmentTietAge;
    @BindView(R.id.create_donation_fragment_tiet_bags_number)
    TextInputEditText createDonationFragmentTietBagsNumber;
    @BindView(R.id.create_donation_fragment_tiethospital_name)
    TextInputEditText createDonationFragmentTiethospitalName;
    @BindView(R.id.create_donation_fragment_tiet_hospital_address)
    TextInputEditText createDonationFragmentTietHospitalAddress;
    @BindView(R.id.create_donation_fragment_tiet_phone)
    TextInputEditText createDonationFragmentTietPhone;
    @BindView(R.id.create_donation_fragment_tiet_note)
    TextInputEditText createDonationFragmentTietNote;
    @BindView(R.id.create_donation_fragment_rl_blood_types)
    RelativeLayout createDonationFragmentRlBloodTypes;
    @BindView(R.id.create_donation_fragment_rl_government)
    RelativeLayout createDonationFragmentRlGovernment;
    @BindView(R.id.create_donation_fragment_rl_hospital_address)
    RelativeLayout createDonationFragmentRlHospitalAddress;
    private Unbinder unbinder;

    public DonationsListFragment donationsListFragment;
    private ClientData clientData;

    private SpinnerAdapter bloodTypesAdapter, governmentsAdapter, citiesAdapter;
    private int bloodTypesSelectedId = 0, governmentSelectedId = 0, citiesSelectedId = 0;
    private AdapterView.OnItemSelectedListener listener;

    public CreateDonationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_donation, container, false);
        unbinder = ButterKnife.bind(this, view);
        setUpActivity();

        donationsListFragment = new DonationsListFragment();

        clientData = loadUserData(getActivity());

        if (navBar != null) {
            navBar.setVisibility(View.GONE);
        }

        toolBar.setVisibility(View.VISIBLE);
        customToolbar(true, getString(R.string.create_donation), true, v -> onBack());

        setSpinner();

        createDonationFragmentSpBloodTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                createDonationFragmentRlBloodTypes.setBackgroundResource(R.drawable.shape_et);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        createDonationFragmentSpCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                createDonationFragmentRlCityContainer.setBackgroundResource(R.drawable.shape_et);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        createDonationFragmentTietHospitalAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                createDonationFragmentRlHospitalAddress.setBackgroundResource(R.drawable.shape_et);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return view;
    }

    private void setSpinner() {
        bloodTypesAdapter = new SpinnerAdapter(getActivity());
//        getSpinnerData(getActivity(), createDonationFragmentSpBloodTypes, bloodTypesAdapter, getString(R.string.select_blood_type),
//                getClient().getBloodTypes(), null, bloodTypesSelectedId, true);

        boolean show = true;

        if (show) {
            if (progressDialog == null) {
                HelperMethod.showProgressDialog(getActivity(), getString(R.string.wait));
            } else {
                if (!progressDialog.isShowing()) {
                    HelperMethod.showProgressDialog(getActivity(), getString(R.string.wait));
                }
            }
        }

        getClient().getBloodTypes().enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(@NonNull Call<GeneralResponse> call, @NonNull Response<GeneralResponse> response) {
                try {
//                    if (show) {
//                        HelperMethod.dismissProgressDialog();
//                    }

                    assert response.body() != null;
                    if (response.body().getStatus() == 1) {

                        bloodTypesAdapter.setData(response.body().getData(), getString(R.string.select_blood_type));

                        createDonationFragmentSpBloodTypes.setAdapter(bloodTypesAdapter);

                        //createDonationFragmentSpBloodTypes.setSelection(bloodTypesSelectedId);
                        getSpinnerData(getActivity(), createDonationFragmentSpGovernment, governmentsAdapter, getString(R.string.select_government),
                                getClient().getGovernorates(), governmentSelectedId, listener);

                    }
                } catch (Exception e) {
                    //ToastCreator.onCreateErrorToast(activity,e.getMessage());
                }

            }

            @Override
            public void onFailure(@NonNull Call<GeneralResponse> call, @NonNull Throwable t) {
                if (show) {
                    //HelperMethod.dismissProgressDialog();
                }
            }
        });

        governmentsAdapter = new SpinnerAdapter(getActivity());
        citiesAdapter = new SpinnerAdapter(getActivity());
        listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                createDonationFragmentRlGovernment.setBackgroundResource(R.drawable.shape_et);
                if (i != 0) {
                    getSpinnerData(getActivity(), createDonationFragmentSpCity, citiesAdapter, getString(R.string.select_city)
                            , getClient().getCities(governmentsAdapter.selectedId), createDonationFragmentRlCityContainer
                            , citiesSelectedId, true);
                } else {
                    createDonationFragmentRlCityContainer.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        };

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.create_donation_fragment_btn_send, R.id.create_donation_fragment_ll_sub_view, R.id.create_donation_fragment_iv_select_location})
    public void onViewClicked(View view) {
        HelperMethod.disappearKeypad(getActivity(), view);

        switch (view.getId()) {
            case R.id.create_donation_fragment_btn_send:
                closeKeypad(Objects.requireNonNull(getActivity()));
                onValidation();
                break;
            case R.id.create_donation_fragment_iv_select_location:
                closeKeypad(Objects.requireNonNull(getActivity()));
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                Objects.requireNonNull(getActivity()).startActivity(intent);
                break;
            case R.id.create_donation_fragment_ll_sub_view:
                //closeKeypad(Objects.requireNonNull(getActivity()));
                break;
        }
    }

    private void onValidation() {

        if (Objects.requireNonNull(createDonationFragmentTietName.getText()).toString().isEmpty() ||
                Objects.requireNonNull(createDonationFragmentTietAge.getText()).toString().trim().isEmpty() ||
                createDonationFragmentSpBloodTypes.getSelectedItemPosition() == 0 ||
                Objects.requireNonNull(createDonationFragmentTietBagsNumber.getText()).toString().trim().isEmpty() ||
                Objects.requireNonNull(createDonationFragmentTiethospitalName.getText()).toString().isEmpty() ||
                Objects.requireNonNull(createDonationFragmentTietHospitalAddress.getText()).toString().isEmpty() ||
                createDonationFragmentSpGovernment.getSelectedItemPosition() == 0 ||
                createDonationFragmentSpCity.getSelectedItemPosition() == 0 ||
                Objects.requireNonNull(createDonationFragmentTietPhone.getText()).toString().trim().isEmpty()) {


            if (Objects.requireNonNull(createDonationFragmentTietName.getText()).toString().isEmpty()) {
                createDonationFragmentTietName.setError(getString(R.string.new_name_require));
            }

            if (Objects.requireNonNull(createDonationFragmentTietAge.getText()).toString().isEmpty()) {
                createDonationFragmentTietAge.setError(getString(R.string.invalid_age));
            }

            if (createDonationFragmentSpBloodTypes.getSelectedItemPosition() == 0) {
                createDonationFragmentRlBloodTypes.setBackgroundResource(R.drawable.shape_et_error);
            }

            if (Objects.requireNonNull(createDonationFragmentTietBagsNumber.getText()).toString().isEmpty()) {
                createDonationFragmentTietBagsNumber.setError(getString(R.string.invalid_bags_number));
            }

            if (Objects.requireNonNull(createDonationFragmentTiethospitalName.getText()).toString().isEmpty()) {
                createDonationFragmentTiethospitalName.setError(getString(R.string.invalid_hospital_name));
            }

            if (Objects.requireNonNull(createDonationFragmentTietHospitalAddress.getText()).toString().isEmpty()) {
                createDonationFragmentRlHospitalAddress.setBackgroundResource(R.drawable.shape_et_error);
            }

            if (createDonationFragmentSpGovernment.getSelectedItemPosition() == 0) {
                createDonationFragmentRlGovernment.setBackgroundResource(R.drawable.shape_et_error);
            }

            if (createDonationFragmentSpCity.getSelectedItemPosition() == 0) {
                createDonationFragmentRlCityContainer.setBackgroundResource(R.drawable.shape_et_error);
            }

            if (Objects.requireNonNull(createDonationFragmentTietPhone.getText()).toString().isEmpty()) {
                createDonationFragmentTietPhone.setError(getString(R.string.phone_require));
            }

            if (Objects.requireNonNull(createDonationFragmentTietPhone.getText()).toString().isEmpty()) {
                createDonationFragmentTietPhone.setError(getString(R.string.phone_require));
            }

            return;
        }

        if (createDonationFragmentSpCity.getSelectedItemPosition() == 0) {
            createDonationFragmentRlCityContainer.setBackgroundResource(R.drawable.shape_et_error);
            return;
        }

        if (!validationLength(createDonationFragmentTietAge, getString(R.string.invalid_age), 0)) {
            return;
        }


        if (!validationLength(createDonationFragmentTilBagsNumber, getString(R.string.invalid_bags_number), 1)) {
            return;
        }


        if (!validationPhone(Objects.requireNonNull(getActivity()), createDonationFragmentTietPhone)) {
            return;
        }

        startCall();

    }

    private void startCall() {

        String apiToken = clientData.getApiToken();
        String patientName = Objects.requireNonNull(createDonationFragmentTietName.getText()).toString();
        String patientAge = Objects.requireNonNull(createDonationFragmentTietAge.getText()).toString().trim();
        String bagsNum = Objects.requireNonNull(createDonationFragmentTietBagsNumber.getText()).toString().trim();
        String hospitalName;
//        if (hospital_name != null){
//            hospitalName = hospital_name;
//        }else {
            hospitalName = Objects.requireNonNull(createDonationFragmentTiethospitalName.getText()).toString();
//        }
        final String hospitalAddress;
        if (hospital_address != null){
            hospitalAddress = hospital_address;
        }else {
            hospitalAddress = Objects.requireNonNull(createDonationFragmentTietHospitalAddress.getText()).toString();
        }
        String phone = Objects.requireNonNull(createDonationFragmentTietPhone.getText()).toString().trim();
        String notes = Objects.requireNonNull(createDonationFragmentTietNote.getText()).toString().trim();
        int bloodTypeId = createDonationFragmentSpBloodTypes.getSelectedItemPosition();
        int cityId = createDonationFragmentSpCity.getSelectedItemPosition();

        if (MapsActivity.latitude != 0.0 && MapsActivity.longitude != 0.0   )
        getClient().createNewDonation(apiToken, patientName, patientAge, bloodTypeId, bagsNum, hospitalName, hospitalAddress,
                cityId, phone, notes, MapsActivity.latitude , MapsActivity.longitude).enqueue(new Callback<CreateNewDonation>() {
            @Override
            public void onResponse(@NonNull Call<CreateNewDonation> call, @NonNull Response<CreateNewDonation> response) {
                try {

                    assert response.body() != null;
                    if (response.body().getStatus() == 1) {
                        MapsActivity.latitude = 0;
                        MapsActivity.longitude = 0;
                        hospital_address = null;
                        hospital_name = null;
                        if (donationsListFragment.donationDataList != null && donationsListFragment.donationAdapter != null){
                            donationsListFragment.donationDataList.add(0, response.body().getData());
                            donationsListFragment.donationAdapter.notifyDataSetChanged();
                            ToastCreator.onCreateSuccessToast(Objects.requireNonNull(getActivity()), response.body().getMsg());
                            onBack();
                        }else {
                        Toast.makeText(getActivity(), "Donation has been created successfully", Toast.LENGTH_LONG).show();
                        HelperMethod.replaceFragment(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, new DonationsListFragment());                        }
                    }

                } catch (Exception e) {
                   // Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    //ToastCreator.onCreateErrorToast(Objects.requireNonNull(getActivity()), e.getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<CreateNewDonation> call, @NonNull Throwable t) {
               // Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (hospital_address != null) {
            //createDonationFragmentTiethospitalName.setText(hospital_name);
            createDonationFragmentTietHospitalAddress.setText(hospital_address);
        }
    }

    @Override
    public void onBack() {
        replaceFragment(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, new HomeContainerFragment());
        homeLine.setVisibility(View.VISIBLE);
        profileLine.setVisibility(View.INVISIBLE);
        notificationLine.setVisibility(View.INVISIBLE);
        moreLine.setVisibility(View.INVISIBLE);
    }
}
