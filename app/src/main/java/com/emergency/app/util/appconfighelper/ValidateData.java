package com.emergency.app.util.appconfighelper;

import android.text.TextUtils;

/**
 * Created by Hussein Kamal on 15/05/2018.
 */

public class ValidateData {
    public static boolean isValid(String text)
    {
        if(text!=null) {
            return !text.equals("") && !TextUtils.isEmpty(text) && text.length() > 0;
        }
        else
        {
            return false;
        }
    }
}
