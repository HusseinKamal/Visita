package com.emergency.app.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.emergency.app.R;
import com.emergency.app.databinding.ActivityLoginBinding;
import com.emergency.app.models.User;
import com.emergency.app.ui.employee.HomeEmployeeActivity;
import com.emergency.app.ui.employee.RegisterEmployeeActivity;
import com.emergency.app.ui.guest.HomeGuestActivity;
import com.emergency.app.ui.guest.RegisterGuestActivity;
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
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    ActivityLoginBinding bind;
    private DatabaseReference mdata;
    private User objData;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new LanguageHelper().initLanguage(this,true);
        bind= DataBindingUtil.setContentView(this,R.layout.activity_login);
        bind.executePendingBindings();
        initViews();
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void initViews()
    {
        try {
            //change arrow in change language
            if (SharedPrefHelper.getSharedString(this, SharedPrefHelper.SHARED_PREFERENCE_LANGUAGE_KEY).equals(AppConfigHelper.ARABIC_LANGUAGE)) {
                bind.lyHeaderContainer.ivLang.setImageResource(R.drawable.ic_united_kingdom);
                bind.lyHeaderContainer.tvLang.setText(getResources().getString(R.string.english));
                bind.lyHeaderContainer.btnLang.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                LinearLayout.LayoutParams params =new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(3, 2, 3, 0);
                bind.lyHeaderContainer.tvLang.setLayoutParams(params);
                bind.edtPassword.setGravity(Gravity.RIGHT);

            } else {
                bind.lyHeaderContainer.ivLang.setImageResource(R.drawable.ic_kwait);
                bind.lyHeaderContainer.tvLang.setText(getResources().getString(R.string.arabic));
            }
            bind.edtEmail.setGravity(GravityCompat.START);
            //listeners
            bind.lyHeaderContainer.btnLang.setOnClickListener(this);
            bind.btnLogin.setOnClickListener(this);
            bind.btnGuestRegister.setOnClickListener(this);
            bind.btnEmployeeRegister.setOnClickListener(this);
            bind.lyHeaderContainer.btnSkip.setOnClickListener(this);
            //radio button change action listeners
            bind.rbGuest.setOnCheckedChangeListener((compoundButton, b) -> {
                bind.rbGuest.setChecked(b);
                bind.rbEmployee.setChecked(!b);

            });
            bind.rbEmployee.setOnCheckedChangeListener((compoundButton, b) -> {
                bind.rbGuest.setChecked(!b);
                bind.rbEmployee.setChecked(b);
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnLogin:
                try {
                    validateInput();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
            case R.id.btnGuestRegister:
                try {
                    AppConfigHelper.gotoActivity(this, RegisterGuestActivity.class,false);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
            case R.id.btnEmployeeRegister:
                try {
                    AppConfigHelper.gotoActivity(this, RegisterEmployeeActivity.class,false);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
            case R.id.btnSkip:
                try {
                    AppConfigHelper.gotoActivity(this, HomeGuestActivity.class,false);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
            case R.id.btnLang:
                try {
                  //change language
                    AppConfigHelper.startWithSelectedLanguage(this,LoginActivity.class,true);
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
            if(!ValidateData.isValid(bind.edtEmail.getText().toString()))
            {
                bind.edtEmail.requestFocus();
                bind.edtEmail.setError(getResources().getString(R.string.enter_empty_data),null);
            }
            else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(bind.edtEmail.getText().toString()).matches()){
                bind.edtEmail.requestFocus();
                bind.edtEmail.setError(getResources().getString(R.string.enter_valid_email),null);
            }
            else if(!ValidateData.isValid(bind.edtPassword.getText().toString()))
            {
                bind.edtPassword.requestFocus();
                bind.edtPassword.setError(getResources().getString(R.string.enter_empty_data),null);
            }
            else if(ValidateData.isValid(bind.edtPassword.getText().toString())&&bind.edtPassword.getText().toString().length()<AppConfigHelper.MIN_NUMBER_OF_PSSWORD)
            {
                bind.edtPassword.requestFocus();
                bind.edtPassword.setError(getResources().getString(R.string.enter_valid_password),null);
            }
            else if(!bind.rbGuest.isChecked()&&!bind.rbEmployee.isChecked())
            {
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.choose_valid_type), Toast.LENGTH_SHORT).show();
            }
            else
            {
                if(bind.rbGuest.isChecked()&&!bind.rbEmployee.isChecked()) {
                    //login guest
                    loginGuest();
                }
                else
                {
                    //login employee
                    loginEmployee();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void loginGuest()
    {
        try {
            CustomProgress.showProgress(this,getResources().getString(R.string.please_wait),true);
            mdata = FirebaseDatabase.getInstance().getReference().child(AppConfigHelper.GUEST_CHILD).child(AppConfigHelper.PROFILE_CHILD);
            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
                String deviceToken = task.getResult().getToken();
                if (!ValidateData.isValid(deviceToken)) {
                    deviceToken = "";
                }
                String finalDeviceToken = deviceToken;
                String token=generateToken();
                    mdata.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            boolean isLogin = false;
                            String name = "";
                            for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                                String email = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.EMAIL_FIELD).getValue());
                                String password = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.PASSWORD_FIELD).getValue());
                                if (email.equals(bind.edtEmail.getText().toString()) && password.equals(bind.edtPassword.getText().toString())) {
                                    isLogin = true;
                                    int id = Integer.parseInt(String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.ID_FIELD).getValue()));
                                    name = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.NAME_FIELD).getValue());
                                    //update device token
                                    mdata.child(String.valueOf(id)).child(AppConfigHelper.DEVICE_TOKEN_FIELD).setValue(finalDeviceToken);
                                    //update token
                                    mdata.child(String.valueOf(id)).child(AppConfigHelper.TOKEN_FIELD).setValue(token);
                                    String mobile = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.MOBILE_FIELD).getValue());
                                    String address = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.ADDRESS_FIELD).getValue());
                                    String type = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.TYPE_FIELD).getValue());
                                    String photo = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.PHOTO_FIELD).getValue());
                                    objData = new User(id, token, name, email, mobile, password, type, address, photo, finalDeviceToken);
                                    //save data in shared preference and go to home
                                    SharedPrefHelper.setSharedOBJECT(LoginActivity.this, SharedPrefHelper.SHARED_PREFERENCE_USER_DATA, objData);
                                }
                            }
                            if (isLogin) {
                                mdata.removeEventListener(this);
                                CustomProgress.hideProgress();
                                AppConfigHelper.gotoActivity(LoginActivity.this, HomeGuestActivity.class, true);
                                Toast.makeText(LoginActivity.this, getResources().getString(R.string.welcome) + " " + name, Toast.LENGTH_SHORT).show();
                            } else {
                                CustomProgress.hideProgress();
                                Toast.makeText(LoginActivity.this, getResources().getString(R.string.invalid_data), Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            CustomProgress.hideProgress();
                            Toast.makeText(LoginActivity.this, getResources().getString(R.string.coudlnt_complete_process), Toast.LENGTH_SHORT).show();
                        }
                    });

            });


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void loginEmployee() {
        CustomProgress.showProgress(this, getResources().getString(R.string.please_wait), true);
        mdata = FirebaseDatabase.getInstance().getReference().child(AppConfigHelper.EMPLOYEE_CHILD).child(AppConfigHelper.PROFILE_CHILD);
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            String deviceToken = task.getResult().getToken();
            if (!ValidateData.isValid(deviceToken)) {
                deviceToken = "";
            }
            String finalDeviceToken = deviceToken;
            String token=generateToken();
            mdata.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean isLogin = false;
                    String name = "";
                    for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                        String email = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.EMAIL_FIELD).getValue());
                        String password = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.PASSWORD_FIELD).getValue());
                        if (email.equals(bind.edtEmail.getText().toString()) && password.equals(bind.edtPassword.getText().toString())) {
                            isLogin = true;
                            int id = Integer.parseInt(String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.ID_FIELD).getValue()));
                            name = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.NAME_FIELD).getValue());
                            //update device token
                            mdata.child(String.valueOf(id)).child(AppConfigHelper.DEVICE_TOKEN_FIELD).setValue(finalDeviceToken);
                            //update token
                            mdata.child(String.valueOf(id)).child(AppConfigHelper.TOKEN_FIELD).setValue(token);
                            String job = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.JOB_FIELD).getValue());
                            String price = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.PRICE_FIELD).getValue());
                            String mobile = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.MOBILE_FIELD).getValue());
                            String address = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.ADDRESS_FIELD).getValue());
                            String type = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.TYPE_FIELD).getValue());
                            String photo = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.PHOTO_FIELD).getValue());
                            int order = Integer.parseInt(String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.ORDER_FIELD).getValue()));
                            int review =Integer.parseInt(String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.REVIEWS_FIELD).getValue()));
                            String rate = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.RATE_FIELD).getValue());
                            boolean isRecommend = (boolean)uniqueKeySnapshot.child(AppConfigHelper.RECOMMEND_FIELD).getValue();
                            int jobID = Integer.parseInt(String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.JOB_ID_FIELD).getValue()));
                            String location = String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.LOCATION_FIELD).getValue());
                            objData = new User(id, token, name, email, mobile, password, job, type, price, address, photo, finalDeviceToken,order,review,rate,isRecommend,jobID,location);
                            //save data in shared preference and go to home
                            SharedPrefHelper.setSharedOBJECT(LoginActivity.this, SharedPrefHelper.SHARED_PREFERENCE_USER_DATA, objData);
                        }
                    }
                    if (isLogin) {
                        mdata.removeEventListener(this);
                        CustomProgress.hideProgress();
                        AppConfigHelper.gotoActivity(LoginActivity.this, HomeEmployeeActivity.class, true);
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.welcome) + " " + name, Toast.LENGTH_SHORT).show();
                    } else {
                        CustomProgress.hideProgress();
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.invalid_data), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    CustomProgress.hideProgress();
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.coudlnt_complete_process), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
    private String generateToken()
    {
        Random random = new Random();
        String CHARS = "abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ";

        StringBuilder token = new StringBuilder(80);
        for (int i = 0; i < 80; i++) {
            token.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return token.toString();
    }

}
