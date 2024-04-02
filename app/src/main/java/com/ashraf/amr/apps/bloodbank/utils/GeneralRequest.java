package com.ashraf.amr.apps.bloodbank.utils;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ashraf.amr.apps.bloodbank.R;
import com.ashraf.amr.apps.bloodbank.adapter.SpinnerAdapter;
import com.ashraf.amr.apps.bloodbank.data.model.client.Client;
import com.ashraf.amr.apps.bloodbank.data.model.generalResponse.GeneralResponse;
import com.ashraf.amr.apps.bloodbank.view.activity.HomeCycleActivity;
import com.ashraf.amr.mirtoast.ToastCreator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ashraf.amr.apps.bloodbank.data.local.SharedPreferencesManger.REMEMBER;
import static com.ashraf.amr.apps.bloodbank.data.local.SharedPreferencesManger.SaveData;
import static com.ashraf.amr.apps.bloodbank.data.local.SharedPreferencesManger.USER_PASSWORD;
import static com.ashraf.amr.apps.bloodbank.data.local.SharedPreferencesManger.saveUserData;
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.progressDialog;
import static com.ashraf.amr.apps.bloodbank.utils.network.InternetState.isConnected;

public class GeneralRequest {

    public static void getSpinnerData(Activity activity, final Spinner spinner, final SpinnerAdapter adapter, final String hint,
                                      Call<GeneralResponse> method, final View view, final int selectedId, final boolean show) {

        if (show) {
            if (progressDialog == null) {
                HelperMethod.showProgressDialog(activity, activity.getString(R.string.wait));
            } else {
                if (!progressDialog.isShowing()) {
                    HelperMethod.showProgressDialog(activity, activity.getString(R.string.wait));
                }
            }
        }


        method.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(@NonNull Call<GeneralResponse> call, @NonNull Response<GeneralResponse> response) {
                try {
                    if (show) {
                        HelperMethod.dismissProgressDialog();
                    }

                    assert response.body() != null;
                    if (response.body().getStatus() == 1) {
                        if (view != null) {
                            view.setVisibility(View.VISIBLE);
                        }
                        adapter.setData(response.body().getData(), hint);

                        spinner.setAdapter(adapter);

                        spinner.setSelection(selectedId);


                    }
                } catch (Exception e) {
                    //ToastCreator.onCreateErrorToast(activity,e.getMessage());
                }

            }

            @Override
            public void onFailure(@NonNull Call<GeneralResponse> call, @NonNull Throwable t) {
                if (show) {
                    HelperMethod.dismissProgressDialog();
                }

            }
        });
    }

    public static void getSpinnerData(final Activity activity, final Spinner spinner, final SpinnerAdapter adapter, final String hint
            , final Call<GeneralResponse> method, final int selectedId1, final AdapterView.OnItemSelectedListener listener) {

        if (progressDialog == null) {
            HelperMethod.showProgressDialog(activity, activity.getString(R.string.wait));
        } else {
            if (!progressDialog.isShowing()) {
                HelperMethod.showProgressDialog(activity, activity.getString(R.string.wait));
            }
        }

        method.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(@NonNull Call<GeneralResponse> call, @NonNull Response<GeneralResponse> response) {
                try {

                    HelperMethod.dismissProgressDialog();
                    assert response.body() != null;
                    if (response.body().getStatus() == 1) {

                        adapter.setData(response.body().getData(), hint);

                        spinner.setAdapter(adapter);

                        spinner.setSelection(selectedId1);

                        spinner.setOnItemSelectedListener(listener);

                    }

                } catch (Exception e) {
                   // ToastCreator.onCreateErrorToast(activity,e.getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<GeneralResponse> call, @NonNull Throwable t) {
                HelperMethod.dismissProgressDialog();
            }
        });
    }

    public static void userData(final Activity activity, Call<Client> method, final String password, final boolean remember, final boolean auth, String called) {

        if (isConnected(activity)) {

            if (progressDialog == null) {
                HelperMethod.showProgressDialog(activity, activity.getString(R.string.wait));
            } else {
                if (!progressDialog.isShowing()) {
                    HelperMethod.showProgressDialog(activity, activity.getString(R.string.wait));
                }
            }

            method.enqueue(new Callback<Client>() {
                @Override
                public void onResponse(@NonNull Call<Client> call, @NonNull Response<Client> response) {
                    try {
                        HelperMethod.dismissProgressDialog();
                        assert response.body() != null;
                        if (response.body().getStatus() == 1) {
                            //Toast.makeText(activity, "getStatus = 1", Toast.LENGTH_SHORT).show();
                            SaveData(activity, USER_PASSWORD, password);
                            saveUserData(activity, response.body().getData());
                            if (auth) {
                                SaveData(activity, REMEMBER, remember);
                                Intent intent = new Intent(activity, HomeCycleActivity.class);
                                activity.startActivity(intent);
                                activity.finish();
                            }

                            if (called.equalsIgnoreCase("Register")){
                                ToastCreator.onCreateSuccessToast(activity,activity.getString(R.string.registered));
                            }else if (called.equalsIgnoreCase("Update")){
                                ToastCreator.onCreateSuccessToast(activity,activity.getString(R.string.updated));
                            }else if (called.equalsIgnoreCase("Login")){
                            }

                        }else {
                            Toast.makeText(activity, response.body().getMsg(), Toast.LENGTH_LONG).show();
                        }
                        //ToastCreator.onCreateErrorToast(activity, response.body().getMsg());
                    } catch (Exception e) {
                        //Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                        ToastCreator.onCreateErrorToast(activity,e.getMessage());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Client> call, @NonNull Throwable t) {
                    HelperMethod.dismissProgressDialog();
                    //Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                    ToastCreator.onCreateErrorToast(activity, activity.getString(R.string.error));
                }
            });

        } else {
            try {
                ToastCreator.onCreateErrorToast(activity, activity.getString(R.string.error_inter_net));
            } catch (Exception e) {
               // ToastCreator.onCreateErrorToast(activity,e.getMessage());
            }

        }

    }

}
