package com.ashraf.amr.apps.bloodbank.view.fragment.homeCycle;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;

import com.ashraf.amr.apps.bloodbank.R;
import com.ashraf.amr.apps.bloodbank.adapter.CustomViewPager2;
import com.ashraf.amr.apps.bloodbank.adapter.ViewPagerWithFragmentAdapter;
import com.ashraf.amr.apps.bloodbank.utils.HelperMethod;
import com.ashraf.amr.apps.bloodbank.utils.SwipeDirection;
import com.ashraf.amr.apps.bloodbank.view.fragment.BaseFragment;
import com.ashraf.amr.apps.bloodbank.view.fragment.homeCycle.donation.DonationsListFragment;
import com.ashraf.amr.apps.bloodbank.view.fragment.homeCycle.post.PostsAndFavoritesListFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.ashraf.amr.apps.bloodbank.utils.Constants.FROM_DONATIONS;
import static com.ashraf.amr.apps.bloodbank.utils.Constants.FROM_POSTS;
import static com.ashraf.amr.apps.bloodbank.utils.Constants.GO_TO_HOME_FILE;
import static com.ashraf.amr.apps.bloodbank.utils.Constants.ON_BACK_HOME_VALUE;
import static com.ashraf.amr.apps.bloodbank.utils.Constants.ON_RETURN_BACK_HOME_VALUE;
import static com.ashraf.amr.apps.bloodbank.view.activity.HomeCycleActivity.navBar;
import static com.ashraf.amr.apps.bloodbank.view.activity.HomeCycleActivity.toolBar;

public class HomeContainerFragment extends BaseFragment {


    @BindView(R.id.home_container_fragment_tl_tab)
    TabLayout homeContainerFragmentTlTab;
    @BindView(R.id.home_container_fragment_vp_view_pager)
    //CustomViewPager homeContainerFragmentVpViewPager;
            CustomViewPager2 homeContainerFragmentVpViewPager2;
    @BindView(R.id.home_container_fragment_ll_container)
    LinearLayout homeContainerFragmentLlContainer;
    private Unbinder unbinder;
    private ViewGroup viewContainer;
    private Boolean openFragmentWithBack = false;
    private String getFragGoFrom = null;

    public PostsAndFavoritesListFragment postsAndFavoritesListFragment;
    public DonationsListFragment donationsListFragment;

    public static TabLayout donationTab = null;

    public HomeContainerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_container, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolBar.setVisibility(View.GONE);
        viewContainer = container;
        setUpActivity();

        donationTab = homeContainerFragmentTlTab;

        if (navBar != null) {
            navBar.setVisibility(View.VISIBLE);
        }

        SharedPreferences onBackPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences(GO_TO_HOME_FILE, Context.MODE_PRIVATE);
        openFragmentWithBack = onBackPreferences.getBoolean(ON_BACK_HOME_VALUE, false);
        getFragGoFrom = onBackPreferences.getString(ON_RETURN_BACK_HOME_VALUE, null);

        ViewPagerWithFragmentAdapter adapter = new ViewPagerWithFragmentAdapter(getChildFragmentManager());
        postsAndFavoritesListFragment = new PostsAndFavoritesListFragment();
        donationsListFragment = new DonationsListFragment();

        donationsListFragment.homeContainerFragment = this;

        adapter.addPager(postsAndFavoritesListFragment, getResources().getString(R.string.frag_home_tl_articles));
        adapter.addPager(donationsListFragment, getString(R.string.frag_home_tl_donations));

        //homeContainerFragmentVpViewPager.setAdapter(adapter);
        homeContainerFragmentVpViewPager2.setAdapter(adapter);
        //homeContainerFragmentTlTab.setupWithViewPager(homeContainerFragmentVpViewPager);
        homeContainerFragmentTlTab.setupWithViewPager(homeContainerFragmentVpViewPager2);

        //donationsListFragment.homeContainerFragmentVpViewPager = homeContainerFragmentVpViewPager;
        donationsListFragment.homeContainerFragmentVpViewPager2 = homeContainerFragmentVpViewPager2;
        //homeContainerFragmentVpViewPager.setPagingEnabled(true);

        if (openFragmentWithBack) {
            if (getFragGoFrom.equals(FROM_DONATIONS)) {
                Objects.requireNonNull(homeContainerFragmentTlTab.getTabAt(1)).select();
            } else if (getFragGoFrom.equals(FROM_POSTS)) {
//                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) homeContainerFragmentLlContainer.getLayoutParams();
//                params.topMargin = HelperMethod.getStatusBarHeight(getActivity());
                //StatusBarUtil.hideFakeStatusBarView(getActivity());
                Objects.requireNonNull(homeContainerFragmentTlTab.getTabAt(0)).select();
            }
        } else {
            Objects.requireNonNull(homeContainerFragmentTlTab.getTabAt(0)).select();
        }

//        if (ViewCompat.getLayoutDirection(viewContainer) == ViewCompat.LAYOUT_DIRECTION_RTL) {
//            homeContainerFragmentVpViewPager2.setAllowedSwipeDirection(SwipeDirection.LEFT);
//        } else if (ViewCompat.getLayoutDirection(viewContainer) == ViewCompat.LAYOUT_DIRECTION_LTR) {
//            homeContainerFragmentVpViewPager2.setAllowedSwipeDirection(SwipeDirection.RIGHT);
//        }

        switch (homeContainerFragmentVpViewPager2.getCurrentItem()) {
            case 0:
                if (ViewCompat.getLayoutDirection(viewContainer) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                    homeContainerFragmentVpViewPager2.setAllowedSwipeDirection(SwipeDirection.LEFT);//LEFT
                } else if (ViewCompat.getLayoutDirection(viewContainer) == ViewCompat.LAYOUT_DIRECTION_LTR) {
                    homeContainerFragmentVpViewPager2.setAllowedSwipeDirection(SwipeDirection.RIGHT);//RIGHT
                }
                break;
            case 1:
                if (ViewCompat.getLayoutDirection(viewContainer) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                    homeContainerFragmentVpViewPager2.setAllowedSwipeDirection(SwipeDirection.RIGHT);//RIGHT
                } else if (ViewCompat.getLayoutDirection(viewContainer) == ViewCompat.LAYOUT_DIRECTION_LTR) {
                    homeContainerFragmentVpViewPager2.setAllowedSwipeDirection(SwipeDirection.LEFT);//LEFT
                }
                break;
            default: //
        }

        homeContainerFragmentVpViewPager2.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Toast.makeText(getActivity(), "onPageScrolled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageSelected(int position) {
                //Toast.makeText(getActivity(), "on         Page   \nSelected", Toast.LENGTH_SHORT).show();
                switch (position) {
                    case 0:
                        if (ViewCompat.getLayoutDirection(viewContainer) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                            homeContainerFragmentVpViewPager2.setAllowedSwipeDirection(SwipeDirection.LEFT);//LEFT
                        } else if (ViewCompat.getLayoutDirection(viewContainer) == ViewCompat.LAYOUT_DIRECTION_LTR) {
                            homeContainerFragmentVpViewPager2.setAllowedSwipeDirection(SwipeDirection.RIGHT);//RIGHT
                        }
                        break;
                    case 1:
                        if (ViewCompat.getLayoutDirection(viewContainer) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                            homeContainerFragmentVpViewPager2.setAllowedSwipeDirection(SwipeDirection.RIGHT);//RIGHT
                        } else if (ViewCompat.getLayoutDirection(viewContainer) == ViewCompat.LAYOUT_DIRECTION_LTR) {
                            homeContainerFragmentVpViewPager2.setAllowedSwipeDirection(SwipeDirection.LEFT);//LEFT
                        }
                        break;
                    default: //
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Toast.makeText(getActivity(), "onPageScrollStateChanged", Toast.LENGTH_SHORT).show();
            }
        });


        //homeCycleActivity.setNavigation(View.VISIBLE, R.id.home_cycle_activity_rb_home);
        //homeCycleActivity.setToolBar(View.GONE, null, null);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

//    @Override
//    public void onBack() {
//        if (getActivity() != null) {
//            getActivity().moveTaskToBack(true);
//            getActivity().finish();
//        }
//    }
}
