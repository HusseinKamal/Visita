package com.emergency.app.ui.guest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.location.Location;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.emergency.app.R;
import com.emergency.app.adapters.guest.EmployeeAdapter;
import com.emergency.app.databinding.ActivityFilterBinding;
import com.emergency.app.dialogs.FilterDialog;
import com.emergency.app.dialogs.MediaBottomSheetDialog;
import com.emergency.app.dialogs.ReviewsDialog;
import com.emergency.app.interfaces.OnEmployeeListener;
import com.emergency.app.models.Filter;
import com.emergency.app.models.User;
import com.emergency.app.util.appconfighelper.AppConfigHelper;
import com.emergency.app.util.appconfighelper.ValidateData;
import com.emergency.app.util.gpshelper.GPSHelper;
import com.emergency.app.util.languagehelper.LanguageHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FilterActivity extends AppCompatActivity implements View.OnClickListener, OnEmployeeListener,GPSHelper.OnLocationListenerEvent{

    ActivityFilterBinding bind;
    private Filter filter;
    private EmployeeAdapter adapter;
    private LinearLayoutManager mLayoutManager;
    private DatabaseReference mData;
    private ArrayList<User> listEmployee=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new LanguageHelper().initLanguage(this,true);
        bind= DataBindingUtil.setContentView(this,R.layout.activity_filter);
        bind.executePendingBindings();
        intiViews();
        getData(filter);
    }

    private void intiViews() {
        try {
            if(getIntent().hasExtra(AppConfigHelper.FILTER_INTENT)) {
                filter = (Filter) getIntent().getExtras().getSerializable(AppConfigHelper.FILTER_INTENT);
            }
            ViewCompat.setNestedScrollingEnabled(bind.rvEmployee, false);
            mLayoutManager = new LinearLayoutManager(this.getApplicationContext(), LinearLayoutManager.VERTICAL, false)
            {
                @Override
                public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position)
                {
                    LinearSmoothScroller smoothScroller = new LinearSmoothScroller(getApplicationContext())
                    {
                        @Override
                        public PointF computeScrollVectorForPosition(int targetPosition)
                        {
                            int yDelta = AppConfigHelper.calculateCurrentDistanceToPosition(mLayoutManager,targetPosition);
                            return new PointF(0, yDelta);
                        }
                        @Override
                        protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics)
                        {

                            return displayMetrics.widthPixels / TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, displayMetrics.heightPixels, displayMetrics);
                        }
                    };
                    smoothScroller.setTargetPosition(position);

                    startSmoothScroll(smoothScroller);
                }
            };

            //listener
            bind.ivFilter.setOnClickListener(this);
            bind.ivSearch.setOnClickListener(this);
            bind.ivBack.setOnClickListener(this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.ivBack:
                try {
                    AppConfigHelper.finishActivity(this);
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
                            //make filter in items activity
                            getData(filter);
                        });
                        dialog.show(objFragmentTransaction, AppConfigHelper.FILTER_DIALOG_TAG);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
            case R.id.ivSearch:
                try {
                    AppConfigHelper.gotoActivity(this, SearchActivity.class,false);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onRecommendClick(int id) {
    }
    @Override
    public void onEmployeeClick(int id) {
        try {
            Intent objIntent = new Intent(this, AddRequestActivity.class);
            objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_NO_ANIMATION);
            objIntent.putExtra(AppConfigHelper.EMPLOYEE_ID_INTENT,id);
            startActivity(objIntent);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onEmployeeReviewClick(int id) {
        try {
            ReviewsDialog reviewsDialog = ReviewsDialog.newInstance(id);
            reviewsDialog.show(getSupportFragmentManager(),"reviewsDialog");
        }
        catch (Exception objException)
        {
            objException.printStackTrace();
        }
    }
    private void getData(Filter filter)
    {
        try {

            mData = FirebaseDatabase.getInstance().getReference().child(AppConfigHelper.EMPLOYEE_CHILD).child(AppConfigHelper.PROFILE_CHILD);
            mData.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                        String addressEmp=String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.ADDRESS_FIELD).getValue());
                        String jobEmp=String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.JOB_FIELD).getValue());
                        String nameEmp=String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.NAME_FIELD).getValue());
                        int priceEmp=Integer.parseInt(String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.PRICE_FIELD).getValue()));
                        if (addressEmp.equals(filter.getLocation())
                                &&jobEmp.equals(filter.getJob())
                        &&filter.getPrice()<=priceEmp){
                            if(ValidateData.isValid(filter.getName()))
                            {
                                if(nameEmp.contains(filter.getName()))
                                {
                                    String name = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.NAME_FIELD).getValue());
                                    int orders = Integer.parseInt(String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.ORDER_FIELD).getValue()));
                                    int id = Integer.parseInt(String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.ID_FIELD).getValue()));
                                    String rate = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.RATE_FIELD).getValue());
                                    int reviews = Integer.parseInt(String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.REVIEWS_FIELD).getValue()));
                                    String type = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.TYPE_FIELD).getValue());
                                    String photo = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.PHOTO_FIELD).getValue());
                                    String job = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.JOB_FIELD).getValue());
                                    String price = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.PRICE_FIELD).getValue());
                                    User objData = new User(id, name,job, type, photo,orders,reviews,rate,price);
                                    listEmployee.clear();
                                    listEmployee.add(objData);
                                    //fill recycler view
                                    bind.rvEmployee.setLayoutManager(mLayoutManager);
                                    adapter=new EmployeeAdapter(FilterActivity.this,FilterActivity.this,listEmployee);
                                    bind.rvEmployee.setAdapter(adapter);
                                    bind.rvEmployee.setVisibility(View.VISIBLE);
                                    bind.pbarLoad.setVisibility(View.GONE);
                                    bind.lyNoData.lyNoData.setVisibility(View.GONE);
                                }
                                else
                                {
                                    listEmployee.clear();
                                }
                            }
                            else
                            {
                                String name = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.NAME_FIELD).getValue());
                                int orders = Integer.parseInt(String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.ORDER_FIELD).getValue()));
                                int id = Integer.parseInt(String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.ID_FIELD).getValue()));
                                String rate = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.RATE_FIELD).getValue());
                                int reviews = Integer.parseInt(String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.REVIEWS_FIELD).getValue()));
                                String type = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.TYPE_FIELD).getValue());
                                String photo = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.PHOTO_FIELD).getValue());
                                String job = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.JOB_FIELD).getValue());
                                String price = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.PRICE_FIELD).getValue());
                                User objData = new User(id, name,job, type, photo,orders,reviews,rate,price);
                                listEmployee.clear();
                                listEmployee.add(objData);
                                //fill recycler view
                                bind.rvEmployee.setLayoutManager(mLayoutManager);
                                adapter=new EmployeeAdapter(FilterActivity.this,FilterActivity.this,listEmployee);
                                bind.rvEmployee.setAdapter(adapter);
                                bind.rvEmployee.setVisibility(View.VISIBLE);
                                bind.pbarLoad.setVisibility(View.GONE);
                                bind.lyNoData.lyNoData.setVisibility(View.GONE);
                            }

                        }
                    }
                    if(listEmployee.size()==0)
                    {
                        hideViews();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    hideViews();
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void hideViews()
    {
        try {
            bind.rvEmployee.setVisibility(View.GONE);
            bind.pbarLoad.setVisibility(View.GONE);
            bind.lyNoData.lyNoData.setVisibility(View.VISIBLE);
        }
        catch (Exception e)
        {
            e.printStackTrace();
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
                    //make filter in items activity
                    getData(filter);
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
