package com.emergency.app.ui.guest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.emergency.app.R;
import com.emergency.app.adapters.CustomArrayAdapter;
import com.emergency.app.databinding.ActivityAddRequestBinding;
import com.emergency.app.models.User;
import com.emergency.app.util.appconfighelper.AppConfigHelper;
import com.emergency.app.util.appconfighelper.ValidateData;
import com.emergency.app.util.dialogshelper.CustomProgress;
import com.emergency.app.util.languagehelper.LanguageHelper;
import com.emergency.app.util.sharedprefrencehelper.SharedPrefHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddRequestActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityAddRequestBinding bind;
    private Calendar objCalendar;
    private int year, month, day,guestID,employeeID,reviews,orders;
    private String date,time,employeeName,guestName,employeeJob,rate,employeePhoto,guestPhoto,employeeMobile,price,deviceToken;
    private CustomArrayAdapter adapterAddress;
    private ArrayList<String> addressList=new ArrayList<>();
    private DatabaseReference mdata;
    private User user;
    private boolean isSendRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new LanguageHelper().initLanguage(this,true);
        bind= DataBindingUtil.setContentView(this,R.layout.activity_add_request);
        bind.executePendingBindings();
        getArea();
        initViews();
        getEmployeeData();
    }
    private void initViews()
    {
        try {
            employeeID=getIntent().getExtras().getInt(AppConfigHelper.EMPLOYEE_ID_INTENT);
            user=(User) SharedPrefHelper.getSharedOBJECT(this,SharedPrefHelper.SHARED_PREFERENCE_USER_DATA);
            guestName=user.getName();
            guestID=user.getId();
            guestPhoto=user.getPhoto();
            if(AppConfigHelper.screenDimensions(this).x>700) {
                bind.card.getLayoutParams().height = (int) (AppConfigHelper.screenDimensions(this).y * .15);
            }
            else
            {
                bind.card.getLayoutParams().height = (int) (AppConfigHelper.screenDimensions(this).y * .3);
            }
            bind.lyHeaderContainer.tvMainTitle.setText(getResources().getString(R.string.add_request));
            //radio button change action listeners
            bind.rbMale.setOnCheckedChangeListener((compoundButton, b) -> {
                bind.rbMale.setChecked(b);
                bind.rbFemale.setChecked(!b);

            });
            bind.rbFemale.setOnCheckedChangeListener((compoundButton, b) -> {
                bind.rbMale.setChecked(!b);
                bind.rbFemale.setChecked(b);
            });
            //listeners
            bind.btnSubmit.setOnClickListener(this);
            bind.lyHeaderContainer.ivBack.setOnClickListener(this);
            bind.tvDateTime.setOnClickListener(this);

            objCalendar = Calendar.getInstance();
            year = objCalendar.get(Calendar.YEAR);
            month = objCalendar.get(Calendar.MONTH);
            day = objCalendar.get(Calendar.DAY_OF_MONTH);
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
            case R.id.btnSubmit:
                try {
                    validateInput();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
            case R.id.tvDateTime:
                try {
                    showDatePickerDialog();
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
    private void validateInput()
    {
        try
        {
            if(!ValidateData.isValid(bind.edtDesc.getText().toString()))
            {
                bind.edtDesc.requestFocus();
                bind.edtDesc.setError(getResources().getString(R.string.enter_empty_data),null);
            }
            else if(!ValidateData.isValid(bind.tvDateTime.getText().toString()))
            {
                bind.tvDateTime.requestFocus();
                bind.tvDateTime.setError(getResources().getString(R.string.enter_empty_data),null);
            }
            else if(!bind.rbMale.isChecked()&&!bind.rbFemale.isChecked())
            {
                bind.rbMale.requestFocus();
                bind.rbFemale.requestFocus();
                Toast.makeText(this,getResources().getString(R.string.choose_valid_type),Toast.LENGTH_SHORT).show();
            }
            else
            {
                //Add request
                addRequest();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void showDatePickerDialog() {
        try {

            DatePickerDialog objDatePickerDialog = new DatePickerDialog(this,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        date = year + "-" + AppConfigHelper.addLeftzer0("" + (monthOfYear + 1)) + "-" + dayOfMonth;
                        showTimePickerDialog(date);
                    }, year, month, day);
            objDatePickerDialog.setTitle(getResources().getString(R.string.select_date));
            Calendar calendar = Calendar.getInstance();
            objDatePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            objDatePickerDialog.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void showTimePickerDialog(String date)
    {
        try {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(this, (timePicker, selectedHour, selectedMinute) -> {
                time=AppConfigHelper.addLeftzer0(String.valueOf(selectedHour)) + ":" + AppConfigHelper.addLeftzer0(String.valueOf(selectedMinute));
                bind.tvDateTime.setText(date+" "+time);
            }, hour, minute, true);//Yes 24 hour time
            mTimePicker.setTitle(getResources().getString(R.string.select_time));
            mTimePicker.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void getArea()
    {
        try {
            DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child(AppConfigHelper.AREA_CHILD);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                        Map<String,Object> area =(Map<String,Object>) postSnapshot.getValue();
                        addressList.add(area.get("name").toString());
                        fillArea();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    addressList.add("Hawally");
                    addressList.add("Faroniea");
                    fillArea();
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void fillArea()
    {
        try {
            adapterAddress=new CustomArrayAdapter(R.layout.item_spinner_layout,this,addressList);
            bind.spCountry.setAdapter(adapterAddress);
            bind.barLoad.setVisibility(View.GONE);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void getEmployeeData()
    {
        try {
            DatabaseReference employeeData = FirebaseDatabase.getInstance().getReference().child(AppConfigHelper.EMPLOYEE_CHILD).child(AppConfigHelper.PROFILE_CHILD);
            employeeData.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                        int idEmp=Integer.parseInt(String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.ID_FIELD).getValue()));
                        if (idEmp==employeeID){
                            employeeName = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.NAME_FIELD).getValue());
                            price = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.PRICE_FIELD).getValue());
                            rate = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.RATE_FIELD).getValue());
                            reviews = Integer.parseInt(String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.REVIEWS_FIELD).getValue()));
                            orders = Integer.parseInt(String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.ORDER_FIELD).getValue()));
                            employeeJob = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.JOB_FIELD).getValue());
                            employeePhoto = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.PHOTO_FIELD).getValue());
                            employeeMobile=String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.MOBILE_FIELD).getValue());
                            String employeeType = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.TYPE_FIELD).getValue());
                            deviceToken=String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.DEVICE_TOKEN_FIELD).getValue());
                            bind.tvName.setText(employeeName);
                            bind.tvJob.setText(employeeJob);
                            bind.tvOrders.setText(orders+" "+getResources().getString(R.string.order));
                            bind.tvPrice.setText(price+" "+getResources().getString(R.string.kwd));
                            bind.tvReviews.setText(reviews+"");
                            bind.tvRate.setText(rate);
                            if(ValidateData.isValid(employeePhoto))
                            {
                                Picasso.with(AddRequestActivity.this).load(employeePhoto).placeholder(R.drawable.ic_girl).into(bind.profileImage);
                            }
                            else
                            {
                                if(employeeType.equals(getResources().getString(R.string.female))) {
                                    bind.profileImage.setImageResource(R.drawable.ic_girl);
                                }
                                else
                                {
                                    bind.profileImage.setImageResource(R.drawable.ic_man);
                                }
                            }
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(AddRequestActivity.this,getResources().getString(R.string.coudlnt_complete_process),Toast.LENGTH_SHORT).show();
                    AppConfigHelper.finishActivity(AddRequestActivity.this);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
            CustomProgress.hideProgress();
        }
    }
    private void addRequest()
    {
        try {
            Map<String, Object> requestMap = new HashMap<>();
            isSendRequest=true;
            CustomProgress.showProgress(this,getResources().getString(R.string.please_wait),false);
            mdata = FirebaseDatabase.getInstance().getReference().child(AppConfigHelper.REQUEST_CHILD);
            mdata.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int id;
                    String type;
                    if(isSendRequest) {
                        if (dataSnapshot.getChildrenCount() > 0) {
                            id = Integer.parseInt(String.valueOf(dataSnapshot.getChildrenCount() + 1));
                        } else {
                            id = 1;
                        }
                        //gender
                        if (bind.rbMale.isChecked()) {
                            type = getResources().getString(R.string.male);
                        } else {
                            type = getResources().getString(R.string.female);
                        }
                        DatabaseReference mdata1 = FirebaseDatabase.getInstance().getReference().child(AppConfigHelper.REQUEST_CHILD).child(String.valueOf(id));
                        requestMap.put(AppConfigHelper.ID_REQUEST_FIELD, id);
                        requestMap.put(AppConfigHelper.GUEST_ID_FIELD, guestID);
                        requestMap.put(AppConfigHelper.EMPLOYEE_ID_FIELD, employeeID);
                        requestMap.put(AppConfigHelper.ADDRESS_REQUEST_FIELD, bind.spCountry.getSelectedItem().toString());
                        requestMap.put(AppConfigHelper.DESCRIPTION_REQUEST_FIELD, bind.edtDesc.getText().toString());
                        requestMap.put(AppConfigHelper.GUEST_TYPE_FIELD, type);
                        requestMap.put(AppConfigHelper.REQUEST_TYPE_FIELD, employeeJob);
                        requestMap.put(AppConfigHelper.REPORT_FIELD, "");
                        requestMap.put(AppConfigHelper.DATETIME_REQUEST_FIELD, bind.tvDateTime.getText().toString());
                        requestMap.put(AppConfigHelper.STATUS_REQUEST_FIELD, AppConfigHelper.WAITING_STATUS);
                        requestMap.put(AppConfigHelper.PRICE_REQUEST_FIELD, price);
                        requestMap.put(AppConfigHelper.EMP_MOBILE_FIELD, employeeMobile);
                        requestMap.put(AppConfigHelper.EMP_JOB_FIELD, employeeJob);
                        requestMap.put(AppConfigHelper.EMP_NAME_FIELD, employeeName);
                        requestMap.put(AppConfigHelper.GUEST_NAME_FIELD, guestName);
                        requestMap.put(AppConfigHelper.EMPLOYEE_PHOTO_FIELD, employeePhoto);
                        requestMap.put(AppConfigHelper.GUEST_PHOTO_FIELD, guestPhoto);
                        requestMap.put(AppConfigHelper.IS_REVIEW_FIELD,false);//if user made a review and rate for order or not then default is false
                        mdata1.setValue(requestMap);
                        isSendRequest=false;
                        sendNotification(id);//send push notification to employee
                        Toast.makeText(AddRequestActivity.this, getResources().getString(R.string.request_done), Toast.LENGTH_SHORT).show();
                        CustomProgress.hideProgress();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    CustomProgress.hideProgress();
                    Toast.makeText(AddRequestActivity.this,getResources().getString(R.string.coudlnt_complete_process),Toast.LENGTH_SHORT).show();
                    AppConfigHelper.finishActivity(AddRequestActivity.this);
                }
            });
        }
        catch (Exception e)
        {
            CustomProgress.hideProgress();
            e.printStackTrace();
        }
    }
    private void sendNotification(int requestId)
    {
        try {
            HashMap<String,String> map= new HashMap<>();
            map.put(AppConfigHelper.ID_REQUEST_FIELD,String.valueOf(requestId));
            map.put(AppConfigHelper.EMP_NAME_FIELD,employeeName);
            map.put(AppConfigHelper.GUEST_NAME_FIELD,guestName);
            map.put(AppConfigHelper.STATUS_REQUEST_FIELD,AppConfigHelper.WAITING_STATUS);
            map.put(AppConfigHelper.DEVICE_TOKEN_FIELD,deviceToken);
            map.put(AppConfigHelper.REQUEST_TYPE_FIELD,employeeJob+" "+getResources().getString(R.string.request));
            map.put(AppConfigHelper.IS_GUEST_FIELD,AppConfigHelper.GUEST);
            AppConfigHelper.sendPushToSingleInstance(this,map);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
