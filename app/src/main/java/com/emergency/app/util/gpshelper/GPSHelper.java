package com.emergency.app.util.gpshelper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.util.TextUtils;

public class GPSHelper  extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {
    private GoogleApiClient googleApiClient;
    private final static int REQUEST_CHECK_SETTINGS_GPS=0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS=0x2;


    public OnLocationListenerEvent listenerEvent;
    private Location mylocationData;
    private Context mContext;
    private Activity activity;

    public GPSHelper(Context mContext,OnLocationListenerEvent listenerEvent)
    {
        this.mContext=mContext;
        this.listenerEvent=listenerEvent;
        setUpGClient();
    }
    public synchronized void setUpGClient() {
        try {
            googleApiClient = new GoogleApiClient.Builder(mContext)
                    .enableAutoManage((FragmentActivity) mContext, 0, this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            googleApiClient.stopAutoManage((FragmentActivity) mContext);
            googleApiClient.disconnect();
            googleApiClient.connect();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


    @Override
    public void onLocationChanged(Location location) {
      mylocationData=location;
      listenerEvent.getLocation(location);
      //googleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        checkPermissions();
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Do whatever you need
        //You can display a message here
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //You can display a message here
    }

    private void getMyLocation(){
        try {

            if (googleApiClient != null) {
                if (googleApiClient.isConnected()) {
                    int permissionLocation = ContextCompat.checkSelfPermission(mContext,
                            Manifest.permission.ACCESS_FINE_LOCATION);
                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                        mylocationData = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                        LocationRequest locationRequest = new LocationRequest();
                        locationRequest.setInterval(1000);
                        locationRequest.setFastestInterval(1000);
                        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                                .addLocationRequest(locationRequest);
                        builder.setAlwaysShow(true);
                        LocationServices.FusedLocationApi
                                .requestLocationUpdates(googleApiClient, locationRequest, this);
                        PendingResult<LocationSettingsResult> result =
                                LocationServices.SettingsApi
                                        .checkLocationSettings(googleApiClient, builder.build());
                        result.setResultCallback(result1 -> {
                            final Status status = result1.getStatus();
                            switch (status.getStatusCode()) {
                                case LocationSettingsStatusCodes.SUCCESS:
                                    // All location settings are satisfied.
                                    // You can initialize location requests here.
                                    int permissionLocation1 = ContextCompat
                                            .checkSelfPermission(mContext,
                                                    Manifest.permission.ACCESS_FINE_LOCATION);
                                    if (permissionLocation1 == PackageManager.PERMISSION_GRANTED) {
                                        mylocationData = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                                        if(mylocationData!=null) {
                                            listenerEvent.getLocation(mylocationData);
                                            //googleApiClient.disconnect();
                                        }
                                        else
                                        {
                                            googleApiClient.connect();
                                        }
                                    }
                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    // Location settings are not satisfied.
                                    // But could be fixed by showing the user a dialog.
                                    try {
                                        // Show the dialog by calling startResolutionForResult(),
                                        // and check the result in onActivityResult().
                                        // Ask to turn on GPS automatically

                                        status.startResolutionForResult((Activity) mContext, REQUEST_CHECK_SETTINGS_GPS);
                                    } catch (IntentSender.SendIntentException e) {
                                        // Ignore the error.
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    // Location settings are not satisfied.
                                    // However, we have no way
                                    // to fix the
                                    // settings so we won't show the dialog.
                                    // finish();
                                    break;
                            }
                        });
                    }
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CHECK_SETTINGS_GPS) {
            if(resultCode == Activity.RESULT_OK){
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                googleApiClient.disconnect();
            }
        }
    }
    private void checkPermissions(){
        try {

            int permissionLocation = ContextCompat.checkSelfPermission(mContext,
                    android.Manifest.permission.ACCESS_FINE_LOCATION);
            List<String> listPermissionsNeeded = new ArrayList<>();
            if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
                if (!listPermissionsNeeded.isEmpty()) {
                    ActivityCompat.requestPermissions((Activity) mContext, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
                }
            } else {
                getMyLocation();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        try {
            int permissionLocation = ContextCompat.checkSelfPermission(mContext,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                getMyLocation();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /*public static boolean isLocationEnabled(Context context) {
        try {
            int locationMode;
            String locationProviders;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                try {
                    locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
                return locationMode != Settings.Secure.LOCATION_MODE_OFF;

            } else {
                try {
                    locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
                    return !TextUtils.isEmpty(locationProviders);
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }

    }*/
    public interface OnLocationListenerEvent{
        void getLocation(Location location);
    }
}
