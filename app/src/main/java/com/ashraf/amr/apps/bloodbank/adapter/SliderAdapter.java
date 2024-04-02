package com.ashraf.amr.apps.bloodbank.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.ashraf.amr.apps.bloodbank.R;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;

    private List<Integer> images = new ArrayList<>();
    private List<String> texts = new ArrayList<>();


    public SliderAdapter(Context context){
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addPage(Integer image, String text){
        this.images.add(image);
        this.texts.add(text);
    }

    @Override
    public int getCount() {
        return this.images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        try{
            View itemView = layoutInflater.inflate(R.layout.item_slider,container,false);
            ImageView sliderAdapterIvSliderImage = itemView.findViewById(R.id.item_slider_img_v_image);
            TextView sliderAdapterTvSliderText = itemView.findViewById(R.id.item_slider_tv_text);
            sliderAdapterIvSliderImage.setImageResource(images.get(position));
            sliderAdapterTvSliderText.setText(texts.get(position));
            container.addView(itemView);
            return itemView;
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //super.destroyItem(container, position, object);
        container.removeView((View) object);
    }
}
