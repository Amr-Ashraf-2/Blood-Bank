package com.ashraf.amr.apps.bloodbank.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.ashraf.amr.apps.bloodbank.R;
import com.ashraf.amr.apps.bloodbank.data.model.client.ClientData;
import com.ashraf.amr.apps.bloodbank.data.model.donation.donationRequests.DonationData;
import com.ashraf.amr.apps.bloodbank.view.activity.BaseActivity;
import com.ashraf.amr.apps.bloodbank.view.fragment.homeCycle.donation.DonationDetailsFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;
import static com.ashraf.amr.apps.bloodbank.data.local.SharedPreferencesManger.loadUserData;
import static com.ashraf.amr.apps.bloodbank.utils.Constants.FROM_DONATIONS;
import static com.ashraf.amr.apps.bloodbank.utils.Constants.GO_TO_HOME_FILE;
import static com.ashraf.amr.apps.bloodbank.utils.Constants.ON_BACK_HOME_VALUE;
import static com.ashraf.amr.apps.bloodbank.utils.Constants.ON_RETURN_BACK_HOME_VALUE;
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.onPermission;
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.replaceFragment;

public class DonationAdapter extends RecyclerView.Adapter<DonationAdapter.ViewHolder> {

    private Context context;
    private BaseActivity activity;
    private List<DonationData> donationDataList = new ArrayList<>();
    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private ClientData clientData;
    private String lang;
    private ViewGroup viewContainer;

    private static final String BUNDLE_MAP_KEY = "ViewBinderHelper_Bundle_Map_Key";

    private Map<String, Integer> mapStates = Collections.synchronizedMap(new HashMap<String, Integer>());
    private Map<String, SwipeRevealLayout> mapLayouts = Collections.synchronizedMap(new HashMap<String, SwipeRevealLayout>());
    private Set<String> lockedSwipeSet = Collections.synchronizedSet(new HashSet<String>());

    private volatile boolean openOnlyOne = false;
    private final Object stateChangeLock = new Object();

    public DonationAdapter(Activity activity, List<DonationData> restaurantDataList) {
        this.context = activity;
        this.activity = (BaseActivity) activity;
        this.donationDataList = restaurantDataList;
        viewBinderHelper.setOpenOnlyOne(true);
        clientData = loadUserData(activity);
        lang = "eg";
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_donation,
                parent, false);

        viewContainer = parent;

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData(holder, position);
        //setSwipe(holder, position);
    }

    @SuppressLint("SetTextI18n")
    private void setData(ViewHolder holder, int position) {

        holder.position = position;
        holder.donationAdapterTvName.setText(activity.getString(R.string.patient_name) + " : " +
                donationDataList.get(position).getClient().getName());

        holder.donationAdapterTvAddress.setText(activity.getString(R.string.hospital) + " : " +
                donationDataList.get(position).getHospitalAddress());

        holder.donationAdapterTvCity.setText(activity.getString(R.string.city) + " : " +
                donationDataList.get(position).getCity().getName());

        holder.donationAdapterTvBloodType.setText(donationDataList.get(position).getBloodType().getName());

        viewBinderHelper.bind(holder.donationAdapterPsIvDonationSwipeLayout, String.valueOf(holder.hashCode()));
        viewBinderHelper.setOpenOnlyOne(true);

        if(ViewCompat.getLayoutDirection(viewContainer) == ViewCompat.LAYOUT_DIRECTION_RTL){
            holder.donationAdapterPsIvDonationSwipeLayout.setDragEdge(SwipeRevealLayout.DRAG_EDGE_LEFT);
        }else if (ViewCompat.getLayoutDirection(viewContainer) == ViewCompat.LAYOUT_DIRECTION_LTR){
            holder.donationAdapterPsIvDonationSwipeLayout.setDragEdge(SwipeRevealLayout.DRAG_EDGE_RIGHT);
        }

    }


//    private void setSwipe(final ViewHolder holder, final int position) {
//        holder.donationAdapterPsIvDonationSwipeLayout.computeScroll();
//        if (lang.equals("ar")) {
//            holder.donationAdapterPsIvDonationSwipeLayout.setDragEdge(SwipeRevealLayout.DRAG_EDGE_LEFT);
//        } else {
//            holder.donationAdapterPsIvDonationSwipeLayout.setDragEdge(SwipeRevealLayout.DRAG_EDGE_RIGHT);
//        }
//
//        viewBinderHelper.bind(holder.donationAdapterPsIvDonationSwipeLayout, String.valueOf(donationDataList.get(position).getId()));
//
//        holder.view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                viewBinderHelper.openLayout(String.valueOf(donationDataList.get(position).getId()));
//                holder.donationAdapterPsIvDonationSwipeLayout.computeScroll();
//            }
//        });
//
//    }

    @Override
    public int getItemCount() {
        return donationDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.donation_adapter_tv_name)
        TextView donationAdapterTvName;
        @BindView(R.id.donation_adapter_tv_address)
        TextView donationAdapterTvAddress;
        @BindView(R.id.donation_adapter_tv_city)
        TextView donationAdapterTvCity;
        @BindView(R.id.donation_adapter_tv_blood_type)
        TextView donationAdapterTvBloodType;
        @BindView(R.id.donation_adapter_ps_iv_donation_swipe_layout)
        SwipeRevealLayout donationAdapterPsIvDonationSwipeLayout;

        private View view;
        private int position;

        private ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.donation_adapter_rl_info, R.id.donation_adapter_rl_call})
        public void onViewClicked(View view) {
            switch (view.getId()) {
                case R.id.donation_adapter_rl_info:

                    activity.getSharedPreferences(GO_TO_HOME_FILE, MODE_PRIVATE)
                            .edit()
                            .putBoolean(ON_BACK_HOME_VALUE, true)
                            .putString(ON_RETURN_BACK_HOME_VALUE, FROM_DONATIONS)
                            .apply();

                    DonationDetailsFragment donationDetailsFragment = new DonationDetailsFragment();
                    donationDetailsFragment.donationId = donationDataList.get(position).getId();
                    replaceFragment(activity.getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, donationDetailsFragment);

                    break;
                case R.id.donation_adapter_rl_call:
                    onPermission(activity);

                    activity.startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", donationDataList.get(position).getPhone(), null)));
                    break;
            }
        }
    }
}
