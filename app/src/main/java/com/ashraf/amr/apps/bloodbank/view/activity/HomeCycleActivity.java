package com.ashraf.amr.apps.bloodbank.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.ashraf.amr.apps.bloodbank.R;
import com.ashraf.amr.apps.bloodbank.data.model.client.ClientData;
import com.ashraf.amr.apps.bloodbank.data.model.notification.getNotificationCount.NotificationCount;
import com.ashraf.amr.apps.bloodbank.view.fragment.homeCycle.HomeContainerFragment;
import com.ashraf.amr.apps.bloodbank.view.fragment.homeCycle.MoreFragment;
import com.ashraf.amr.apps.bloodbank.view.fragment.homeCycle.notification.NotificationsFragment;
import com.ashraf.amr.apps.bloodbank.view.fragment.userCycle.RegistersAndEditProfileFragment;
import com.ashraf.amr.mirtoast.ToastCreator;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ashraf.amr.apps.bloodbank.data.api.RetrofitClient.getClient;
import static com.ashraf.amr.apps.bloodbank.data.local.SharedPreferencesManger.loadUserData;
import static com.ashraf.amr.apps.bloodbank.utils.Constants.GO_TO_HOME_FILE;
import static com.ashraf.amr.apps.bloodbank.utils.Constants.ON_BACK_HOME_VALUE;
import static com.ashraf.amr.apps.bloodbank.utils.Constants.PROFILE_FILE;
import static com.ashraf.amr.apps.bloodbank.utils.Constants.PROFILE_VALUE;
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.replaceFragment;

public class HomeCycleActivity extends BaseActivity {

    @BindView(R.id.toolbar_back)
    RelativeLayout toolbarBack;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_sub_view)
    RelativeLayout toolbarSubView;
    @BindView(R.id.toolbar)
    androidx.appcompat.widget.Toolbar toolbar;

    @BindView(R.id.home_cycle_activity_v_home)
    View homeCycleActivityVHome;
    @BindView(R.id.home_cycle_activity_v_profile)
    View homeCycleActivityVProfile;
    @BindView(R.id.home_cycle_activity_v_notification)
    View homeCycleActivityVNotification;
    @BindView(R.id.home_cycle_activity_v_more)
    View homeCycleActivityVMore;

    @BindView(R.id.home_cycle_activity_rb_home)
    RadioButton homeCycleActivityRbHome;
    @BindView(R.id.home_cycle_activity_rb_profile)
    RadioButton homeCycleActivityRbProfile;
    @BindView(R.id.home_cycle_activity_rb_notification)
    RadioButton homeCycleActivityRbNotification;
    @BindView(R.id.home_cycle_activity_rb_more)
    RadioButton homeCycleActivityRbMore;
    @BindView(R.id.home_cycle_activity_rg_navigation)
    RadioGroup homeCycleActivityRgNavigation;
    @BindView(R.id.home_cycle_activity_img_v_notification)
    ImageView homeCycleActivityImgVNotification;

    public static RadioGroup navBar = null;
    public static View homeLine = null;
    public static View profileLine = null;
    public static View notificationLine = null;
    public static View moreLine = null;

    public static RelativeLayout toolBarBack = null;
    public static TextView toolBarTitle = null;
    public static RelativeLayout toolBar = null;
    public static androidx.appcompat.widget.Toolbar toolbarOriginal = null;
    @BindView(R.id.fragment_client_cart_items_cl_total)
    ConstraintLayout fragmentClientCartItemsClTotal;
    @BindView(R.id.item_custom_notification_alert_tv)
    TextView fragmentClientCartItemstvTotal;

    public ClientData clientData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_cycle);
        ButterKnife.bind(this);

        navBar = homeCycleActivityRgNavigation;
        toolBarBack = toolbarBack;
        toolBarTitle = toolbarTitle;
        toolBar = toolbarSubView;
        toolbarOriginal = toolbar;

        homeLine = homeCycleActivityVHome;
        profileLine = homeCycleActivityVProfile;
        notificationLine = homeCycleActivityVNotification;
        moreLine = homeCycleActivityVMore;

        //this.setSupportActionBar(toolbarOriginal);

        clientData = loadUserData(this);

        getNotificationCount();

        getSharedPreferences(PROFILE_FILE, MODE_PRIVATE)
                .edit()
                .putBoolean(PROFILE_VALUE, true)
                .apply(); // profile

        Objects.requireNonNull(this).getSharedPreferences(GO_TO_HOME_FILE, MODE_PRIVATE)
                .edit()
                .putBoolean(ON_BACK_HOME_VALUE, false)
                .apply();

        replaceFragment(getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, new HomeContainerFragment());
        homeCycleActivityVHome.setVisibility(View.VISIBLE);
    }

    private void getNotificationCount() {
        getClient().getNotificationsCounter(clientData.getApiToken()).enqueue(new Callback<NotificationCount>() {
            @Override
            public void onResponse(@NonNull Call<NotificationCount> call, @NonNull Response<NotificationCount> response) {
                try {
                    assert response.body() != null;
                    if (response.body().getStatus() == 1) {
                        if (response.body().getData().getNotificationsCount() == 0) {
                            fragmentClientCartItemsClTotal.setVisibility(View.GONE);
                        } else {
                            fragmentClientCartItemsClTotal.setVisibility(View.VISIBLE);
                            if (response.body().getData().getNotificationsCount() > 999) {
                                fragmentClientCartItemstvTotal.setText(getString(R.string.notification_counter));
                            } else {
                                fragmentClientCartItemstvTotal.setText(String.valueOf(response.body().getData().getNotificationsCount()));
                            }
                        }
                    }
                } catch (Exception e) {
                   // ToastCreator.onCreateErrorToast(HomeCycleActivity.this, e.getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<NotificationCount> call, @NonNull Throwable t) {

            }
        });
    }

    @OnClick({R.id.home_cycle_activity_rb_home, R.id.home_cycle_activity_rb_profile, R.id.home_cycle_activity_rb_notification, R.id.home_cycle_activity_rb_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.home_cycle_activity_rb_home:
                replaceFragment(getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, new HomeContainerFragment());
                homeCycleActivityVHome.setVisibility(View.VISIBLE);

                homeCycleActivityVProfile.setVisibility(View.INVISIBLE);
                homeCycleActivityVNotification.setVisibility(View.INVISIBLE);
                homeCycleActivityVMore.setVisibility(View.INVISIBLE);

                //Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                break;
            case R.id.home_cycle_activity_rb_profile:
                replaceFragment(getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, new RegistersAndEditProfileFragment());
                homeCycleActivityVHome.setVisibility(View.VISIBLE);
                homeCycleActivityVProfile.setVisibility(View.VISIBLE);

                homeCycleActivityVHome.setVisibility(View.INVISIBLE);
                homeCycleActivityVNotification.setVisibility(View.INVISIBLE);
                homeCycleActivityVMore.setVisibility(View.INVISIBLE);

                //Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                break;
            case R.id.home_cycle_activity_rb_notification:
                replaceFragment(getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, new NotificationsFragment());
                homeCycleActivityVNotification.setVisibility(View.VISIBLE);

                homeCycleActivityVProfile.setVisibility(View.INVISIBLE);
                homeCycleActivityVHome.setVisibility(View.INVISIBLE);
                homeCycleActivityVMore.setVisibility(View.INVISIBLE);

                //Toast.makeText(this, "Notification", Toast.LENGTH_SHORT).show();
                break;
            case R.id.home_cycle_activity_rb_more:
                replaceFragment(getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, new MoreFragment());
                homeCycleActivityVMore.setVisibility(View.VISIBLE);

                homeCycleActivityVNotification.setVisibility(View.INVISIBLE);
                homeCycleActivityVProfile.setVisibility(View.INVISIBLE);
                homeCycleActivityVHome.setVisibility(View.INVISIBLE);

                //Toast.makeText(this, "More", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
