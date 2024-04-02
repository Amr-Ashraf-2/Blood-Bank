package com.ashraf.amr.apps.bloodbank.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;


import com.ashraf.amr.apps.bloodbank.R;
import com.ashraf.amr.apps.bloodbank.data.api.ApiService;
import com.ashraf.amr.apps.bloodbank.view.activity.UserCycleActivity;

import java.util.Objects;

import static com.ashraf.amr.apps.bloodbank.data.local.SharedPreferencesManger.clean;


public class LogOutDialog {
    private ApiService apiService;

    public void showDialog(final Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setContentView(R.layout.log_out_dialog);
        dialog.setCanceledOnTouchOutside(true);
        TextView text = (TextView) dialog.findViewById(R.id.text);

        ImageView dialogImageOk = (ImageView) dialog.findViewById(R.id.dialog_ok);
        dialogImageOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Call

                clean(activity);

                Intent i = new Intent(activity, UserCycleActivity.class);

                activity.startActivity(i);
                // close this activity
                activity.finish();
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        ImageView dialogImageNo = (ImageView) dialog.findViewById(R.id.dialog_no);
        dialogImageNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });


        dialog.show();

    }
}
