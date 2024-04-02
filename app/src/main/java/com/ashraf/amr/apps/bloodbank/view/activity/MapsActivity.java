package com.ashraf.amr.apps.bloodbank.view.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ashraf.amr.apps.bloodbank.R;
import com.ashraf.amr.apps.bloodbank.utils.GPSTracker;
import com.ashraf.amr.apps.bloodbank.utils.network.InternetState;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.location.Address;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ashraf.amr.apps.bloodbank.utils.Constants.ACCESS_LOCATION_REQ_CODE;
import static com.ashraf.amr.apps.bloodbank.utils.Constants.ENABLE_GPS_REQ_CODE;
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.bitmapDescriptorFromVector;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    public int geocoderMaxResults = 1;
    public static double latitude = 0.0, longitude = 0.0;
    public static String hospital_address = null, hospital_name = null;

    private AlertDialog alertDialog;

    private boolean mLocationPermissionGranted = false;

    @BindView(R.id.toolbar_back)
    RelativeLayout toolbarBack;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_sub_view)
    RelativeLayout toolbarSubView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        toolbarSubView.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.select_address));
        toolbarBack.setOnClickListener(v -> onBackPressed());


        if(InternetState.isConnected(this)){
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},ACCESS_LOCATION_REQ_CODE);
            }else {
                if(isGPSEnabled()){
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    assert mapFragment != null;
                    mapFragment.getMapAsync(this);
                }else {
                    isGPSEnabled();
                }
            }

        }


    }

    private boolean isGPSEnabled(){
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        assert manager != null;
        if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }

    private void buildAlertMessageNoGps() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("GPS Required")
                .setMessage("Enable GPS otherwise location tracking won't work")
                .setPositiveButton("SETTINGS", (dialog, which) -> {
                    Intent enableGpsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(enableGpsIntent,ENABLE_GPS_REQ_CODE);
                })
                .setNegativeButton("CANCEL", (dialog, which) -> {
                    alertDialog.cancel();
                    onBackPressed();
                });
        alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        if(InternetState.isConnected(this)){
            LatLng egypt = new LatLng(30.033333, 	31.233334);
            float zoomLevel = 6.5f; //This goes up to 21
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(egypt,zoomLevel));

            mMap.setMyLocationEnabled(true);

        try {

            String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

            if (!provider.contains("gps")) { //if gps is disabled
                final Intent poke = new Intent();
                poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                sendBroadcast(poke);
            } else {

                //GPSTracker gpsTracker = new GPSTracker(MapsActivity.this, MapsActivity.this);
                Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.ENGLISH);
//                latitude = gpsTracker.getLatitude();
//                longitude = gpsTracker.getLongitude();
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, geocoderMaxResults);
                Address address = addresses.get(0);
                hospital_address = address.getAddressLine(0);
                //hospital_name = address.getThoroughfare();
//                Toast.makeText(MapsActivity.this, "getAddressLine : " + address.getAddressLine(0) + "\n" +
//                                "getUrl : " + address.getUrl() + "\n" +
//                                "getThoroughfare : " + address.getThoroughfare() + "\n" +
//                                "getSubThoroughfare : " +  address.getSubThoroughfare() + "\n" +
//                                "getSubLocality : " + address.getSubLocality() + "\n" +
//                                "getSubAdminArea : " + address.getSubAdminArea() + "\n" +
//                                "getPostalCode : " + address.getPostalCode() + "\n" +
//                                "getPremises : " +  address.getPremises() + "\n" +
//                                "getPhone : " +  address.getPhone() + "\n" +
//                                "getLocality : " +  address.getLocality() + "\n" +
//                                "getFeatureName : " +  address.getFeatureName() + "\n" +
//                                "getCountryName : " + address.getCountryName() + "\n" +
//                                "getCountryCode : " +  address.getCountryCode() + "\n" +
//                                "getAdminArea : " + address.getAdminArea()  + "\n" +
//                                "getAddressLine : " + address.getAddressLine(0)  + "\n"
//                                                                , Toast.LENGTH_LONG).show();
                //LatLng you = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
                LatLng you = new LatLng(latitude, longitude);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(you, 17f));
            }


        } catch (Exception e) {
            //e.printStackTrace();
           // Toast.makeText(MapsActivity.this , e.getMessage(), Toast.LENGTH_SHORT).show();

        }

            googleMap.setOnMapClickListener(latLng -> {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.ic_map_marker_alt_solid));
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                latitude = latLng.latitude;
                longitude = latLng.longitude;

                Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.ENGLISH);

                try {
                    List<Address> addresses = geocoder.getFromLocation(latitude,longitude,geocoderMaxResults);
                    Address address = addresses.get(0);
                    hospital_address = address.getAddressLine(0);
                    //hospital_name = address.getThoroughfare();
                } catch (Exception e) {
                   // Toast.makeText(MapsActivity.this , e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                // Clears the previously touched position
                mMap.clear();

                // Animating to the touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f));

                // Placing a marker on the touched position
                mMap.addMarker(markerOptions);
            });
        }else {
            onBackPressed();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(this, "onActivityResult", Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, RESULT_OK+"", Toast.LENGTH_SHORT).show();
        if (requestCode == ENABLE_GPS_REQ_CODE){
            if (mLocationPermissionGranted){
                if(resultCode == RESULT_OK){
                    alertDialog.dismiss();
                    //Toast.makeText(this, "GPS Opened", Toast.LENGTH_SHORT).show();
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    assert mapFragment != null;
                    mapFragment.getMapAsync(this);
                }else {
                    alertDialog.dismiss();
                    //Toast.makeText(this, "GPS closed", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }else {
                getLocationPermission();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getLocationPermission() {
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},ACCESS_LOCATION_REQ_CODE);
        }else {
            mLocationPermissionGranted = true;
            if (isGPSEnabled()){
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                assert mapFragment != null;
                mapFragment.getMapAsync(this);
            }else {
                isGPSEnabled();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissionGranted = false;
        if (requestCode == ACCESS_LOCATION_REQ_CODE){
            // if request is cancelled, the request array are empty
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionGranted = true;
                if (isGPSEnabled()){
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    assert mapFragment != null;
                    mapFragment.getMapAsync(this);
                }else {
                    isGPSEnabled();
                }
            }else {
                onBackPressed();
            }
        }
    }

    @OnClick(R.id.donation_details_fragment_btn_call)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
