package com.emergency.app.ui.guest;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;

import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.emergency.app.R;
import com.emergency.app.adapters.CustomArrayAdapter;
import com.emergency.app.databinding.ActivityRegisterBinding;
import com.emergency.app.models.User;
import com.emergency.app.ui.LoginActivity;
import com.emergency.app.ui.employee.HomeEmployeeActivity;
import com.emergency.app.ui.employee.RegisterEmployeeActivity;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RegisterGuestActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityRegisterBinding bind;
    private CustomArrayAdapter adapterAddress;
    private ArrayList<String> addressList=new ArrayList<>();
    private DatabaseReference mdata;
    private boolean isRegister=false;
    private User objData;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new LanguageHelper().initLanguage(this,true);
        bind= DataBindingUtil.setContentView(this,R.layout.activity_register);
        bind.executePendingBindings();
        getArea();
        initViews();
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void initViews()
    {
        try {
            if (SharedPrefHelper.getSharedString(this, SharedPrefHelper.SHARED_PREFERENCE_LANGUAGE_KEY).equals(AppConfigHelper.ARABIC_LANGUAGE)) {
                bind.lyHeaderContainer.ivLang.setImageResource(R.drawable.ic_united_kingdom);
                bind.lyHeaderContainer.tvLang.setText(getResources().getString(R.string.english));
                bind.lyHeaderContainer.btnLang.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                LinearLayout.LayoutParams params =new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(3, 0, 3, 0);
                bind.lyHeaderContainer.tvLang.setLayoutParams(params);
                bind.edtPassword.setGravity(Gravity.RIGHT);

            } else {
                bind.lyHeaderContainer.ivLang.setImageResource(R.drawable.ic_kwait);
                bind.lyHeaderContainer.tvLang.setText(getResources().getString(R.string.arabic));
            }
            bind.edtEmail.setGravity(GravityCompat.START);
            //listeners
            bind.lyHeaderContainer.btnLang.setOnClickListener(this);
            bind.tvLogin.setOnClickListener(this);
            bind.btnRegister.setOnClickListener(this);
            bind.lyHeaderContainer.btnSkip.setOnClickListener(this);
            //radio button change action listeners
            bind.rbMale.setOnCheckedChangeListener((compoundButton, b) -> {
                bind.rbMale.setChecked(b);
                bind.rbFemale.setChecked(!b);

            });
            bind.rbFemale.setOnCheckedChangeListener((compoundButton, b) -> {
                bind.rbMale.setChecked(!b);
                bind.rbFemale.setChecked(b);
            });
            AppConfigHelper.setOffetsSpinner(bind.spCountry);
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
            case R.id.btnRegister:
                try {
                    validateInput();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
            case R.id.tvLogin:
                try {
                    AppConfigHelper.gotoActivity(this, LoginActivity.class,false);
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
                    AppConfigHelper.startWithSelectedLanguage(this, RegisterGuestActivity.class,true);
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
            if(!ValidateData.isValid(bind.edtFullName.getText().toString()))
            {
                bind.edtFullName.requestFocus();
                bind.edtFullName.setError(getResources().getString(R.string.enter_empty_data),null);
            }
            else if(!ValidateData.isValid(bind.edtEmail.getText().toString()))
            {
                bind.edtEmail.requestFocus();
                bind.edtEmail.setError(getResources().getString(R.string.enter_empty_data),null);
            }
            else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(bind.edtEmail.getText().toString()).matches()){
                bind.edtEmail.requestFocus();
                bind.edtEmail.setError(getResources().getString(R.string.enter_valid_email),null);
            }
            else if(!ValidateData.isValid(bind.edtPhone.getText().toString()))
            {
                bind.edtPhone.requestFocus();
                bind.edtPhone.setError(getResources().getString(R.string.enter_empty_data),null);
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
            else if(!bind.rbMale.isChecked()&&!bind.rbFemale.isChecked())
            {
                bind.rbMale.requestFocus();
                bind.rbFemale.requestFocus();
                Toast.makeText(this,getResources().getString(R.string.choose_valid_type),Toast.LENGTH_SHORT).show();
            }
            else
            {
                //register
                register();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void register()
    {
        try {
            isRegister=true;
            Map<String, Object> userMap = new HashMap<>();
            CustomProgress.showProgress(this,getResources().getString(R.string.please_wait),true);
            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
                String device_token = task.getResult().getToken();
                if (!ValidateData.isValid(device_token)) {
                    device_token = "";
                }
                //Add Employee to FireBase
                mdata = FirebaseDatabase.getInstance().getReference().child(AppConfigHelper.GUEST_CHILD).child(AppConfigHelper.PROFILE_CHILD);
                // String finalDevice_token = device_token;
                String finalDevice_token = device_token;
                mdata.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int id;
                        String type, token;
                        boolean isUniqueEmail=true;
                        if(isRegister) {
                            for(DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()){
                                String email=String.valueOf(uniqueKeySnapshot.child(AppConfigHelper.EMAIL_FIELD).getValue());
                                if(email.equals(bind.edtEmail.getText().toString()))
                                {
                                    isUniqueEmail=false;
                                }
                                else
                                {
                                    isUniqueEmail=true;
                                }
                            }
                            if(isUniqueEmail) {
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
                                DatabaseReference mdata1 = FirebaseDatabase.getInstance().getReference().child(AppConfigHelper.GUEST_CHILD).child(AppConfigHelper.PROFILE_CHILD).child(String.valueOf(id));
                                token = generateToken();
                                userMap.put(AppConfigHelper.ID_FIELD, id);
                                userMap.put(AppConfigHelper.TOKEN_FIELD, token);
                                userMap.put(AppConfigHelper.NAME_FIELD, bind.edtFullName.getText().toString());
                                userMap.put(AppConfigHelper.EMAIL_FIELD, bind.edtEmail.getText().toString());
                                userMap.put(AppConfigHelper.PASSWORD_FIELD, bind.edtPassword.getText().toString());
                                userMap.put(AppConfigHelper.DEVICE_TOKEN_FIELD, finalDevice_token);
                                userMap.put(AppConfigHelper.MOBILE_FIELD, bind.edtPhone.getText().toString());
                                userMap.put(AppConfigHelper.ADDRESS_FIELD, bind.spCountry.getSelectedItem().toString());
                                userMap.put(AppConfigHelper.TYPE_FIELD, type);
                                userMap.put(AppConfigHelper.PHOTO_FIELD, "");
                                mdata1.setValue(userMap);
                                objData= new User(Integer.parseInt(String.valueOf(id)), token, bind.edtFullName.getText().toString(), bind.edtEmail.getText().toString(), bind.edtPhone.getText().toString()
                                        , bind.edtPassword.getText().toString(), type,
                                        bind.spCountry.getSelectedItem().toString(), "", finalDevice_token);
                                //save data in shared preference and go to home
                                SharedPrefHelper.setSharedOBJECT(RegisterGuestActivity.this, SharedPrefHelper.SHARED_PREFERENCE_USER_DATA, objData);
                                AppConfigHelper.gotoActivity(RegisterGuestActivity.this, HomeGuestActivity.class,true);
                                Toast.makeText(RegisterGuestActivity.this, getResources().getString(R.string.register_done), Toast.LENGTH_SHORT).show();
                                mdata1.removeEventListener(this);
                                isRegister = false;
                            }
                            else
                            {
                                Toast.makeText(RegisterGuestActivity.this, getResources().getString(R.string.email_unique), Toast.LENGTH_SHORT).show();
                            }
                            CustomProgress.hideProgress();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        CustomProgress.hideProgress();
                        Toast.makeText(RegisterGuestActivity.this,getResources().getString(R.string.coudlnt_complete_process),Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
    private void getArea()
    {
        try {
            DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child(AppConfigHelper.AREA_CHILD);
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
                public void onCancelled(@NonNull DatabaseError databaseError) {
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
}
