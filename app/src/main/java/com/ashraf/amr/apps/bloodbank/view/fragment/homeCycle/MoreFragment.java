package com.ashraf.amr.apps.bloodbank.view.fragment.homeCycle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.ashraf.amr.apps.bloodbank.R;
import com.ashraf.amr.apps.bloodbank.utils.LogOutDialog;
import com.ashraf.amr.apps.bloodbank.view.fragment.BaseFragment;
import com.ashraf.amr.apps.bloodbank.view.fragment.homeCycle.notification.NotificationsSettingFragment;
import com.ashraf.amr.apps.bloodbank.view.fragment.homeCycle.post.PostsAndFavoritesListFragment;
import com.ashraf.amr.apps.bloodbank.view.fragment.homeCycle.staticScreen.AboutAppFragment;
import com.ashraf.amr.apps.bloodbank.view.fragment.homeCycle.staticScreen.ContactUsFragment;


import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.customToolbar;
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.replaceFragment;
import static com.ashraf.amr.apps.bloodbank.view.activity.HomeCycleActivity.homeLine;
import static com.ashraf.amr.apps.bloodbank.view.activity.HomeCycleActivity.moreLine;
import static com.ashraf.amr.apps.bloodbank.view.activity.HomeCycleActivity.navBar;
import static com.ashraf.amr.apps.bloodbank.view.activity.HomeCycleActivity.notificationLine;
import static com.ashraf.amr.apps.bloodbank.view.activity.HomeCycleActivity.profileLine;
import static com.ashraf.amr.apps.bloodbank.view.activity.HomeCycleActivity.toolBar;

public class MoreFragment extends BaseFragment {


    private Unbinder unbinder;

    public MoreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        unbinder = ButterKnife.bind(this, view);
        setUpActivity();
        toolBar.setVisibility(View.VISIBLE);
        customToolbar(true,getString(R.string.more), false, null);

        if (navBar != null) {
            navBar.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.more_fragment_rel_favourites, R.id.more_fragment_rel_contact_us, R.id.more_fragment_rel_about_app, R.id.more_fragment_rel_rate, R.id.more_fragment_rel_notifications_settings, R.id.more_fragment_rel_sign_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.more_fragment_rel_favourites:
                PostsAndFavoritesListFragment postsAndFavoritesList = new PostsAndFavoritesListFragment();
                postsAndFavoritesList.favourites = true;
                assert getFragmentManager() != null;
                replaceFragment(getFragmentManager(), R.id.home_cycle_activity_fl_home_frame, postsAndFavoritesList);

                break;
            case R.id.more_fragment_rel_contact_us:
                replaceFragment(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame
                        , new ContactUsFragment());
                break;
            case R.id.more_fragment_rel_about_app:
                replaceFragment(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame
                        , new AboutAppFragment());
                break;
            case R.id.more_fragment_rel_rate:
                break;
            case R.id.more_fragment_rel_notifications_settings:
                replaceFragment(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame
                        , new NotificationsSettingFragment());
                break;
            case R.id.more_fragment_rel_sign_out:
                LogOutDialog dialog = new LogOutDialog();
                dialog.showDialog(getActivity());

                break;
        }
    }

    @Override
    public void onBack() {
        replaceFragment(Objects.requireNonNull(getActivity()).getSupportFragmentManager(),
                R.id.home_cycle_activity_fl_home_frame, new HomeContainerFragment());
        homeLine.setVisibility(View.VISIBLE);
        profileLine.setVisibility(View.INVISIBLE);
        notificationLine.setVisibility(View.INVISIBLE);
        moreLine.setVisibility(View.INVISIBLE);
    }
}
