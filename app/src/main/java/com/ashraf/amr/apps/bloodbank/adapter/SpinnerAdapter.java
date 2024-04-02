package com.ashraf.amr.apps.bloodbank.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.view.ViewCompat;

import com.ashraf.amr.apps.bloodbank.R;
import com.ashraf.amr.apps.bloodbank.data.model.generalResponse.GeneralResponseData;

import java.util.ArrayList;
import java.util.List;

public class SpinnerAdapter extends BaseAdapter {

    private Context context;
    private List<GeneralResponseData> generalResponseDataList = new ArrayList<>();
    private LayoutInflater inflter;
    public int selectedId = 0;
    private float textSize = 0;

    public SpinnerAdapter(Context applicationContext) {
        this.context = applicationContext;
        inflter = (LayoutInflater.from(applicationContext));
    }

    public SpinnerAdapter(Context applicationContext, float textSize) {
        this.context = applicationContext;
        inflter = (LayoutInflater.from(applicationContext));
        this.textSize = textSize;
    }

    public void setData(List<GeneralResponseData> generalResponseDataList, String hint) {
        this.generalResponseDataList = new ArrayList<>();
        this.generalResponseDataList.add(new GeneralResponseData(0, hint));
        this.generalResponseDataList.addAll(generalResponseDataList);
    }

    @Override
    public int getCount() {
        return generalResponseDataList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = inflter.inflate(R.layout.items_custom_spinner, null);

        TextView names =  view.findViewById(R.id.item_spinner_tv_text);

        if (textSize != 0){
            names.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }

        if(ViewCompat.getLayoutDirection(viewGroup) == ViewCompat.LAYOUT_DIRECTION_RTL){
            names.setGravity(Gravity.RIGHT);
        }else if (ViewCompat.getLayoutDirection(viewGroup) == ViewCompat.LAYOUT_DIRECTION_LTR){
            names.setGravity(Gravity.LEFT);
        }

        names.setText(generalResponseDataList.get(i).getName());
        selectedId = generalResponseDataList.get(i).getId();

        return view;
    }
}