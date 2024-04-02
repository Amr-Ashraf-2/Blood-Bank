package com.ashraf.amr.apps.bloodbank.view.fragment.splashCycle;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.ashraf.amr.apps.bloodbank.adapter.SliderAdapter;
import com.jaeger.library.StatusBarUtil;
import com.ashraf.amr.apps.bloodbank.R;
import com.ashraf.amr.apps.bloodbank.view.activity.UserCycleActivity;
import com.ashraf.amr.apps.bloodbank.view.fragment.BaseFragment;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class SliderFragment extends BaseFragment {


    @BindView(R.id.slider_fragment_vp_slider)
    ViewPager sliderFragmentVpSlider;
    private Unbinder unbinder;
    @BindView(R.id.fragment_i_v_slider_circle1)
    ImageView fragmentIVSliderCircle1;
    @BindView(R.id.fragment_i_v_slider_circle2)
    ImageView fragmentIVSliderCircle2;
    @BindView(R.id.fragment_i_v_slider_circle3)
    ImageView fragmentIVSliderCircle3;
    @BindView(R.id.fragment_slider_i_btn_slider)
    ImageButton fragmentSliderIBtnSlider;

    public SliderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_slider, container, false);
        unbinder = ButterKnife.bind(this, view);
        //StatusBarUtil.setTranslucent(getActivity());
        StatusBarUtil.setColorNoTranslucent(getActivity(),getResources().getColor(R.color.frag_slider_status_bar_color));


//        View.OnClickListener action = view1 -> {
//            int currentItem = sliderFragmentVpSlider.getCurrentItem();
//            if (currentItem == sliderCreator.images.size() - 1) {
//                Intent intent = new Intent(getActivity(), UserCycleActivity.class);
//                Objects.requireNonNull(getActivity()).startActivity(intent);
//                getActivity().finish();
//            } else {
//                sliderFragmentVpSlider.setCurrentItem(currentItem + 1);
//            }
//
//        };

//        sliderCreator = new SliderCreator(Objects.requireNonNull(getActivity()), action, R.drawable.ic_next, R.drawable.shape_active
//                , R.drawable.shape_an_active);
//
//        sliderCreator.addPage(R.drawable.ic_slide2, getString(R.string.item_slider_tv_text));//slider1
//        sliderCreator.addPage(R.drawable.ic_slide2, getString(R.string.item_slider_tv_text));//slider1
//        sliderCreator.addPage(R.drawable.ic_slide3, getString(R.string.item_slider_tv_text));//slider1
//
//        sliderFragmentVpSlider.setAdapter(sliderCreator);

        if(getActivity()!=null){
            SliderAdapter sliderAdapter = new SliderAdapter(getActivity());
            sliderAdapter.addPage(R.drawable.ic_slide2, getString(R.string.item_slider_tv_text));
            sliderAdapter.addPage(R.drawable.ic_slide3, getString(R.string.item_slider_tv_text));
            sliderAdapter.addPage(R.drawable.ic_slide2, getString(R.string.item_slider_tv_text));
            sliderFragmentVpSlider.setAdapter(sliderAdapter);
        }

        sliderFragmentVpSlider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (sliderFragmentVpSlider.getCurrentItem()) {
                    case 0:
                        fragmentIVSliderCircle1.setImageResource(R.drawable.ic_red_circle);
                        fragmentIVSliderCircle2.setImageResource(R.drawable.ic_orange_circle);
                        fragmentIVSliderCircle3.setImageResource(R.drawable.ic_orange_circle);
                        fragmentSliderIBtnSlider.setImageResource(R.drawable.ic_slider_arrow_btn);
                        break;
                    case 1:
                        fragmentIVSliderCircle1.setImageResource(R.drawable.ic_red_circle);
                        fragmentIVSliderCircle2.setImageResource(R.drawable.ic_red_circle);
                        fragmentIVSliderCircle3.setImageResource(R.drawable.ic_orange_circle);
                        fragmentSliderIBtnSlider.setImageResource(R.drawable.ic_slider_arrow_btn);
                        break;
                    case 2:
                        fragmentIVSliderCircle1.setImageResource(R.drawable.ic_red_circle);
                        fragmentIVSliderCircle2.setImageResource(R.drawable.ic_red_circle);
                        fragmentIVSliderCircle3.setImageResource(R.drawable.ic_red_circle);
                        fragmentSliderIBtnSlider.setImageResource(R.drawable.ic_slider_ok_btn);
                        break;
                    default:
                        //
                }
            }
        });

        fragmentSliderIBtnSlider.setOnClickListener(v -> {
            switch (sliderFragmentVpSlider.getCurrentItem()) {
                case 0:
                    sliderFragmentVpSlider.setCurrentItem(1);
                    break;
                case 1:
                    sliderFragmentVpSlider.setCurrentItem(2);
                    break;
                case 2:
                    // Go To Next Cycle // Login Page
                    startActivity(new Intent(getActivity(), UserCycleActivity.class));
                    break;
                default:
                    //
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onBack() {
        //Objects.requireNonNull(getActivity()).finish();
        if(getActivity()!=null){
            getActivity().moveTaskToBack(true);
            getActivity().finish();
        }
    }
}
