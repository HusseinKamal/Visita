package com.emergency.app.ui.employee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.emergency.app.R;
import com.emergency.app.databinding.ActivityHomeEmployeeBinding;
import com.emergency.app.dialogs.MediaBottomSheetDialog;
import com.emergency.app.fragments.AboutFragment;
import com.emergency.app.fragments.MoreFragment;
import com.emergency.app.fragments.emploee.ProfileEmployeeFragment;
import com.emergency.app.fragments.emploee.HomeEmployeeFragment;
import com.emergency.app.models.User;
import com.emergency.app.ui.guest.SearchActivity;
import com.emergency.app.util.appconfighelper.AppConfigHelper;
import com.emergency.app.util.appconfighelper.ValidateData;
import com.emergency.app.util.dialogshelper.CustomProgress;
import com.emergency.app.util.gpshelper.GPSHelper;
import com.emergency.app.util.languagehelper.LanguageHelper;
import com.emergency.app.util.sharedprefrencehelper.SharedPrefHelper;
import com.emergency.app.viewmodel.HomeViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HomeEmployeeActivity extends AppCompatActivity implements View.OnClickListener, GPSHelper.OnLocationListenerEvent {
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private HomeViewModel model;
    private boolean isOpenGPs,isGetData;
    private User user;
    ActivityHomeEmployeeBinding bind;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                updateFragmentContainer(AppConfigHelper.HOME_ID);
                return true;
            case R.id.navigation_more:
                updateFragmentContainer(AppConfigHelper.MORE_ID);
                return true;
            case R.id.navigation_profile:
                updateFragmentContainer(AppConfigHelper.PROFILE_ID);
                return true;
        }
        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new LanguageHelper().initLanguage(this, true);
        bind = DataBindingUtil.setContentView(this, R.layout.activity_home_employee);
        bind.executePendingBindings();
        initViews();
    }

    private void initViews() {
        try {
            fragmentManager = getSupportFragmentManager();
            bind.navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
            updateFragmentContainer(AppConfigHelper.HOME_ID);
            bind.lyHeaderContainer.ivSearch.setVisibility(View.GONE);
            bind.lyHeaderContainer.ivFilter.setVisibility(View.GONE);
            bind.lyHeaderContainer.ivSearch.setOnClickListener(this);
            bind.lyHeaderContainer.ivFilter.setOnClickListener(this);
            model = ViewModelProviders.of(this).get(HomeViewModel.class);
            model.getData().observe(this, data -> {
                try {
                    switch (data) {
                        case AppConfigHelper.HOME_ID:
                            bind.navView.setSelectedItemId(R.id.navigation_home);
                            break;
                        case AppConfigHelper.MORE_ID:
                            bind.navView.setSelectedItemId(R.id.navigation_more);
                            break;
                        case AppConfigHelper.PROFILE_ID:
                            bind.navView.setSelectedItemId(R.id.navigation_profile);
                            break;
                        case AppConfigHelper.ABOUT_ID:
                            updateFragmentContainer(AppConfigHelper.ABOUT_ID);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            //get user data
            user=(User) SharedPrefHelper.getSharedOBJECT(this,SharedPrefHelper.SHARED_PREFERENCE_USER_DATA);
            //check if user has started request to update location
            getRequestsDetails();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("ResourceAsColor")
    private void updateFragmentContainer(int fragID) {
        try {
            switch (fragID) {
                case AppConfigHelper.HOME_ID:
                    HomeEmployeeFragment homeEmployeeFragment = HomeEmployeeFragment.newInstance();
                    transaction = fragmentManager.beginTransaction();
                    fragmentManager.popBackStack();
                    transaction.replace(R.id.fragmentContainer, homeEmployeeFragment);
                    transaction.commit();
                    bind.lyHeaderContainer.tvMainTitle.setText(getResources().getString(R.string.order));
                    break;
                case AppConfigHelper.PROFILE_ID:
                    ProfileEmployeeFragment profileFragment = ProfileEmployeeFragment.newInstance();
                    transaction = fragmentManager.beginTransaction();
                    fragmentManager.popBackStack();
                    transaction.replace(R.id.fragmentContainer, profileFragment);
                    transaction.commit();
                    bind.lyHeaderContainer.tvMainTitle.setText(getResources().getString(R.string.profile));
                    break;
                case AppConfigHelper.MORE_ID:
                    MoreFragment moreFragment = MoreFragment.newInstance();
                    transaction = fragmentManager.beginTransaction();
                    fragmentManager.popBackStack();
                    transaction.replace(R.id.fragmentContainer, moreFragment);
                    transaction.commit();
                    bind.lyHeaderContainer.tvMainTitle.setText(getResources().getString(R.string.more));
                    break;
                case AppConfigHelper.ABOUT_ID:
                    AboutFragment aboutFragment = AboutFragment.newInstance();
                    transaction = fragmentManager.beginTransaction();
                    fragmentManager.popBackStack();
                    transaction.replace(R.id.fragmentContainer, aboutFragment);
                    transaction.commit();
                    bind.lyHeaderContainer.tvMainTitle.setText(getResources().getString(R.string.about_us));
                    break;
            }
        } catch (Exception objException) {
            objException.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivSearch:
                try {
                    AppConfigHelper.gotoActivity(this, SearchActivity.class, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ivFilter:
                try {
                    //check permission first
                    int permissionLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
                    if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
                        //get Current Location
                        new GPSHelper(this, this);
                    } else {
                        MediaBottomSheetDialog bottomSheetDialog = new MediaBottomSheetDialog();
                        bottomSheetDialog.show(this.getSupportFragmentManager(), AppConfigHelper.FILTER_DIALOG_TAG);
                    }
                } catch (Exception objExcption) {
                    objExcption.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            int permissionLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                MediaBottomSheetDialog bottomSheetDialog = new MediaBottomSheetDialog();
                bottomSheetDialog.show(this.getSupportFragmentManager(), AppConfigHelper.FILTER_DIALOG_TAG);
            }
            if(isOpenGPs) {
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    new GPSHelper(this, this);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getLocation(Location location) {
        try {
            //update location for employee
            DatabaseReference mData = FirebaseDatabase.getInstance().getReference().child(AppConfigHelper.EMPLOYEE_CHILD).child(AppConfigHelper.PROFILE_CHILD).child(String.valueOf(user.getId())).child(AppConfigHelper.LOCATION_FIELD);
            mData.setValue(location.getLatitude() + "," + location.getLatitude());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //update location for employee to enable tracking
    private void getRequestsDetails() {
        try {
            isGetData=true;//stop getting data after enable gps
            DatabaseReference mdata = FirebaseDatabase.getInstance().getReference().child(AppConfigHelper.REQUEST_CHILD);
            mdata.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(isGetData) {
                        for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                            if (String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.STATUS_REQUEST_FIELD).getValue()).equals(AppConfigHelper.START_STATUS)) {
                                new GPSHelper(HomeEmployeeActivity.this, HomeEmployeeActivity.this);
                                isOpenGPs = true;
                            }
                        }
                        isGetData=false;
                        mdata.removeEventListener(this);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
