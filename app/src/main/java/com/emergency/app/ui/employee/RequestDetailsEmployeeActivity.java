package com.emergency.app.ui.employee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.emergency.app.R;
import com.emergency.app.databinding.ActivityRequestDetailsEmployeeBinding;
import com.emergency.app.dialogs.MessageDialog;
import com.emergency.app.dialogs.ReportDialog;
import com.emergency.app.util.appconfighelper.AppConfigHelper;
import com.emergency.app.util.appconfighelper.ValidateData;
import com.emergency.app.util.dialogshelper.CustomProgress;
import com.emergency.app.util.gpshelper.GPSHelper;
import com.emergency.app.util.languagehelper.LanguageHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class RequestDetailsEmployeeActivity extends AppCompatActivity implements View.OnClickListener,GPSHelper.OnLocationListenerEvent{
    ActivityRequestDetailsEmployeeBinding bind;
    private int requestID,guestID,employeeID,reviews;
    private DatabaseReference mdata;
    private String status,address,desc,guestType,requestType,report, dataTime,price,mobile,employeeJob,empployeeName,guestName,empployeePhoto,guestPhoto,rate;
    boolean isSendFeedback=false,isReview=false,isGetData=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new LanguageHelper().initLanguage(this,true);
        bind= DataBindingUtil.setContentView(this,R.layout.activity_request_details_employee);
        bind.executePendingBindings();
        initViews();
        getRequestDetails();
    }
    private void initViews()
    {
        try {
            requestID=getIntent().getExtras().getInt(AppConfigHelper.REQUEST_ID_INTENT);
            bind.lyContainerDetails.setBackground(ContextCompat.getDrawable(this,R.drawable.ic_bg));
            bind.lyHeaderContainer.tvMainTitle.setText(getResources().getString(R.string.request_details));


            bind.tvPhone.setOnClickListener(this);
            bind.lyHeaderContainer.ivBack.setOnClickListener(this);
            bind.tvPhone.setOnClickListener(this);
            bind.btnReport.setOnClickListener(this);
            bind.btnAccept.setOnClickListener(this);
            bind.btnReject.setOnClickListener(this);
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
            case R.id.tvPhone:
                try {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setMessage(getResources().getString(R.string.make_call));
                    dialog.setCancelable(true);
                    dialog.setPositiveButton(R.string.yes, (dialog1, which) -> {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + bind.tvPhone.getText().toString()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK  | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(this,
                                        new String[]{Manifest.permission.CALL_PHONE},123);
                            } else {
                                startActivity(intent);
                            }
                        } else {
                            startActivity(intent);
                        }
                    });
                    dialog.setNegativeButton(R.string.no, (dialog12, which) -> dialog12.cancel());
                    dialog.show();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
            case R.id.ivBack:
                try {
                    AppConfigHelper.finishActivity(this);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
            case R.id.btnAccept:
                try {
                    //accept order
                    mdata.child(String.valueOf(requestID)).child(AppConfigHelper.STATUS_REQUEST_FIELD).setValue(AppConfigHelper.ACCEPT_STATUS);
                    bind.btnReport.setText(getResources().getString(R.string.start_order));
                    bind.tvStatus.setText(AppConfigHelper.getStatus(this,AppConfigHelper.ACCEPT_STATUS));
                    bind.lyStatus.setBackground(AppConfigHelper.getBackground(this,AppConfigHelper.ACCEPT_STATUS));
                    bind.lyAccept.setVisibility(View.GONE);
                    bind.btnReport.setVisibility(View.VISIBLE);
                    showAlertMessage(getResources().getString(R.string.start_request_message));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
            case R.id.btnReject:
                try {
                    //reject order
                    mdata.child(String.valueOf(requestID)).child(AppConfigHelper.STATUS_REQUEST_FIELD).setValue(AppConfigHelper.REJECT_STATUS);
                    bind.btnReport.setVisibility(View.GONE);
                    bind.lyAccept.setVisibility(View.GONE);
                    bind.tvStatus.setText(AppConfigHelper.getStatus(this,AppConfigHelper.REJECT_STATUS));
                    bind.lyStatus.setBackground(AppConfigHelper.getBackground(this,AppConfigHelper.REJECT_STATUS));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
            case R.id.btnReport:
                try {
                    //start order
                    if(bind.btnReport.getText().toString().equals(getResources().getString(R.string.start_order)))
                    {
                        mdata.child(String.valueOf(requestID)).child(AppConfigHelper.STATUS_REQUEST_FIELD).setValue(AppConfigHelper.START_STATUS);
                        bind.tvStatus.setText(AppConfigHelper.getStatus(this,AppConfigHelper.START_STATUS));
                        bind.lyStatus.setBackground(AppConfigHelper.getBackground(this,AppConfigHelper.START_STATUS));
                        new GPSHelper(this,this);
                        bind.btnReport.setText(getResources().getString(R.string.add_report));
                    }
                    //add report
                    else if(bind.btnReport.getText().toString().equals(getResources().getString(R.string.add_report))) {
                        FragmentTransaction objFragmentTransaction = getSupportFragmentManager().beginTransaction();
                        Fragment prevFragment = getSupportFragmentManager().findFragmentByTag(AppConfigHelper.REPORT_DIALOG_TAG);
                        if (prevFragment != null) {
                            objFragmentTransaction.remove(prevFragment);
                        }
                        ReportDialog dialog = ReportDialog.newInstance(report -> {
                            bind.tvReport.setText(report);
                            //save report in request data and change button text to edit report
                            mdata.child(String.valueOf(requestID)).child(AppConfigHelper.REPORT_FIELD).setValue(report);
                            showAlertMessage(getResources().getString(R.string.finish_request_message));
                            bind.btnReport.setText(getResources().getString(R.string.finish_order));
                        });
                        dialog.show(objFragmentTransaction,AppConfigHelper.REPORT_DIALOG_TAG);
                        new GPSHelper(this,this);
                    }
                    //finish order
                    else
                    {
                        mdata.child(String.valueOf(requestID)).child(AppConfigHelper.STATUS_REQUEST_FIELD).setValue(AppConfigHelper.FINISH_STATUS);
                        bind.tvStatus.setText(AppConfigHelper.getStatus(this,AppConfigHelper.FINISH_STATUS));
                        bind.lyStatus.setBackground(AppConfigHelper.getBackground(this,AppConfigHelper.FINISH_STATUS));
                        bind.btnReport.setVisibility(View.GONE);
                        Toast.makeText(this,getResources().getString(R.string.request_finish_done),Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
        }
    }
    private void getRequestDetails() {
        try {
            CustomProgress.showProgress(this,getResources().getString(R.string.please_wait),false);
            isGetData=true;
            mdata = FirebaseDatabase.getInstance().getReference().child(AppConfigHelper.REQUEST_CHILD);
            mdata.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(isGetData) {
                        for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                            if (Integer.parseInt(String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.ID_REQUEST_FIELD).getValue())) == requestID) {
                                guestID = Integer.parseInt(String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.GUEST_ID_FIELD).getValue()));
                                employeeID = Integer.parseInt(String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.EMPLOYEE_ID_FIELD).getValue()));
                                address = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.ADDRESS_REQUEST_FIELD).getValue());
                                desc = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.DESCRIPTION_REQUEST_FIELD).getValue());
                                guestType = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.GUEST_TYPE_FIELD).getValue());
                                requestType = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.REQUEST_TYPE_FIELD).getValue());
                                report = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.REPORT_FIELD).getValue());
                                dataTime = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.DATETIME_REQUEST_FIELD).getValue());
                                price = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.PRICE_REQUEST_FIELD).getValue());
                                status = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.STATUS_REQUEST_FIELD).getValue());
                                employeeJob = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.EMP_JOB_FIELD).getValue());
                                empployeeName = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.EMP_NAME_FIELD).getValue());
                                guestName = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.GUEST_NAME_FIELD).getValue());
                                empployeePhoto = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.EMPLOYEE_PHOTO_FIELD).getValue());
                                guestPhoto = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.GUEST_PHOTO_FIELD).getValue());
                                isReview = (boolean) (uniqueKeySnapshot.child(AppConfigHelper.IS_REVIEW_FIELD).getValue());
                                //photo
                                if (ValidateData.isValid(guestPhoto)) {
                                    Picasso.with(getApplicationContext()).load(guestPhoto).placeholder(R.drawable.ic_girl).into(bind.profileImage);
                                } else {
                                    if (guestType.equals(getResources().getString(R.string.female))) {
                                        bind.profileImage.setImageResource(R.drawable.ic_girl);
                                    } else {
                                        bind.profileImage.setImageResource(R.drawable.ic_man);
                                    }
                                }
                                //status
                                bind.tvStatus.setText(AppConfigHelper.getStatus(getApplicationContext(), status));
                                bind.lyStatus.setBackground(AppConfigHelper.getBackground(getApplicationContext(), status));
                                //change text on add report button  based on order stats
                                if (!ValidateData.isValid(report)) {
                                    switch (status) {
                                        case AppConfigHelper.WAITING_STATUS:
                                            bind.lyAccept.setVisibility(View.VISIBLE);
                                            bind.btnReport.setVisibility(View.GONE);
                                            break;
                                        case AppConfigHelper.ACCEPT_STATUS:
                                            bind.lyAccept.setVisibility(View.GONE);
                                            bind.btnReport.setVisibility(View.VISIBLE);
                                            bind.btnReport.setText(getResources().getString(R.string.start_order));
                                            break;
                                        case AppConfigHelper.START_STATUS:
                                            new GPSHelper(RequestDetailsEmployeeActivity.this,RequestDetailsEmployeeActivity.this);
                                            bind.lyAccept.setVisibility(View.GONE);
                                            bind.btnReport.setVisibility(View.VISIBLE);
                                            bind.btnReport.setText(getResources().getString(R.string.finish_order));
                                            break;
                                        default:
                                            //rejected or finish
                                            bind.lyAccept.setVisibility(View.GONE);
                                            bind.btnReport.setVisibility(View.GONE);
                                            break;
                                    }
                                } else {
                                    if(!status.equals(AppConfigHelper.FINISH_STATUS)) {
                                        bind.lyAccept.setVisibility(View.GONE);
                                        bind.btnReport.setVisibility(View.VISIBLE);
                                        bind.btnReport.setText(getResources().getString(R.string.add_report));
                                    }
                                    else
                                    {
                                        bind.lyAccept.setVisibility(View.GONE);
                                        bind.btnReport.setVisibility(View.GONE);
                                    }
                                }
                                //request
                                bind.tvRequest.setText(requestType + " " + getResources().getString(R.string.request));
                                bind.tvDescRequest.setText(desc);
                                bind.tvRequestDateTime.setText(dataTime);
                                //guest
                                bind.tvUser.setText(guestName);
                                bind.tvPrice.setText(price + " " + getResources().getString(R.string.kwd));
                                bind.tvLocation.setText(address);
                                bind.tvGender.setText(guestType);
                                if (ValidateData.isValid(report)) {
                                    bind.tvReport.setText(report);
                                    bind.lyContainerReport.setVisibility(View.VISIBLE);
                                } else {
                                    bind.lyContainerReport.setVisibility(View.GONE);
                                }
                                CustomProgress.hideProgress();
                            }
                        }
                        isGetData=false;//stop get live data from firebase
                        mdata.removeEventListener(this);
                        getGuestData(guestID);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    CustomProgress.hideProgress();
                    Toast.makeText(RequestDetailsEmployeeActivity.this,getResources().getString(R.string.coudlnt_complete_process),Toast.LENGTH_SHORT).show();
                    AppConfigHelper.finishActivity(RequestDetailsEmployeeActivity.this);
                }
            });
        }
        catch (Exception e)
        {
            CustomProgress.hideProgress();
            Toast.makeText(RequestDetailsEmployeeActivity.this,getResources().getString(R.string.coudlnt_complete_process),Toast.LENGTH_SHORT).show();
            AppConfigHelper.finishActivity(RequestDetailsEmployeeActivity.this);
        }
    }
    private void getGuestData(int guestID)
    {
        try {
            DatabaseReference mData = FirebaseDatabase.getInstance().getReference().child(AppConfigHelper.GUEST_CHILD).child(AppConfigHelper.PROFILE_CHILD);
            mData.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                        if (Integer.parseInt(String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.ID_FIELD).getValue()))==guestID) {
                            mobile = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.MOBILE_FIELD).getValue());
                            bind.tvPhone.setText(mobile);
                            mData.removeEventListener(this);
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void showAlertMessage(String message)
    {
        try {
            FragmentTransaction objFragmentTransaction = getSupportFragmentManager().beginTransaction();
            Fragment prevFragment = getSupportFragmentManager().findFragmentByTag(AppConfigHelper.ADD_MESSAGE_DIALOG_TAG);
            if (prevFragment != null) {
                objFragmentTransaction.remove(prevFragment);
            }
            MessageDialog dialog = MessageDialog.newInstance(message);
            dialog.show(objFragmentTransaction,AppConfigHelper.ADD_MESSAGE_DIALOG_TAG);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void getLocation(Location location) {
        try {
           //update location for employee
            DatabaseReference mData = FirebaseDatabase.getInstance().getReference().child(AppConfigHelper.EMPLOYEE_CHILD).child(AppConfigHelper.PROFILE_CHILD).child(String.valueOf(employeeID)).child(AppConfigHelper.LOCATION_FIELD);
            mData.setValue(location.getLatitude() +","+ location.getLatitude());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        try {
            int permissionLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                new GPSHelper(this,this);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}
