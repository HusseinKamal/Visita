package com.emergency.app.util.permissionhepler;

import android.app.Activity;

import android.Manifest;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.emergency.app.util.appconfighelper.AppConfigHelper;

/**
 * Created by Hussein Kamal
 */

public class AndroidPermission {
    public static final int RECORD_PERMISSION_REQUEST_CODE = 3;
    Activity activity;

    public AndroidPermission(Activity activity) {
        this.activity = activity;
    }

    public boolean checkPermissionForRecord(){
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }

    public boolean checkPermissionForExternalStorage(){
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }

    public boolean checkPermissionForCamera(){
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }

    public void requestPermissionForRecord(){
        try {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_PERMISSION_REQUEST_CODE);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void requestPermissionForExternalStorage(){
         try{
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},AppConfigHelper.EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
         }
         catch (Exception e)
         {
             e.printStackTrace();
         }
    }

    public void requestPermissionForCamera(){
        try {
          ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.CAMERA}, AppConfigHelper.CAMERA_PERMISSION_REQUEST_CODE);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
