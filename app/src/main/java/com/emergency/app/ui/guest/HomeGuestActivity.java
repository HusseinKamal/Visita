package com.emergency.app.ui.guest;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import com.emergency.app.fragments.guest.OrderGuestFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.emergency.app.R;
import com.emergency.app.databinding.ActivityMainBinding;
import com.emergency.app.dialogs.FilterDialog;
import com.emergency.app.fragments.AboutFragment;
import com.emergency.app.fragments.guest.HomeGuestFragment;
import com.emergency.app.fragments.MoreFragment;
import com.emergency.app.fragments.guest.ProfileFragment;
import com.emergency.app.util.appconfighelper.AppConfigHelper;
import com.emergency.app.util.gpshelper.GPSHelper;
import com.emergency.app.util.languagehelper.LanguageHelper;
import com.emergency.app.viewmodel.HomeViewModel;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import android.view.View;

public class HomeGuestActivity extends AppCompatActivity implements View.OnClickListener,GPSHelper.OnLocationListenerEvent {
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private HomeViewModel model;
    ActivityMainBinding bind;
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
                    case R.id.navigation_mafqudat:
                        updateFragmentContainer(AppConfigHelper.ORDER_ID);
                        return true;
                }
                return false;
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new LanguageHelper().initLanguage(this,true);
        bind= DataBindingUtil.setContentView(this,R.layout.activity_main);
        bind.executePendingBindings();
        initViews();
    }
    private void initViews()
    {
        try {
            fragmentManager=getSupportFragmentManager();
            bind.navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
            updateFragmentContainer(AppConfigHelper.HOME_ID);
            bind.lyHeaderContainer.ivSearch.setOnClickListener(this);
            bind.lyHeaderContainer.ivFilter.setOnClickListener(this);
            model = ViewModelProviders.of(this).get(HomeViewModel.class);
            model.getData().observe(this, data -> {
                try {
                    switch (data)
                    {
                        case AppConfigHelper.HOME_ID:
                            bind.navView.setSelectedItemId(R.id.navigation_home);
                            break;
                        case AppConfigHelper.MORE_ID:
                            bind.navView.setSelectedItemId(R.id.navigation_more);
                            break;
                        case AppConfigHelper.PROFILE_ID:
                            bind.navView.setSelectedItemId(R.id.navigation_profile);
                            break;
                        case AppConfigHelper.ORDER_ID:
                            bind.navView.setSelectedItemId(R.id.navigation_mafqudat);
                            break;
                        case AppConfigHelper.ABOUT_ID:
                            updateFragmentContainer(AppConfigHelper.ABOUT_ID);
                            break;
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @SuppressLint("ResourceAsColor")
    private void updateFragmentContainer(int fragID) {
        try {
            switch (fragID) {
                case AppConfigHelper.HOME_ID:
                    HomeGuestFragment homeGuestFragment = HomeGuestFragment.newInstance();
                    transaction = fragmentManager.beginTransaction();
                    fragmentManager.popBackStack();
                    transaction.replace(R.id.fragmentContainer, homeGuestFragment);
                    transaction.commit();
                    bind.lyHeaderContainer.tvMainTitle.setText(getResources().getString(R.string.home));
                    break;
                case AppConfigHelper.PROFILE_ID:
                    ProfileFragment profileFragment = ProfileFragment.newInstance();
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
                case AppConfigHelper.ORDER_ID:
                    OrderGuestFragment orderGuestFragment = OrderGuestFragment.newInstance();
                    transaction = fragmentManager.beginTransaction();
                    fragmentManager.popBackStack();
                    transaction.replace(R.id.fragmentContainer, orderGuestFragment);
                    transaction.commit();
                    bind.lyHeaderContainer.tvMainTitle.setText(getResources().getString(R.string.order));
                    break;
            }
        } catch (Exception objException) {
            objException.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.ivSearch:
                try {
                    AppConfigHelper.gotoActivity(this, SearchActivity.class,false);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
            case R.id.ivFilter:
                try {
                    //check permission first
                    int permissionLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
                    if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
                        //get Current Location
                        new GPSHelper(this,this);
                    }
                    else {
                        FragmentTransaction objFragmentTransaction = getSupportFragmentManager().beginTransaction();
                        Fragment prevFragment = getSupportFragmentManager().findFragmentByTag(AppConfigHelper.FILTER_DIALOG_TAG);
                        if (prevFragment != null) {
                            objFragmentTransaction.remove(prevFragment);
                        }
                        FilterDialog dialog = FilterDialog.newInstance(filter -> {
                            //make filter
                            Intent objIntent = new Intent(this, FilterActivity.class);
                            objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            Bundle bundle=new Bundle();
                            bundle.putSerializable(AppConfigHelper.FILTER_INTENT,filter);
                            objIntent.putExtras(bundle);
                            startActivity(objIntent);
                        });
                        dialog.show(objFragmentTransaction, AppConfigHelper.FILTER_DIALOG_TAG);
                    }
                }
                catch (Exception objExcption)
                {
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
                    //show Filter Dialog
                    FragmentTransaction objFragmentTransaction = getSupportFragmentManager().beginTransaction();
                    Fragment prevFragment = getSupportFragmentManager().findFragmentByTag(AppConfigHelper.FILTER_DIALOG_TAG);
                    if (prevFragment != null) {
                        objFragmentTransaction.remove(prevFragment);
                    }
                    FilterDialog dialog = FilterDialog.newInstance(filter -> {
                        //make filter
                        Intent objIntent = new Intent(this, FilterActivity.class);
                        objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable(AppConfigHelper.FILTER_INTENT,filter);
                        objIntent.putExtras(bundle);
                        startActivity(objIntent);
                    });
                    dialog.show(objFragmentTransaction, AppConfigHelper.FILTER_DIALOG_TAG);
                }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void getLocation(Location location) {

    }
}
