package com.ashraf.amr.apps.bloodbank.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ashraf.amr.apps.bloodbank.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.ashraf.amr.apps.bloodbank.data.model.DateTxt;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import static com.ashraf.amr.apps.bloodbank.view.activity.HomeCycleActivity.navBar;
import static com.ashraf.amr.apps.bloodbank.view.activity.HomeCycleActivity.toolBarBack;
import static com.ashraf.amr.apps.bloodbank.view.activity.HomeCycleActivity.toolBarTitle;

public class HelperMethod {
    public static ProgressDialog progressDialog;
    public static AlertDialog alertDialog;
    private static boolean isKeyboardShowing;
    private static Animation slide_up, slide_down;

    public static void replaceFragment(FragmentManager getChildFragmentManager, int id, Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager.beginTransaction();
        transaction.replace(id, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void showCalender(Context context, String title, final TextView text_view_data, final DateTxt data1) { //THEME_HOLO_DARK
        DatePickerDialog mDatePicker = new DatePickerDialog(context, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedYear, int selectedMonth, int selectedDay) {
                DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
                DecimalFormat mFormat = new DecimalFormat("00", symbols);
                String data = selectedYear + "-" + mFormat.format(Double.valueOf((selectedMonth + 1))) + "-" + mFormat.format(Double.valueOf(selectedDay));
                data1.setDate_txt(data);
                data1.setDay(mFormat.format(Double.valueOf(selectedDay)));
                data1.setMonth(mFormat.format(Double.valueOf(selectedMonth + 1)));
                data1.setYear(String.valueOf(selectedYear));
                text_view_data.setText(data);
            }
        }, Integer.parseInt(data1.getYear()), Integer.parseInt(data1.getMonth()) - 1, Integer.parseInt(data1.getDay()));
        mDatePicker.setTitle(title);
        mDatePicker.show();
    }

    public static Date convertDateString(String date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            Date parse = format.parse(date);

            return parse;

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static DateTxt convertStringToDateTxtModel(String date) {
        try {
            Date date1 = convertDateString(date);
            String day = (String) DateFormat.format("dd", date1); // 20
            String monthNumber = (String) DateFormat.format("MM", date1); // 06
            String year = (String) DateFormat.format("yyyy", date1); // 2013

            return new DateTxt(day, monthNumber, year, date);

        } catch (Exception e) {
            return null;
        }
    }

    public static void onLoadImageFromUrl(ImageView imageView, String URl, Context context) {
        Glide.with(context)
                .load(URl)
                .into(imageView);
    }

    public static void setInitRecyclerViewAsLinearLayoutManager(Context context, RecyclerView recyclerView, LinearLayoutManager linearLayoutManager) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public static void setInitRecyclerViewAsGridLayoutManager(Activity activity, RecyclerView recyclerView, GridLayoutManager gridLayoutManager, int numberOfColumns) {
        gridLayoutManager = new GridLayoutManager(activity, numberOfColumns);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    public static void showProgressDialog(Activity activity, String title) {
        try {

            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage(title);
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);

            progressDialog.show();

        } catch (Exception e) {

        }
    }

    public static void dismissProgressDialog() {
        try {

            progressDialog.dismiss();

        } catch (Exception e) {

        }
    }

    public static void disappearKeypad(Activity activity, View v) {
        try {
            if (v != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        } catch (Exception e) {

        }
    }

    public static void changeLang(Context context, String lang) {
        Resources res = context.getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(lang)); // API 17+ only.
        // Use conf.locale = new Locale(...) if targeting lower versions
        res.updateConfiguration(conf, dm);
    }

    public static void htmlReader(TextView textView, String s) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(s, Html.FROM_HTML_MODE_COMPACT));
        } else {
            textView.setText(Html.fromHtml(s));
        }
    }

//    public static void customToast(Activity activity, String ToastTitle, boolean failed) {
//
//        LayoutInflater inflater = activity.getLayoutInflater();
//
//        int layout_id;
//
//        if (failed) {
//            layout_id = R.layout.toast;
//        } else {
//            layout_id = R.layout.success_toast;
//        }
//
//        View layout = inflater.inflate(layout_id,
//                (ViewGroup) activity.findViewById(R.id.toast_layout_root));
//
//        TextView text = layout.findViewById(R.id.text);
//        text.setText(ToastTitle);
//
//        Toast toast = new Toast(activity);
//        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 100);
//        toast.setDuration(Toast.LENGTH_LONG);
//        toast.setView(layout);
//        toast.show();
//    }

    public static void onPermission(Activity activity) {
        String[] perms = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CALL_PHONE};

        ActivityCompat.requestPermissions(activity,
                perms,
                100);

    }

    public static boolean checkWriteExternalPermission(Context context) {
        boolean check = false;
        String permission1 = Manifest.permission.ACCESS_FINE_LOCATION;
        int res1 = context.checkCallingOrSelfPermission(permission1);
        if (res1 != PackageManager.PERMISSION_GRANTED) {
            check = true;
        }
//
//        String permission2 = android.Manifest.permission.READ_CONTACTS;
//        int res2 = context.checkCallingOrSelfPermission(permission2);
//        if (res2 != PackageManager.PERMISSION_GRANTED) {
//            check = true;
//        }

        String permission3 = Manifest.permission.READ_EXTERNAL_STORAGE;
        int res3 = context.checkCallingOrSelfPermission(permission3);
        if (res3 != PackageManager.PERMISSION_GRANTED) {
            check = true;
        }

        String permission4 = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        int res4 = context.checkCallingOrSelfPermission(permission4);
        if (res4 != PackageManager.PERMISSION_GRANTED) {
            check = true;
        }

//        String permission5 = android.Manifest.permission.READ_PHONE_STATE;
//        int res5 = context.checkCallingOrSelfPermission(permission5);
//        if (res5 != PackageManager.PERMISSION_GRANTED) {
//            check = true;
//        }

        String permission6 = Manifest.permission.CALL_PHONE;
        int res6 = context.checkCallingOrSelfPermission(permission6);
        if (res6 != PackageManager.PERMISSION_GRANTED) {
            check = true;
        }

        return check;
    }


//    public static void changeLang(Context context) {
//        Resources res = context.getResources();
//        // Change locale settings in the app.
//        DisplayMetrics dm = res.getDisplayMetrics();
//        android.content.res.Configuration conf = res.getConfiguration();
//        conf.setLocale(new Locale(LoadStringData((Activity) context, USER_LANG))); // API 17+ only.
//        // Use conf.locale = new Locale(...) if targeting lower versions
//        res.updateConfiguration(conf, dm);
//    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(40, 20, vectorDrawable.getIntrinsicWidth() + 40, vectorDrawable.getIntrinsicHeight() + 20);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public static int getNavigationBarHeight(Activity activity)
    {
        boolean hasMenuKey = ViewConfiguration.get(activity).hasPermanentMenuKey();
        int resourceId = activity.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0 && !hasMenuKey)
        {
            return activity.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static int getStatusBarHeight(Activity activity) {
        final Resources resources = activity.getResources();
        final int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
            return resources.getDimensionPixelSize(resourceId);
        else
            return (int) Math.ceil((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? 24 : 25) * resources.getDisplayMetrics().density);
    }

    public static void closeKeypad(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public static void customToolbar(Boolean titleVisibility, String title,
                                     Boolean backBtn, View.OnClickListener onClickListener){
        if (toolBarTitle!=null){
            if(titleVisibility){
                toolBarTitle.setVisibility(View.VISIBLE);
                toolBarTitle.setText(title);
            }else {
                toolBarTitle.setText("");
                toolBarTitle.setVisibility(View.GONE);
            }
        }

        if (toolBarBack!=null){
            if (backBtn){
                toolBarBack.setVisibility(View.VISIBLE);
                toolBarBack.setOnClickListener(onClickListener);
            }else {
                toolBarBack.setVisibility(View.GONE);
            }
        }
    }




//    public static void keyboardVisibility(View contentView){
//        isKeyboardShowing = false;
//        // ContentView is the root view of the layout of this activity/fragment
//        contentView.getViewTreeObserver().addOnGlobalLayoutListener(
//                new ViewTreeObserver.OnGlobalLayoutListener() {
//                    @Override
//                    public void onGlobalLayout() {
//
//                        Rect r = new Rect();
//                        contentView.getWindowVisibleDisplayFrame(r);
//                        int screenHeight = contentView.getRootView().getHeight();
//
//                        // r.bottom is the position above soft keypad or device button.
//                        // if keypad is shown, the r.bottom is smaller than that before.
//                        int keypadHeight = screenHeight - r.bottom;
//
//                        //Log.d(TAG, "keypadHeight = " + keypadHeight);
//
//                        if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
//                            // keyboard is opened
//                            if (!isKeyboardShowing) {
//                                isKeyboardShowing = true;
//                                onKeyboardVisibilityChanged(true);
//                            }
//                        }
//                        else {
//                            // keyboard is closed
//                            if (isKeyboardShowing) {
//                                isKeyboardShowing = false;
//                                onKeyboardVisibilityChanged(false);
//                            }
//                        }
//                    }
//                });
//    }
//
//    private static void onKeyboardVisibilityChanged(boolean opened) {
//        if (navBar != null){
//            if (opened){
//                navBar.setVisibility(View.GONE);
//            }else {
//                navBar.setVisibility(View.VISIBLE);
//            }
//        }
//    }


    public static void keyboardVisibility(View contentView,Activity context){
        isKeyboardShowing = false;
        // ContentView is the root view of the layout of this activity/fragment
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        Rect r = new Rect();
                        contentView.getWindowVisibleDisplayFrame(r);
                        int screenHeight = contentView.getRootView().getHeight();

                        // r.bottom is the position above soft keypad or device button.
                        // if keypad is shown, the r.bottom is smaller than that before.
                        int keypadHeight = screenHeight - r.bottom;

                        //Log.d(TAG, "keypadHeight = " + keypadHeight);

                        if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                            // keyboard is opened
                            if (!isKeyboardShowing) {
                                isKeyboardShowing = true;
                                onKeyboardVisibilityChanged(true, context);
                            }
                        }
                        else {
                            // keyboard is closed
                            if (isKeyboardShowing) {
                                isKeyboardShowing = false;
                                onKeyboardVisibilityChanged(false, context);
                            }
                        }
                    }
                });
    }

    private static void onKeyboardVisibilityChanged(boolean opened, Activity context) {
        if (navBar != null){
            if (opened){
                slide_down = AnimationUtils.loadAnimation(context,R.anim.slide_down);
                navBar.startAnimation(slide_down);
                navBar.setVisibility(View.GONE);
                //Toast.makeText(context, "slide down", Toast.LENGTH_SHORT).show();
            }else {
                slide_up = AnimationUtils.loadAnimation(context,R.anim.slide_up);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        navBar.startAnimation(slide_up);
                        navBar.setVisibility(View.VISIBLE);
                    }
                }, 5);
            }
        }
    }



    public static void createAlertDialog(Activity activity, FragmentManager fragmentManager, int id,Fragment fragment){
        new AlertDialog.Builder(activity)
                .setTitle("Connection Failed")
                .setMessage("Please Check Your Internet Connection")
//                .setIcon(R.drawable.ic_internet_connection)
                .setCancelable(false)
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //Intent i = new Intent(ShowData.this, ShowData.class);
                        //finish();
                        activity.overridePendingTransition(0,0);
                        //startActivity(i);
                        replaceFragment(fragmentManager,id,fragment);
                        activity.overridePendingTransition(0,0);
                    }
                })
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finishAndRemoveTask();
                    }
                })
                .create()
                .show();
    }

    public static int random() {
        Random generator = new Random();
        return generator.nextInt(98765 - 10478 + 1) + 10478;
    }


}
