package com.ashraf.amr.apps.bloodbank.view.fragment.homeCycle.notification;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ashraf.amr.apps.bloodbank.view.fragment.homeCycle.HomeContainerFragment;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.ashraf.amr.apps.bloodbank.R;
import com.ashraf.amr.apps.bloodbank.adapter.NotificationAdapter;
import com.ashraf.amr.apps.bloodbank.data.model.client.ClientData;
import com.ashraf.amr.apps.bloodbank.data.model.notification.getAllNotification.Notification;
import com.ashraf.amr.apps.bloodbank.data.model.notification.getAllNotification.NotificationData;
import com.ashraf.amr.apps.bloodbank.utils.OnEndLess;
import com.ashraf.amr.apps.bloodbank.view.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;
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
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.replaceFragment;
import static com.ashraf.amr.apps.bloodbank.utils.RecycleTool.setRecycleTool;
import static com.ashraf.amr.apps.bloodbank.utils.network.InternetState.isConnected;
import static com.ashraf.amr.apps.bloodbank.view.activity.HomeCycleActivity.homeLine;
import static com.ashraf.amr.apps.bloodbank.view.activity.HomeCycleActivity.moreLine;
import static com.ashraf.amr.apps.bloodbank.view.activity.HomeCycleActivity.navBar;
import static com.ashraf.amr.apps.bloodbank.view.activity.HomeCycleActivity.notificationLine;
import static com.ashraf.amr.apps.bloodbank.view.activity.HomeCycleActivity.profileLine;
import static com.ashraf.amr.apps.bloodbank.view.activity.HomeCycleActivity.toolBar;

public class NotificationsFragment extends BaseFragment {


    @BindView(R.id.notifications_fragment_rv_notification_list)
    RecyclerView notificationsFragmentRvNotificationList;
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
    @BindView(R.id.donations_list_Fragment_sr_refresh_donations)
    SwipeRefreshLayout donationsListFragmentSrRefreshDonations;
    @BindView(R.id.notifications_fragment_s_fl_shimmer_donations)
    ShimmerFrameLayout notificationsFragmentSFlShimmerDonations;
    private Unbinder unbinder;

    private LinearLayoutManager linearLayoutManager;
    private List<NotificationData> notificationsDataList = new ArrayList<>();
    private NotificationAdapter notificationAdapter;
    private int maxPage = 0;
    private OnEndLess onEndLess;
    private ClientData clientData;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolBar.setVisibility(View.VISIBLE);
        customToolbar(true, getString(R.string.notification), true, v -> onBack());
        clientData = loadUserData(getActivity());

        if (navBar != null) {
            navBar.setVisibility(View.GONE);
        }

        init();

        setUpActivity();

        //homeCycleActivity.setNavigation(View.VISIBLE, R.id.home_cycle_activity_rb_home);
//        homeCycleActivity.setToolBar(View.VISIBLE, getString(R.string.notification), new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBack();
//            }
//        });

        return view;
    }

    private void init() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        notificationsFragmentRvNotificationList.setLayoutManager(linearLayoutManager);

        onEndLess = new OnEndLess(linearLayoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {

                if (current_page <= maxPage) {
                    if (maxPage != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;
                        loadMore.setVisibility(View.VISIBLE);

                        getNotification(current_page);

                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                }

            }
        };
        notificationsFragmentRvNotificationList.addOnScrollListener(onEndLess);

        notificationAdapter = new NotificationAdapter(getActivity(), notificationsDataList);
        notificationsFragmentRvNotificationList.setAdapter(notificationAdapter);

        getNotification(1);

        donationsListFragmentSrRefreshDonations.setOnRefreshListener(() -> {
            reInit();
            getNotification(1);
        });
    }

    private void getNotification(int page) {

        if (page == 1) {
            errorSubView.setVisibility(View.GONE);
            notificationsFragmentSFlShimmerDonations.startShimmer();
            notificationsFragmentSFlShimmerDonations.setVisibility(View.VISIBLE);
        }

        if (isConnected(getActivity())) {

            getClient().getNotifications(clientData.getApiToken(), page).enqueue(new Callback<Notification>() {
                @Override
                public void onResponse(@NonNull Call<Notification> call, @NonNull Response<Notification> response) {
                    try {
                        notificationsFragmentSFlShimmerDonations.stopShimmer();
                        notificationsFragmentSFlShimmerDonations.setVisibility(View.GONE);
                        loadMore.setVisibility(View.GONE);
                        donationsListFragmentSrRefreshDonations.setRefreshing(false);

                        assert response.body() != null;
                        if (response.body().getStatus() == 1) {
                            maxPage = response.body().getData().getLastPage();

                            if (response.body().getData().getTotal() != 0) {
                                notificationsDataList.addAll(response.body().getData().getData());
                                notificationAdapter.notifyDataSetChanged();
                            } else {
                                setError(getString(R.string.no_notification));
                            }

                        }

                    } catch (Exception e) {
                       // Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Notification> call, @NonNull Throwable t) {
                    try {
                        notificationsFragmentSFlShimmerDonations.stopShimmer();
                        notificationsFragmentSFlShimmerDonations.setVisibility(View.GONE);
                        loadMore.setVisibility(View.GONE);
                        donationsListFragmentSrRefreshDonations.setRefreshing(false);
                        setError(getString(R.string.error_list));
                    } catch (Exception e) {
                       // Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else {
            try {
                notificationsFragmentSFlShimmerDonations.stopShimmer();
                notificationsFragmentSFlShimmerDonations.setVisibility(View.GONE);
                loadMore.setVisibility(View.GONE);
                donationsListFragmentSrRefreshDonations.setRefreshing(false);
                setError(getString(R.string.error_inter_net));
            } catch (Exception e) {
               // Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void setError(String errorTitleTxt) {
        if (notificationsDataList.size() == 0) {
            View.OnClickListener action = view -> {
                reInit();
                getNotification(1);

            };
            setRecycleTool(baseActivity, errorSubView, errorImage, errorTitle, errorAction, R.drawable.ic_no_notification
                    , errorTitleTxt, getString(R.string.reload), action);
        }
    }

    private void reInit() {
        onEndLess.previousTotal = 0;
        onEndLess.current_page = 1;
        onEndLess.previous_page = 1;
        notificationsDataList = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(getActivity(), notificationsDataList);
        notificationsFragmentRvNotificationList.setAdapter(notificationAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onBack() {
        replaceFragment(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame
                , new HomeContainerFragment());
        homeLine.setVisibility(View.VISIBLE);
        profileLine.setVisibility(View.INVISIBLE);
        notificationLine.setVisibility(View.INVISIBLE);
        moreLine.setVisibility(View.INVISIBLE);
    }
}
