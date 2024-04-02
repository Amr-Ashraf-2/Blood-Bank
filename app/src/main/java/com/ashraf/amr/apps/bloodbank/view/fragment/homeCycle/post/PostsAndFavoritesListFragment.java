package com.ashraf.amr.apps.bloodbank.view.fragment.homeCycle.post;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ashraf.amr.apps.bloodbank.R;
import com.ashraf.amr.apps.bloodbank.adapter.PostsAndFavouritesAdapter;
import com.ashraf.amr.apps.bloodbank.adapter.SpinnerAdapter;
import com.ashraf.amr.apps.bloodbank.data.model.client.ClientData;
import com.ashraf.amr.apps.bloodbank.data.model.post.posts.Posts;
import com.ashraf.amr.apps.bloodbank.data.model.post.posts.PostsData;
import com.ashraf.amr.apps.bloodbank.utils.OnEndLess;
import com.ashraf.amr.apps.bloodbank.view.fragment.BaseFragment;
import com.ashraf.amr.apps.bloodbank.view.fragment.homeCycle.MoreFragment;
import com.ashraf.amr.apps.bloodbank.view.fragment.homeCycle.donation.CreateDonationFragment;
import com.ashraf.amr.mirtoast.ToastCreator;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
import static com.ashraf.amr.apps.bloodbank.utils.Constants.FROM_POSTS;
import static com.ashraf.amr.apps.bloodbank.utils.Constants.GO_TO_HOME_FILE;
import static com.ashraf.amr.apps.bloodbank.utils.Constants.ON_BACK_HOME_VALUE;
import static com.ashraf.amr.apps.bloodbank.utils.Constants.ON_RETURN_BACK_HOME_VALUE;
import static com.ashraf.amr.apps.bloodbank.utils.GeneralRequest.getSpinnerData;
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.customToolbar;
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.disappearKeypad;
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.keyboardVisibility;
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.replaceFragment;
import static com.ashraf.amr.apps.bloodbank.utils.network.InternetState.isConnected;
import static com.ashraf.amr.apps.bloodbank.view.activity.HomeCycleActivity.navBar;
import static com.ashraf.amr.apps.bloodbank.view.activity.HomeCycleActivity.toolBar;

public class PostsAndFavoritesListFragment extends BaseFragment {

    @BindView(R.id.posts_favourites_list_Fragment_sp_categories)
    Spinner postsFavouritesListFragmentSpCategories;
    @BindView(R.id.posts_favourites_list_Fragment_iv_search)
    ImageView postsFavouritesListFragmentIvSearch;
    @BindView(R.id.Sfl_ShimmerFrameLayout)
    ShimmerFrameLayout SflShimmerFrameLayout;
    @BindView(R.id.posts_favourites_list_Fragment_rv_list)
    RecyclerView postsFavouritesListFragmentRvList;
    @BindView(R.id.load_more)
    RelativeLayout loadMore;
    @BindView(R.id.error_sub_view)
    LinearLayout errorSubView;
    @BindView(R.id.posts_favourites_list_Fragment_lin_toolbar)
    LinearLayout postsFavouritesListFragmentLinToolbar;
    @BindView(R.id.posts_favourites_list_Fragment_f_a_btn_create_donations)
    FloatingActionButton postsFavouritesListFragmentFABtnCreateDonations;
    private Unbinder unbinder;
    @BindView(R.id.posts_favourites_list_Fragment_sr_refresh_posts)
    SwipeRefreshLayout postsFavouritesListFragmentSrRefreshPosts;
    @BindView(R.id.error_image)
    ImageView errorImage;
    @BindView(R.id.error_title)
    TextView errorTitle;
    @BindView(R.id.error_action)
    TextView errorAction;
    @BindView(R.id.posts_favourites_list_Fragment_et_keyword)
    EditText postsFavouritesListFragmentEtKeyword;
    @BindView(R.id.posts_favourites_list_Fragment_tv_no_results)
    TextView postsFavouritesListFragmentTvNoResults;

    private ClientData clientData;
    private SpinnerAdapter categoriesAdapter;
    private LinearLayoutManager linearLayout;
    private Integer maxPage = 0;
    private OnEndLess onEndLess;
    private boolean Filter = false;
    private String keyword = "";
    public boolean favourites = false;
    public List<PostsData> postsData = new ArrayList<>();
    public PostsAndFavouritesAdapter postsAndFavouritesAdapter;


    public PostsAndFavoritesListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_posts_favourites_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        clientData = loadUserData(getActivity());
        setUpActivity();

        List<String> spinnerArray = new ArrayList<>();
        spinnerArray.add(getString(R.string.select_category));
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                Objects.requireNonNull(getActivity()), R.layout.items_custom_spinner2 , spinnerArray);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        postsFavouritesListFragmentSpCategories.setAdapter(spinnerAdapter);

        if (favourites) {
            postsFavouritesListFragmentLinToolbar.setVisibility(View.GONE);
            postsFavouritesListFragmentFABtnCreateDonations.hide();

            toolBar.setVisibility(View.VISIBLE);
            customToolbar(true, getString(R.string.favourites), true, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBack();
                }
            });

            if (navBar != null) {
                navBar.setVisibility(View.GONE);
            }

        } else {

            postsFavouritesListFragmentFABtnCreateDonations.show();

            toolBar.setVisibility(View.GONE);

            if (navBar != null) {
                navBar.setVisibility(View.VISIBLE);
            }

            keyboardVisibility(view, getActivity());

        }
        categoriesAdapter = new SpinnerAdapter(getActivity(), getResources().getDimension(R.dimen._12ssp));
        getSpinnerData(getActivity(), postsFavouritesListFragmentSpCategories, categoriesAdapter, getString(R.string.select_category),
                getClient().getCategories(), null, 0, false);

        init();


        return view;
    }

    private void init() {
        linearLayout = new LinearLayoutManager(getActivity());
        postsFavouritesListFragmentRvList.setLayoutManager(linearLayout);

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
                            getPosts(current_page);
                        }

                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                } else {
                    onEndLess.current_page = onEndLess.previous_page;
                }
            }
        };
        postsFavouritesListFragmentRvList.addOnScrollListener(onEndLess);

        postsAndFavouritesAdapter = new PostsAndFavouritesAdapter(getActivity(), getActivity(), postsData, favourites);
        postsFavouritesListFragmentRvList.setAdapter(postsAndFavouritesAdapter);

        getPosts(1);

        postsFavouritesListFragmentSrRefreshPosts.setOnRefreshListener(() -> {

            if (Filter) {
                onFilter(1);
            } else {
                getPosts(1);
            }

        });
    }

    private void getPosts(int page) {
        Call<Posts> call;

        if (!favourites) {
            call = getClient().getPosts(clientData.getApiToken(), page);
        } else {
            call = getClient().getFavouritesList(clientData.getApiToken(), page);
        }

        startCall(call, page);
    }

    private void onFilter(int page) {
        keyword = postsFavouritesListFragmentEtKeyword.getText().toString().trim();

        Filter = true;

        Call<Posts> call = getClient().getPostFilter(
                clientData.getApiToken(), keyword, page, categoriesAdapter.selectedId);

        startCall(call, page);
    }

    private void startCall(Call<Posts> call, int page) {
        errorSubView.setVisibility(View.GONE);
        if (page == 1) {
            reInit();
            SflShimmerFrameLayout.startShimmer();
            SflShimmerFrameLayout.setVisibility(View.VISIBLE);
            postsFavouritesListFragmentTvNoResults.setVisibility(View.GONE);

        }

        if (isConnected(getActivity())) {

            call.enqueue(new Callback<Posts>() {
                @Override
                public void onResponse(@NonNull Call<Posts> call, @NonNull Response<Posts> response) {
                    try {
                        SflShimmerFrameLayout.stopShimmer();
                        SflShimmerFrameLayout.setVisibility(View.GONE);
                        loadMore.setVisibility(View.GONE);
                        postsFavouritesListFragmentSrRefreshPosts.setRefreshing(false);
                        postsFavouritesListFragmentTvNoResults.setVisibility(View.GONE);

                        assert response.body() != null;
                        if (response.body().getStatus() == 1) {
                            maxPage = response.body().getData().getLastPage();

                            if (response.body().getData().getTotal() != 0) {
                                postsData.addAll(response.body().getData().getData());
                                postsAndFavouritesAdapter.notifyDataSetChanged();

                            } else {
                                postsFavouritesListFragmentTvNoResults.setVisibility(View.VISIBLE);
                            }


                        } else {
                            setError(getString(R.string.error_list));
                        }

                    } catch (Exception e) {
                       // ToastCreator.onCreateErrorToast(Objects.requireNonNull(getActivity()), e.getMessage());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Posts> call, @NonNull Throwable t) {
                    try {
                        SflShimmerFrameLayout.stopShimmer();
                        SflShimmerFrameLayout.setVisibility(View.GONE);
                        loadMore.setVisibility(View.GONE);
                        postsFavouritesListFragmentSrRefreshPosts.setRefreshing(false);
                        setError(getString(R.string.error_list));
                    } catch (Exception e) {
                       // ToastCreator.onCreateErrorToast(Objects.requireNonNull(getActivity()), e.getMessage());
                    }

                }
            });

        } else {
            try {
                SflShimmerFrameLayout.onDetachedFromWindow();
                SflShimmerFrameLayout.stopShimmer();
                SflShimmerFrameLayout.setVisibility(View.GONE);
                loadMore.setVisibility(View.GONE);
                postsFavouritesListFragmentSrRefreshPosts.setRefreshing(false);
                setError(getString(R.string.error_inter_net));
            } catch (Exception e) {
              //  ToastCreator.onCreateErrorToast(Objects.requireNonNull(getActivity()), e.getMessage());
            }

        }

    }


    private void reInit() {
        onEndLess.previousTotal = 0;
        onEndLess.current_page = 1;
        onEndLess.previous_page = 1;
        postsData = new ArrayList<>();
        postsAndFavouritesAdapter = new PostsAndFavouritesAdapter(getActivity(), getActivity(), postsData, favourites);
        postsFavouritesListFragmentRvList.setAdapter(postsAndFavouritesAdapter);
    }

    private void setError(String errorTitleTxt) {
        View.OnClickListener action = view -> {

            if (Filter) {
                onFilter(1);
            } else {
                getPosts(1);
            }

        };
        postsFavouritesListFragmentTvNoResults.setVisibility(View.VISIBLE);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.posts_favourites_list_Fragment_iv_search, R.id.posts_favourites_list_Fragment_f_a_btn_create_donations})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.posts_favourites_list_Fragment_iv_search:
                disappearKeypad(getActivity(), getView());
                if (categoriesAdapter.selectedId == 0 && keyword.equals("") && Filter) {
                    Filter = false;
                    getPosts(1);
                } else {
                    onFilter(1);
                }
                break;
            case R.id.posts_favourites_list_Fragment_f_a_btn_create_donations:

                Objects.requireNonNull(getActivity()).getSharedPreferences(GO_TO_HOME_FILE, MODE_PRIVATE)
                        .edit()
                        .putBoolean(ON_BACK_HOME_VALUE, true)
                        .putString(ON_RETURN_BACK_HOME_VALUE, FROM_POSTS)
                        .apply();

                replaceFragment(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, new CreateDonationFragment());
                break;
        }
    }

    @Override
    public void onBack() {
        //Toast.makeText(getActivity(), "back", Toast.LENGTH_SHORT).show();

        if (!favourites) {
            if (getActivity() != null) {
                getActivity().moveTaskToBack(true);
                getActivity().finish();
            }
        } else {
            replaceFragment(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame
                    , new MoreFragment());
        }
    }
}
