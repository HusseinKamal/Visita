package com.emergency.app.ui.guest;

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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.emergency.app.R;
import com.emergency.app.databinding.ActivityRequestDetailsBinding;
import com.emergency.app.dialogs.RateDiaog;
import com.emergency.app.util.appconfighelper.AppConfigHelper;
import com.emergency.app.util.appconfighelper.ValidateData;
import com.emergency.app.util.dialogshelper.CustomProgress;
import com.emergency.app.util.languagehelper.LanguageHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class RequestDetailsGuestActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityRequestDetailsBinding bind;
    private int requestID,guestID,employeeID,reviews;
    private DatabaseReference mdata;
    private String status,address,desc,guestType,requestType,report, dataTime,price,mobile,employeeJob,empployeeName,guestName,empployeePhoto,guestPhoto,rate;
    boolean isSendFeedback=false,isReview=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new LanguageHelper().initLanguage(this,true);
        bind= DataBindingUtil.setContentView(this,R.layout.activity_request_details);
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
            bind.btnRoute.setOnClickListener(this);
            bind.tvPhone.setOnClickListener(this);
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
            case R.id.btnRoute:
                try {
                    if(bind.btnRoute.getText().equals(getResources().getString(R.string.send_feedback))) {
                        Map<String, Object> userComment = new HashMap<>();
                        //rate here
                        FragmentTransaction objFragmentTransaction = getSupportFragmentManager().beginTransaction();
                        Fragment prevFragment = getSupportFragmentManager().findFragmentByTag(AppConfigHelper.RATE_DIALOG_TAG);
                        if (prevFragment != null) {
                            objFragmentTransaction.remove(prevFragment);
                        }
                        RateDiaog dialog = RateDiaog.newInstance((rateUser, message) -> {
                            //if user made a review before.
                            if(!isReview) {
                                DatabaseReference mdata = FirebaseDatabase.getInstance().getReference().child(AppConfigHelper.EMPLOYEE_CHILD).child(AppConfigHelper.PROFILE_CHILD).child(String.valueOf(employeeID));
                                //add message,rate and reviews
                                mdata.child(AppConfigHelper.REVIEWS_FIELD).setValue(reviews + 1);
                                Float rateVal = Float.parseFloat(rate) + Float.parseFloat(rateUser);
                                mdata.child(AppConfigHelper.RATE_FIELD).setValue(String.valueOf(rateVal));
                                if (ValidateData.isValid(message)) {
                                    isSendFeedback = true;
                                    mdata.child(AppConfigHelper.COMMENT_CHILD).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (isSendFeedback) {
                                                isSendFeedback = false;
                                                isReview=true;
                                                bind.btnRoute.setVisibility(View.GONE);
                                                //close make review after finished
                                                DatabaseReference mDataReview = FirebaseDatabase.getInstance().getReference().child(AppConfigHelper.REQUEST_CHILD).child(String.valueOf(requestID));
                                                mDataReview.child(AppConfigHelper.IS_REVIEW_FIELD).setValue(true);
                                                if (dataSnapshot.getChildrenCount() > 0) {
                                                    userComment.put(AppConfigHelper.ID_FIELD, guestID);
                                                    userComment.put(AppConfigHelper.NAME_FIELD, guestName);
                                                    userComment.put(AppConfigHelper.PHOTO_FIELD, guestPhoto);
                                                    userComment.put(AppConfigHelper.MESSAGE_FIELD, message);
                                                    userComment.put(AppConfigHelper.TIME_FIELD, System.currentTimeMillis());
                                                    mdata.child(AppConfigHelper.COMMENT_CHILD).child(String.valueOf(dataSnapshot.getChildrenCount() + 1)).setValue(userComment);
                                                } else {
                                                    userComment.put(AppConfigHelper.ID_FIELD, guestID);
                                                    userComment.put(AppConfigHelper.NAME_FIELD, guestName);
                                                    userComment.put(AppConfigHelper.PHOTO_FIELD, guestPhoto);
                                                    userComment.put(AppConfigHelper.MESSAGE_FIELD, message);
                                                    userComment.put(AppConfigHelper.TIME_FIELD, System.currentTimeMillis());
                                                    mdata.child(AppConfigHelper.COMMENT_CHILD).child("1").setValue(userComment);
                                                }
                                            }
                                            Toast.makeText(RequestDetailsGuestActivity.this, getResources().getString(R.string.thanks_feedback), Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                } else {
                                    //add rate only
                                    Float rateEmployee = Float.parseFloat(rate) + Float.parseFloat(rateUser);
                                    mdata.child(AppConfigHelper.RATE_FIELD).setValue(String.valueOf(rateEmployee));
                                }
                            }
                        });
                        dialog.show(objFragmentTransaction, AppConfigHelper.RATE_DIALOG_TAG);
                    }
                    else
                    {
                        //track employee
                        Intent objIntent = new Intent(this, MapTrackActivity.class);
                        objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        objIntent.putExtra(AppConfigHelper.EMPLOYEE_ID_INTENT,employeeID);
                        startActivity(objIntent);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
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
        }
    }
    private void getRequestDetails() {
        try {
            CustomProgress.showProgress(this,getResources().getString(R.string.please_wait),false);
            mdata = FirebaseDatabase.getInstance().getReference().child(AppConfigHelper.REQUEST_CHILD);
            mdata.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                        if (Integer.parseInt(String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.ID_REQUEST_FIELD).getValue())) == requestID) {
                            guestID = Integer.parseInt(String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.GUEST_ID_FIELD).getValue()));
                            employeeID =Integer.parseInt(String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.EMPLOYEE_ID_FIELD).getValue()));
                            address = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.ADDRESS_REQUEST_FIELD).getValue());
                            desc = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.DESCRIPTION_REQUEST_FIELD).getValue());
                            guestType = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.GUEST_TYPE_FIELD).getValue());
                            requestType = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.REQUEST_TYPE_FIELD).getValue());
                            report = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.REPORT_FIELD).getValue());
                            dataTime = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.DATETIME_REQUEST_FIELD).getValue());
                            price = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.PRICE_REQUEST_FIELD).getValue());
                            status=String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.STATUS_REQUEST_FIELD).getValue());
                            mobile = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.EMP_MOBILE_FIELD).getValue());
                            employeeJob = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.EMP_JOB_FIELD).getValue());
                            empployeeName = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.EMP_NAME_FIELD).getValue());
                            guestName = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.GUEST_NAME_FIELD).getValue());
                            empployeePhoto = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.EMPLOYEE_PHOTO_FIELD).getValue());
                            guestPhoto = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.GUEST_PHOTO_FIELD).getValue());
                            isReview=(boolean)(uniqueKeySnapshot.child(AppConfigHelper.IS_REVIEW_FIELD).getValue());
                            //photo
                            if(ValidateData.isValid(empployeePhoto))
                            {
                                Picasso.with(getApplicationContext()).load(empployeePhoto).placeholder(R.drawable.ic_girl).into(bind.profileImage);
                            }
                            else
                            {
                                if(guestType.equals(getResources().getString(R.string.female))) {
                                    bind.profileImage.setImageResource(R.drawable.ic_girl);
                                }
                                else
                                {
                                    bind.profileImage.setImageResource(R.drawable.ic_man);
                                }
                            }
                            //status
                            bind.tvStatus.setText(AppConfigHelper.getStatus(getApplicationContext(),status));
                            bind.lyStatus.setBackground(AppConfigHelper.getBackground(getApplicationContext(),status));
                            if(!isReview) {
                                if (status.equals(AppConfigHelper.START_STATUS)) {
                                    bind.btnRoute.setText(getResources().getString(R.string.track_route));
                                    bind.btnRoute.setVisibility(View.VISIBLE);
                                } else if (status.equals(AppConfigHelper.FINISH_STATUS)) {
                                    bind.btnRoute.setText(getResources().getString(R.string.send_feedback));
                                    bind.btnRoute.setVisibility(View.VISIBLE);
                                } else {
                                    bind.btnRoute.setVisibility(View.GONE);
                                }
                            }
                            else
                            {
                                bind.btnRoute.setVisibility(View.GONE);
                            }
                            //request
                            bind.tvRequest.setText(requestType+" "+getResources().getString(R.string.request));
                            bind.tvDescRequest.setText(desc);
                            bind.tvRequestDateTime.setText(dataTime);
                            //employee
                            bind.tvUser.setText(empployeeName);
                            bind.tvJob.setText(employeeJob);
                            bind.tvPrice.setText(price+" "+getResources().getString(R.string.kwd));
                            bind.tvPhone.setText(mobile);
                            bind.tvLocation.setText(address);
                            bind.tvGender.setText(guestType);
                            if(ValidateData.isValid(report)) {
                                bind.tvReport.setText(report);
                                bind.lyContainerReport.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                bind.lyContainerReport.setVisibility(View.GONE);
                            }
                            CustomProgress.hideProgress();
                        }
                    }
                    mdata.removeEventListener(this);
                    getEmployeeData(employeeID);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    CustomProgress.hideProgress();
                    Toast.makeText(RequestDetailsGuestActivity.this,getResources().getString(R.string.coudlnt_complete_process),Toast.LENGTH_SHORT).show();
                    AppConfigHelper.finishActivity(RequestDetailsGuestActivity.this);
                }
            });
        }
        catch (Exception e)
        {
            CustomProgress.hideProgress();
            Toast.makeText(RequestDetailsGuestActivity.this,getResources().getString(R.string.coudlnt_complete_process),Toast.LENGTH_SHORT).show();
            AppConfigHelper.finishActivity(RequestDetailsGuestActivity.this);
        }
    }
    private void getEmployeeData(int employeeID)
    {
        try {
            DatabaseReference mData = FirebaseDatabase.getInstance().getReference().child(AppConfigHelper.EMPLOYEE_CHILD).child(AppConfigHelper.PROFILE_CHILD);
            mData.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                        if (Integer.parseInt(String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.ID_FIELD).getValue()))==employeeID) {
                            rate = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.RATE_FIELD).getValue());
                            reviews = Integer.parseInt(String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.REVIEWS_FIELD).getValue()));
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

}
