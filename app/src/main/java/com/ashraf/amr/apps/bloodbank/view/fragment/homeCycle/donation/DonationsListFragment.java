package com.ashraf.amr.apps.bloodbank.view.fragment.homeCycle.donation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ashraf.amr.apps.bloodbank.adapter.CustomViewPager2;
import com.ashraf.amr.mirtoast.ToastCreator;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.ashraf.amr.apps.bloodbank.R;
import com.ashraf.amr.apps.bloodbank.adapter.DonationAdapter;
import com.ashraf.amr.apps.bloodbank.adapter.SpinnerAdapter;
import com.ashraf.amr.apps.bloodbank.data.model.client.ClientData;
import com.ashraf.amr.apps.bloodbank.data.model.donation.donationRequests.DonationData;
import com.ashraf.amr.apps.bloodbank.data.model.donation.donationRequests.DonationRequests;
import com.ashraf.amr.apps.bloodbank.utils.OnEndLess;
import com.ashraf.amr.apps.bloodbank.view.fragment.BaseFragment;
import com.ashraf.amr.apps.bloodbank.view.fragment.homeCycle.HomeContainerFragment;

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

import static android.content.Context.MODE_PRIVATE;
import static com.ashraf.amr.apps.bloodbank.data.api.RetrofitClient.getClient;
import static com.ashraf.amr.apps.bloodbank.data.local.SharedPreferencesManger.loadUserData;
import static com.ashraf.amr.apps.bloodbank.utils.Constants.FROM_DONATIONS;
import static com.ashraf.amr.apps.bloodbank.utils.Constants.FROM_POSTS;
import static com.ashraf.amr.apps.bloodbank.utils.Constants.GO_TO_HOME_FILE;
import static com.ashraf.amr.apps.bloodbank.utils.Constants.ON_BACK_HOME_VALUE;
import static com.ashraf.amr.apps.bloodbank.utils.Constants.ON_RETURN_BACK_HOME_VALUE;
import static com.ashraf.amr.apps.bloodbank.utils.GeneralRequest.getSpinnerData;
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.replaceFragment;
import static com.ashraf.amr.apps.bloodbank.utils.RecycleTool.setRecycleTool;
import static com.ashraf.amr.apps.bloodbank.utils.network.InternetState.isConnected;
import static com.ashraf.amr.apps.bloodbank.view.fragment.homeCycle.HomeContainerFragment.donationTab;

public class DonationsListFragment extends BaseFragment {

    @BindView(R.id.donations_list_Fragment_sp_blood_types)
    Spinner donationsListFragmentSpBloodTypes;
    @BindView(R.id.donations_list_Fragment_sp_government)
    Spinner donationsListFragmentSpGovernment;
    @BindView(R.id.donations_list_Fragment_rv_donations)
    RecyclerView donationsListFragmentRvDonations;
    @BindView(R.id.donations_list_Fragment_s_fl_shimmer_donations)
    ShimmerFrameLayout donationsListFragmentSFlShimmerDonations;
    @BindView(R.id.donations_list_Fragment_sr_refresh_donations)
    SwipeRefreshLayout donationsListFragmentSrRefreshDonations;
    @BindView(R.id.load_more)
    RelativeLayout loadMore;
    @BindView(R.id.error_image)
    ImageView errorImage;
    @BindView(R.id.error_title)
    TextView errorTitle;
    @BindView(R.id.error_action)
    TextView errorAction;
    @BindView(R.id.error_sub_view)
    View errorSubView;
    @BindView(R.id.donations_list_Fragment_ll_sub_view)
    LinearLayout donationsListFragmentLlSubView;
    private Unbinder unbinder;

    private SpinnerAdapter bloodTypesAdapter, gaviermentAdapter;
    private LinearLayoutManager linearLayout;
    public List<DonationData> donationDataList = new ArrayList<>();
    public DonationAdapter donationAdapter;
    private Integer maxPage = 0;
    private OnEndLess onEndLess;
    private boolean Filter = false;
    private ClientData clientData;
    //public CustomViewPager homeContainerFragmentVpViewPager;
    public CustomViewPager2 homeContainerFragmentVpViewPager2;
    public HomeContainerFragment homeContainerFragment;
    //private String lang = "en";

    public DonationsListFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_donation_list, container, false);
        unbinder = ButterKnife.bind(this, view);

//        homeContainerFragmentVpViewPager.setPagingEnabled(false);

        clientData = loadUserData(getActivity());

        if (getActivity() != null) {
            bloodTypesAdapter = new SpinnerAdapter(getActivity(), getResources().getDimension(R.dimen._11ssp));
            getSpinnerData(getActivity(), donationsListFragmentSpBloodTypes, bloodTypesAdapter, getString(R.string.select_blood_type),
                    getClient().getBloodTypes(), null, 0, false);

            gaviermentAdapter = new SpinnerAdapter(getActivity(), getResources().getDimension(R.dimen._11ssp));
            getSpinnerData(getActivity(), donationsListFragmentSpGovernment, gaviermentAdapter, getString(R.string.select_government),
                    getClient().getGovernorates(), null, 0, false);
        }


        init();

//        donationsListFragmentRvDonations.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
//            public void onSwipeTop() {
//            }
//
//            public void onSwipeRight() {
//                if (lang.equals("en")) {
//                    homeContainerFragmentVpViewPager.setPagingEnabled(true);
//                    homeContainerFragmentVpViewPager.setCurrentItem(0);
//                }
//            }
//
//            public void onSwipeLeft() {
//                if (lang.equals("ar")) {
//                    homeContainerFragmentVpViewPager.setPagingEnabled(true);
//                    homeContainerFragmentVpViewPager.setCurrentItem(0);
//                }
//            }
//
//            public void onSwipeBottom() {
//            }
//        });

        return view;
    }

    private void init() {
        linearLayout = new LinearLayoutManager(getActivity());
        donationsListFragmentRvDonations.setLayoutManager(linearLayout);

        onEndLess = new OnEndLess(linearLayout, 1) {
            @Override
            public void onLoadMore(int current_page) {
                if (current_page <= maxPage) {
                    if (maxPage != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;
                        loadMore.setVisibility(View.VISIBLE);
                        if (Filter) {
                            onFilter(current_page);
                        } else {
                            getDonations(current_page);
                        }

                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                } else {
                    onEndLess.current_page = onEndLess.previous_page;
                }
            }
        };
        donationsListFragmentRvDonations.addOnScrollListener(onEndLess);

        donationAdapter = new DonationAdapter(getActivity(), donationDataList);
        donationsListFragmentRvDonations.setAdapter(donationAdapter);

        if (donationDataList.size() == 0) {
            getDonations(1);
        } else {
            donationsListFragmentSFlShimmerDonations.stopShimmer();
            donationsListFragmentSFlShimmerDonations.setVisibility(View.GONE);
        }

        donationsListFragmentSrRefreshDonations.setOnRefreshListener(() -> {

            if (Filter) {
                onFilter(1);
            } else {
                getDonations(1);
            }

        });
    }

    private void getDonations(int page) {
        Call<DonationRequests> call = getClient().getDonationRequests(clientData.getApiToken(), page);

        startCall(call, page);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.donations_list_Fragment_rl_filter, R.id.donations_list_Fragment_f_a_btn_create_donations})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.donations_list_Fragment_rl_filter:

                if (bloodTypesAdapter.selectedId == 0 && gaviermentAdapter.selectedId == 0 && Filter) {
                    Filter = false;

                    getDonations(1);
                } else {
                    onFilter(1);
                }

                break;

            case R.id.donations_list_Fragment_f_a_btn_create_donations:

                Objects.requireNonNull(getActivity()).getSharedPreferences(GO_TO_HOME_FILE, MODE_PRIVATE)
                        .edit()
                        .putBoolean(ON_BACK_HOME_VALUE, true)
                        .putString(ON_RETURN_BACK_HOME_VALUE, FROM_DONATIONS)
                        .apply();

                replaceFragment(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, new CreateDonationFragment());

                break;
        }
    }

    private void onFilter(int page) {

        Filter = true;

        Call<DonationRequests> call = getClient().getDonationRequests(
                clientData.getApiToken(), page, bloodTypesAdapter.selectedId, gaviermentAdapter.selectedId);

        startCall(call, page);
    }

    private void startCall(Call<DonationRequests> call, int page) {
        errorSubView.setVisibility(View.GONE);
        if (page == 1) {
            reInit();
            errorSubView.setVisibility(View.GONE);
            donationsListFragmentSFlShimmerDonations.startShimmer();
            donationsListFragmentSFlShimmerDonations.setVisibility(View.VISIBLE);
        }

        if (isConnected(getActivity())) {

            call.enqueue(new Callback<DonationRequests>() {
                @Override
                public void onResponse(@NonNull Call<DonationRequests> call, @NonNull Response<DonationRequests> response) {
                    try {
                        donationsListFragmentSFlShimmerDonations.stopShimmer();
                        donationsListFragmentSFlShimmerDonations.setVisibility(View.GONE);
                        loadMore.setVisibility(View.GONE);
                        donationsListFragmentSrRefreshDonations.setRefreshing(false);

                        assert response.body() != null;
                        if (response.body().getStatus() == 1) {
                            maxPage = response.body().getData().getLastPage();

                            if (response.body().getData().getTotal() != 0) {
                                donationDataList.addAll(response.body().getData().getData());
                                donationAdapter.notifyDataSetChanged();
                            } else {
                                View.OnClickListener action = view -> {
                                    CreateDonationFragment createDonationFragment = new CreateDonationFragment();
                                    createDonationFragment.donationsListFragment = DonationsListFragment.this;
                                    replaceFragment(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), R.id.frame, new CreateDonationFragment());
                                };
                                setRecycleTool(baseActivity, errorSubView, errorImage, errorTitle, errorAction, R.drawable.ic_transfusion
                                        , getString(R.string.no_donations), getString(R.string.add_new_donation), action);
                            }


                        } else {
                            setError(getString(R.string.error_list));
                        }

                    } catch (Exception e) {
                       // ToastCreator.onCreateErrorToast(Objects.requireNonNull(getActivity()), e.getMessage());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<DonationRequests> call, @NonNull Throwable t) {
                    try {
                        donationsListFragmentSFlShimmerDonations.stopShimmer();
                        donationsListFragmentSFlShimmerDonations.setVisibility(View.GONE);
                        loadMore.setVisibility(View.GONE);
                        donationsListFragmentSrRefreshDonations.setRefreshing(false);
                        setError(getString(R.string.error_list));
                    } catch (Exception e) {
                       // ToastCreator.onCreateErrorToast(Objects.requireNonNull(getActivity()), e.getMessage());
                    }

                }
            });

        } else {
            try {
                donationsListFragmentSFlShimmerDonations.stopShimmer();
                donationsListFragmentSFlShimmerDonations.setVisibility(View.GONE);
                loadMore.setVisibility(View.GONE);
                donationsListFragmentSrRefreshDonations.setRefreshing(false);
                setError(getString(R.string.error_inter_net));
            } catch (Exception e) {
               // ToastCreator.onCreateErrorToast(Objects.requireNonNull(getActivity()), e.getMessage());
            }

        }

    }

    private void reInit() {
        onEndLess.previousTotal = 0;
        onEndLess.current_page = 1;
        onEndLess.previous_page = 1;
        donationDataList = new ArrayList<>();
        donationAdapter = new DonationAdapter(getActivity(), donationDataList);
        donationsListFragmentRvDonations.setAdapter(donationAdapter);
    }

    private void setError(String errorTitleTxt) {
        if (donationDataList.size() == 0) {
            View.OnClickListener action = view -> {

                if (Filter) {
                    onFilter(1);
                } else {
                    getDonations(1);
                }

            };
            setRecycleTool(baseActivity, errorSubView, errorImage, errorTitle, errorAction, R.drawable.ic_transfusion
                    , errorTitleTxt, getString(R.string.reload), action);
        }
    }

    @Override
    public void onBack() {
        if(getActivity()!=null){
            //getActivity().moveTaskToBack(true);
            Objects.requireNonNull(donationTab.getTabAt(0)).select();
            //getActivity().finish();
        }
    }
}
