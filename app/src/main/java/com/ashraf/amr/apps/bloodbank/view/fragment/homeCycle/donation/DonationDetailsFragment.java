package com.ashraf.amr.apps.bloodbank.view.fragment.homeCycle.donation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ashraf.amr.apps.bloodbank.R;
import com.ashraf.amr.apps.bloodbank.data.model.client.ClientData;
import com.ashraf.amr.apps.bloodbank.data.model.donation.createNewDonation.CreateNewDonation;
import com.ashraf.amr.apps.bloodbank.data.model.donation.donationRequests.DonationData;
import com.ashraf.amr.apps.bloodbank.view.fragment.BaseFragment;
import com.ashraf.amr.apps.bloodbank.view.fragment.homeCycle.HomeContainerFragment;
import com.ashraf.amr.mirtoast.ToastCreator;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.bitmapDescriptorFromVector;
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.customToolbar;
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.onPermission;
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.replaceFragment;
import static com.ashraf.amr.apps.bloodbank.utils.network.InternetState.isConnected;
import static com.ashraf.amr.apps.bloodbank.view.activity.HomeCycleActivity.navBar;
import static com.ashraf.amr.apps.bloodbank.view.activity.HomeCycleActivity.toolBar;

public class DonationDetailsFragment extends BaseFragment {

    @BindView(R.id.donation_details_fragment_tv_name)
    TextView donationDetailsFragmentTvName;
    @BindView(R.id.donation_details_fragment_tv_age)
    TextView donationDetailsFragmentTvAge;
    @BindView(R.id.donation_details_fragment_tv_blood_type)
    TextView donationDetailsFragmentTvBloodType;
    @BindView(R.id.donation_details_fragment_tv_count)
    TextView donationDetailsFragmentTvCount;
    @BindView(R.id.donation_details_fragment_tv_hospital)
    TextView donationDetailsFragmentTvHospital;
    @BindView(R.id.donation_details_fragment_tv_address)
    TextView donationDetailsFragmentTvAddress;
    @BindView(R.id.donation_details_fragment_tv_phone_2)
    TextView donationDetailsFragmentTvPhone2;
    @BindView(R.id.donation_details_fragment_tv_notes)
    TextView donationDetailsFragmentTvNotes;
    @BindView(R.id.donation_details_fragment_mv_map)
    MapView donationDetailsFragmentMvMap;
    @BindView(R.id.donation_details_fragment_s_fl_shimmer_donations)
    ShimmerFrameLayout donationDetailsFragmentSFlShimmerDonations;
    @BindView(R.id.donation_details_fragment_v_notes_line)
    View donationDetailsFragmentVNotesLine;
    private Unbinder unbinder;

    public int donationId;
    private ClientData clientData;
    private DonationData donationData;

    public DonationDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_donation_details, container, false);
        unbinder = ButterKnife.bind(this, view);
        setUpActivity();
        clientData = loadUserData(getActivity());
        toolBar.setVisibility(View.VISIBLE);

        donationDetailsFragmentMvMap.onCreate(savedInstanceState);

        getDonation();

        if (navBar != null) {
            navBar.setVisibility(View.GONE);
        }

        return view;
    }

    private void getDonation() {
        donationDetailsFragmentSFlShimmerDonations.startShimmer();
        donationDetailsFragmentSFlShimmerDonations.setVisibility(View.VISIBLE);

        if (isConnected(getActivity())) {

            getClient().getDonationRequestData(clientData.getApiToken(), donationId).enqueue(new Callback<CreateNewDonation>() {
                @Override
                public void onResponse(@NonNull Call<CreateNewDonation> call, @NonNull Response<CreateNewDonation> response) {
                    try {

                        donationDetailsFragmentSFlShimmerDonations.stopShimmer();
                        donationDetailsFragmentSFlShimmerDonations.setVisibility(View.GONE);

                        assert response.body() != null;
                        donationData = response.body().getData();
                        setData();

                    } catch (Exception e) {
                       // Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CreateNewDonation> call, @NonNull Throwable t) {
                    ToastCreator.onCreateErrorToast(Objects.requireNonNull(getActivity()), getActivity().getString(R.string.error));
                }
            });
        } else {
            ToastCreator.onCreateErrorToast(Objects.requireNonNull(getActivity()), getActivity().getString(R.string.error_inter_net));
        }

    }

    @SuppressLint("SetTextI18n")
    private void setData() {

        customToolbar(true, getString(R.string.donation) + " :  " + donationData.getPatientName(), true, v -> onBack());

        //Toast.makeText(getActivity(), donationData.getPhone() , Toast.LENGTH_SHORT).show();
        donationDetailsFragmentTvName.setText(getString(R.string.name) + " :  " + donationData.getPatientName());
        donationDetailsFragmentTvAge.setText(getString(R.string.age) + " :  " + donationData.getPatientAge());
        donationDetailsFragmentTvBloodType.setText(getString(R.string.blood_type) + " :  " + donationData.getBloodType().getName());
        donationDetailsFragmentTvCount.setText(getString(R.string.bags_number) + " :  " + donationData.getBagsNum());
        donationDetailsFragmentTvHospital.setText(getString(R.string.hospital) + " :  " + donationData.getHospitalName());
        donationDetailsFragmentTvAddress.setText(getString(R.string.address) + " :  " + donationData.getHospitalAddress());
        donationDetailsFragmentTvPhone2.setText(getString(R.string.phone) + " :  " + donationData.getPhone());

        if (donationData.getNotes() == null) {
            donationDetailsFragmentVNotesLine.setVisibility(View.GONE);
            donationDetailsFragmentTvNotes.setVisibility(View.GONE);
        } else {
            donationDetailsFragmentVNotesLine.setVisibility(View.VISIBLE);
            donationDetailsFragmentTvNotes.setText(donationData.getNotes());
        }

        donationDetailsFragmentMvMap.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(Objects.requireNonNull(getActivity()).getApplicationContext());
        } catch (Exception e) {
           // Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            //e.printStackTrace();
        }

        donationDetailsFragmentMvMap.getMapAsync(googleMap -> {
            if (donationData.getLatitude() != null && donationData.getLongitude() != null){
                donationDetailsFragmentMvMap.setVisibility(View.VISIBLE);
                try {
                    //currentUserLocation.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_alt_solid));
                    //Toast.makeText(getActivity(), donationData.getId() + "", Toast.LENGTH_SHORT).show();
                    MarkerOptions currentUserLocation = new MarkerOptions();
                    LatLng currentUserLatLang = new LatLng(Double.parseDouble(donationData.getLatitude()), Double.parseDouble(donationData.getLongitude()));
                    currentUserLocation.position(currentUserLatLang);
                    currentUserLocation.icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_map_marker_alt_solid));
                    currentUserLocation.title(donationData.getLatitude() + " : " + donationData.getLongitude());
                    googleMap.clear();
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentUserLatLang, 17f));
                    googleMap.addMarker(currentUserLocation);

//                float zoom = 6.5f;//10
//                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentUserLatLang, zoom));

                } catch (Exception e) {
                   // Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }else {
                donationDetailsFragmentMvMap.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "There are no address specified", Toast.LENGTH_LONG).show();
            }

        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.donation_details_fragment_btn_call})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.donation_details_fragment_btn_call) {
            onPermission(getActivity());

            Objects.requireNonNull(getActivity()).startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "01277778921", null)));
        }
    }

    @Override
    public void onBack() {
        replaceFragment(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, new HomeContainerFragment());
    }

}
