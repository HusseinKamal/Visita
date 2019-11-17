package com.emergency.app.ui.guest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.emergency.app.R;
import com.emergency.app.databinding.ActivityMapTrackBinding;
import com.emergency.app.util.appconfighelper.AppConfigHelper;
import com.emergency.app.util.appconfighelper.ValidateData;
import com.emergency.app.util.languagehelper.LanguageHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MapTrackActivity extends AppCompatActivity implements View.OnClickListener,OnMapReadyCallback{
    ActivityMapTrackBinding bind;
    protected static Location mCurrentLocation;
    protected static GoogleMap mGoogleMap;
    private final static int REQUEST_CHECK_SETTINGS_GPS=0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS=0x2;
    private int employeeID;
    private DatabaseReference mData;
    private String[] location;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mGoogleMap = googleMap;
            mGoogleMap.setMyLocationEnabled(true);
        } catch (Exception objException) {
            objException.printStackTrace();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        bind.map.onLowMemory();
    }

    @Override
    public void onResume() {
        super.onResume();
        bind.map.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        bind.map.onSaveInstanceState(outState);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new LanguageHelper().initLanguage(this,true);
        AppConfigHelper.makeTransparent(this);
        bind= DataBindingUtil.setContentView(this,R.layout.activity_map_track);
        bind.executePendingBindings();
        employeeID=getIntent().getExtras().getInt(AppConfigHelper.EMPLOYEE_ID_INTENT);
        MapsInitializer.initialize(this);
        bind.map.onCreate(savedInstanceState);
        bind.map.getMapAsync(this);
        updateMarker();
        initViews();
    }
    private void initViews()
    {
        try {
            AppConfigHelper.changeBackArrow(this,bind.ivBack);
            bind.lyBack.setOnClickListener(this);
            bind.ivBack.setOnClickListener(this);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void updateMarker() {
        mData = FirebaseDatabase.getInstance().getReference().child(AppConfigHelper.EMPLOYEE_CHILD).child(AppConfigHelper.PROFILE_CHILD).child(String.valueOf(employeeID));
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!String.valueOf(dataSnapshot.child(AppConfigHelper.LOCATION_FIELD).getValue()).equals("0")
                && ValidateData.isValid(String.valueOf(dataSnapshot.child(AppConfigHelper.LOCATION_FIELD).getValue())))
                {
                    mGoogleMap.clear();
                    location = String.valueOf(dataSnapshot.child(AppConfigHelper.LOCATION_FIELD).getValue()).split(",");
                    double lat=Double.parseDouble(location[0]);
                    double lang=Double.parseDouble(location[1]);
                    LatLng egy = new LatLng(lat, lang);
                    mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
                    mGoogleMap.addMarker(new MarkerOptions().flat(true).position(egy).icon(BitmapDescriptorFactory.fromBitmap(AppConfigHelper.getBitmapFromVectorDrawable(getApplicationContext(),R.drawable.ic_pin2))));
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(egy));
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(egy, 15.0f));
                }
                else
                {
                    Toast.makeText(MapTrackActivity.this,getResources().getString(R.string.employee_trac_failed),Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
            case R.id.lyBack:
                try {
                    AppConfigHelper.finishActivity(this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
