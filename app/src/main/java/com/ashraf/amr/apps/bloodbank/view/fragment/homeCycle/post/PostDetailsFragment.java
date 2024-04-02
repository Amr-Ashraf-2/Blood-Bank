package com.ashraf.amr.apps.bloodbank.view.fragment.homeCycle.post;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.ashraf.amr.apps.bloodbank.R;
import com.ashraf.amr.apps.bloodbank.data.model.client.ClientData;
import com.ashraf.amr.apps.bloodbank.data.model.post.posts.PostsData;
import com.ashraf.amr.apps.bloodbank.view.fragment.BaseFragment;
import com.ashraf.amr.apps.bloodbank.view.fragment.homeCycle.HomeContainerFragment;
import com.ashraf.amr.apps.bloodbank.view.fragment.homeCycle.MoreFragment;
import com.jaeger.library.StatusBarUtil;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.ashraf.amr.apps.bloodbank.data.local.SharedPreferencesManger.loadUserData;
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.onLoadImageFromUrl;
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.replaceFragment;
import static com.ashraf.amr.apps.bloodbank.view.activity.HomeCycleActivity.navBar;
import static com.ashraf.amr.apps.bloodbank.view.activity.HomeCycleActivity.toolBar;


public class PostDetailsFragment extends BaseFragment {

    public PostsData postsData;
    @BindView(R.id.fragment_display_post_details_toolbar)
    Toolbar fragmentDisplayPostDetailsToolbar;
    @BindView(R.id.fragment_display_post_details_img_v_image)
    ImageView fragmentDisplayPostDetailsImgVImage;
    @BindView(R.id.fragment_display_post_details_img_v_favorite)
    ImageView fragmentDisplayPostDetailsImgVFavorite;
    @BindView(R.id.fragment_display_post_details_tv_title)
    TextView fragmentDisplayPostDetailsTvTitle;
    @BindView(R.id.post_details_fragment_tv_description)
    TextView fragmentDisplayPostDetailsTvDescription;
    @BindView(R.id.toolbar_back_posts)
    RelativeLayout toolbarBackPosts;
    private Unbinder unbinder;
    private ClientData client;

    public PostDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_details, container, false);
        setUpActivity();
        unbinder = ButterKnife.bind(this, view);
        StatusBarUtil.setTranslucentForImageViewInFragment(getActivity(), 0, null);
        toolBar.setVisibility(View.GONE);

        toolbarBackPosts.setOnClickListener(v -> onBack());



//        fragmentDisplayPostDetailsToolbar.setNavigationIcon(R.drawable.ic_back_arrow);
//        fragmentDisplayPostDetailsToolbar.setNavigationOnClickListener(v -> {
//            if (getActivity() != null) {
//                onBack();
//            }
//        });

        client = loadUserData(getActivity());
        getPostDetails();


        if (navBar != null) {
            navBar.setVisibility(View.GONE);
        }


        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private void getPostDetails() {
        try {
            onLoadImageFromUrl(fragmentDisplayPostDetailsImgVImage, postsData.getThumbnailFullPath(), getActivity());
            fragmentDisplayPostDetailsTvTitle.setText(postsData.getTitle());
            fragmentDisplayPostDetailsTvDescription.setText(postsData.getContent());

            if (postsData.getIsFavourite()) {
                fragmentDisplayPostDetailsImgVFavorite.setImageResource(R.drawable.ic_heart_solid);

            } else {
                fragmentDisplayPostDetailsImgVFavorite.setImageResource(R.drawable.ic_heart_regular);

            }
        } catch (Exception e) {
           // Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBack() {
        Objects.requireNonNull(getActivity()).getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        Objects.requireNonNull(getActivity()).getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        Objects.requireNonNull(getActivity()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        super.onBack();
        ///replaceFragment(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, new HomeContainerFragment());
    }


}
