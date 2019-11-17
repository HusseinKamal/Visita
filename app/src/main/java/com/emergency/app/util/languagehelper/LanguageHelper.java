package com.emergency.app.util.languagehelper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.util.Log;
import android.view.View;

import com.emergency.app.util.sharedprefrencehelper.SharedPrefHelper;

import java.util.Locale;

public class LanguageHelper {

    public static String getCurrentLanguage(Context context) {
        String value = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).getString(SharedPrefHelper.SHARED_PREFERENCE_LANGUAGE_KEY, "en");
        Log.v("language", value);
        return value;
    }

    public void initLanguage(Activity context,boolean enforceRtl) {
        String currentLanguage = context.getBaseContext().getResources().getConfiguration().locale.getLanguage();
        String language = new SharedPrefHelper().getSharedString(context, SharedPrefHelper.SHARED_PREFERENCE_LANGUAGE_KEY);
        Log.v("lang",language);
        if(!currentLanguage.equalsIgnoreCase(language)) {
            changeLanguage(context, enforceRtl, language);
        }
    }

    public void changeLanguage(Activity context, boolean enforceRtl, String newLanguage) {
        SharedPrefHelper.setSharedString(context, SharedPrefHelper.SHARED_PREFERENCE_LANGUAGE_KEY, newLanguage);
        Locale locale = new Locale(newLanguage);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getBaseContext().getResources().updateConfiguration(config,
                context.getBaseContext().getResources().getDisplayMetrics());
        if (enforceRtl) {
            if (newLanguage.equalsIgnoreCase("ar") || newLanguage.equalsIgnoreCase("ur"))
                this.forceRTLIfSupported(context, true);

        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void forceRTLIfSupported(Activity context,boolean replace)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && replace)
            context.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !replace)
            context.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
    }


//    public void changeLanguage(Activity context,String newLanguage) {
//        SharedPrefHelper.setSharedString(context, SharedPrefHelper.SHARED_PREFERENCE_LANGUAGE_KEY, newLanguage);
//    }
}
