package com.emergency.app.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.emergency.app.R;
import com.emergency.app.databinding.ActivitySplashBinding;
import com.emergency.app.models.User;
import com.emergency.app.ui.employee.HomeEmployeeActivity;
import com.emergency.app.ui.guest.HomeGuestActivity;
import com.emergency.app.util.appconfighelper.AppConfigHelper;
import com.emergency.app.util.appconfighelper.ValidateData;
import com.emergency.app.util.languagehelper.LanguageHelper;
import com.emergency.app.util.sharedprefrencehelper.SharedPrefHelper;

import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    ActivitySplashBinding bind;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new LanguageHelper().initLanguage(this,true);
        AppConfigHelper.makeFullScreen(this);
        bind= DataBindingUtil.setContentView(this,R.layout.activity_splash);
        bind.executePendingBindings();
        bind.lyContainer.setBackground(ContextCompat.getDrawable(this,R.drawable.ic_bg));
        final Handler handler = new Handler();
        handler.postDelayed(() -> getLocalLanguage(), 1500);
    }
    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void getLocalLanguage()
    {
        try {
            String lang= SharedPrefHelper.getSharedString(SplashActivity.this,SharedPrefHelper.SHARED_PREFERENCE_LANGUAGE_KEY);
            if(ValidateData.isValid(lang)) {
                if (lang.equals(AppConfigHelper.ARABIC_LANGUAGE)){
                    SharedPrefHelper.setSharedString(SplashActivity.this, SharedPrefHelper.SHARED_PREFERENCE_LANGUAGE_KEY, AppConfigHelper.ARABIC_LANGUAGE);
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                        getWindow().getDecorView().setLayoutDirection(View.TEXT_DIRECTION_LTR);

                    }


                } else{
                    SharedPrefHelper.setSharedString(SplashActivity.this, SharedPrefHelper.SHARED_PREFERENCE_LANGUAGE_KEY, AppConfigHelper.ENGLISH_LANGUAGE);
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                        getWindow().getDecorView().setLayoutDirection(View.TEXT_DIRECTION_RTL);
                    }
                }
            }
            else
            {
                //start with the default lang of the  device
                if(Locale.getDefault().equals(AppConfigHelper.ARABIC_LANGUAGE))
                {
                    SharedPrefHelper.setSharedString(SplashActivity.this, SharedPrefHelper.SHARED_PREFERENCE_LANGUAGE_KEY, AppConfigHelper.ARABIC_LANGUAGE);
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                        getWindow().getDecorView().setLayoutDirection(View.TEXT_DIRECTION_LTR);
                    }
                }
                else
                {
                    SharedPrefHelper.setSharedString(SplashActivity.this, SharedPrefHelper.SHARED_PREFERENCE_LANGUAGE_KEY, AppConfigHelper.ENGLISH_LANGUAGE);
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                        getWindow().getDecorView().setLayoutDirection(View.TEXT_DIRECTION_RTL);
                    }
                }
            }
            //check if user is login or not
            User user=(User) SharedPrefHelper.getSharedOBJECT(this,SharedPrefHelper.SHARED_PREFERENCE_USER_DATA);
            if(user!=null) {
                if (ValidateData.isValid(user.getToken())) {
                    if (ValidateData.isValid(user.getJob())) {
                        AppConfigHelper.gotoActivity(this, HomeEmployeeActivity.class, true);
                    } else {
                        AppConfigHelper.gotoActivity(this, HomeGuestActivity.class, true);
                    }
                } else {
                    AppConfigHelper.gotoActivity(this, LoginActivity.class, true);
                }
            }
            else
            {
                AppConfigHelper.gotoActivity(this, LoginActivity.class, true);
            }

        }
        catch (Exception objException)
        {
            SharedPrefHelper.setSharedOBJECT(this,SharedPrefHelper.SHARED_PREFERENCE_USER_DATA,new User());
            AppConfigHelper.gotoActivity(this, LoginActivity.class, true);
            //objException.printStackTrace();
        }
    }
}
