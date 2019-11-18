package com.emergency.app.util.appconfighelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.emergency.app.R;
import com.emergency.app.util.sharedprefrencehelper.SharedPrefHelper;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class AppConfigHelper {
    public static final String ARABIC_LANGUAGE = "ar";
    public static final String ENGLISH_LANGUAGE = "en";

    public static final String MESSAAGE_NOTIFY_CODE = "1";
    public static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
    public static final String APP_SERVER_KEY = "AAAAp8I0dH0:APA91bH-7XEgoz9hYHgsw4GAKAa0RgMEIAUOtQ3Tk0ZsNpfVag_hI836Z5ggTJB-EvHLFFz-sGQWBk131jT6KFV1PPzu3N08_v_bTqm5CraBMJUYG2oJdZrPw-pBbty4hTEBHPpxvldh";

    public static final String GUEST_CHILD = "Guest";
    public static final String EMPLOYEE_CHILD = "Employee";
    public static final String AREA_CHILD = "Area";
    public static final String PROFILE_CHILD = "Profile";
    public static final String REQUEST_CHILD = "Request";
    public static final String COMMENT_CHILD = "Comment";

    //storage permission code
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 1;
    public static final int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 2;

    public static final int MIN_NUMBER_OF_PSSWORD = 6;

    //BottomNavMenu
    public static final int HOME_ID = 0;
    public static final int PROFILE_ID = 1;
    public static final int ORDER_ID = 2;
    public static final int MORE_ID = 3;
    public static final int ABOUT_ID = 4;

    //Job ID
    public static final int NURSE_JOB_ID = 1;
    public static final int PHYSIOTHERAPY_JOB_ID = 2;
    public static final int BABY_SITTER_JOB_ID = 3;


    public static final int PHONE_NMBER = 14789;
    //intent
    public static final String REQUEST_ID_INTENT ="requestID";
    public static final String EMPLOYEE_ID_INTENT = "EmployeeID";
    public static final String EMPLOYEE_NAME_INTENT = "EmployeeName";
    public static final String EMPLOYEE_JOB_ID_INTENT = "employeeJobID";
    public static final String FILTER_INTENT = "filter";

    //FireBase User Fields
    public static final String ID_FIELD ="id";
    public static final String TOKEN_FIELD ="token";
    public static final String NAME_FIELD ="name";
    public static final String EMAIL_FIELD ="email";
    public static final String PASSWORD_FIELD="password";
    public static final String PRICE_FIELD="price";
    public static final String ADDRESS_FIELD="address";
    public static final String TYPE_FIELD="type";
    public static final String MOBILE_FIELD ="mobile";
    public static final String DEVICE_TOKEN_FIELD ="deviceToken";
    public static final String JOB_FIELD ="job";
    public static final String JOB_ID_FIELD ="jobID";
    public static final String PHOTO_FIELD ="photo";
    public static final String RATE_FIELD ="rate";
    public static final String REVIEWS_FIELD ="reviews";
    public static final String RECOMMEND_FIELD ="recommend";
    public static final String ORDER_FIELD ="order";
    public static final String LOCATION_FIELD ="location";
    public static final String MESSAGE_FIELD ="message";
    public static final String TIME_FIELD ="time";
    //FireBase Request Fields
    public static final String ID_REQUEST_FIELD ="requestId";
    public static final String GUEST_ID_FIELD ="guestID";
    public static final String EMPLOYEE_ID_FIELD ="employeeID";
    public static final String ADDRESS_REQUEST_FIELD="requestAddress";
    public static final String DESCRIPTION_REQUEST_FIELD="requestDescription";
    public static final String GUEST_TYPE_FIELD ="guestType";
    public static final String REQUEST_TYPE_FIELD="requestType";
    public static final String REPORT_FIELD="report";
    public static final String DATETIME_REQUEST_FIELD="dateTime";
    public static final String STATUS_REQUEST_FIELD ="status";
    public static final String PRICE_REQUEST_FIELD ="price";
    public static final String EMP_MOBILE_FIELD ="employeeMobile";
    public static final String EMP_JOB_FIELD ="employeeJob";
    public static final String EMP_NAME_FIELD ="employeeName";
    public static final String GUEST_NAME_FIELD ="guestName";
    public static final String EMPLOYEE_PHOTO_FIELD ="employeePhoto";
    public static final String GUEST_PHOTO_FIELD ="guestPhoto";
    public static final String IS_REVIEW_FIELD ="isReview";

    //FireBase request status
    public static final String WAITING_STATUS ="Waiting";
    public static final String ACCEPT_STATUS ="Accepted";
    public static final String REJECT_STATUS ="Rejected";
    public static final String START_STATUS ="Started";
    public static final String FINISH_STATUS ="Finished";

    public static final String IS_GUEST_FIELD ="isGuest";
    public static final String GUEST ="guest";
    public static final String EMPLOYEE ="employee";

    //Dialogs
    public static final String FILTER_DIALOG_TAG="filterDialogTAG";
    public static final String REPORT_DIALOG_TAG="reportDialogTAG";
    public static final String ADD_MESSAGE_DIALOG_TAG="addMessageDialogTAG";
    public static final String RATE_DIALOG_TAG="rateDialogTAG";


    public static String getStatus(Context context, String status) {
        try {
            String finalStatus = null;
            switch (status)
            {
                case WAITING_STATUS:
                    finalStatus=context.getResources().getString(R.string.waiting);
                    break;
                case ACCEPT_STATUS:
                    finalStatus=context.getResources().getString(R.string.accepted);
                break;
                case REJECT_STATUS:
                    finalStatus=context.getResources().getString(R.string.rejected);
                break;
                case START_STATUS:
                    finalStatus=context.getResources().getString(R.string.started);
                break;
                case FINISH_STATUS:
                    finalStatus=context.getResources().getString(R.string.finished);
                break;
            }
            return finalStatus;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }
    public static Drawable getBackground(Context context, String status) {
        try {
            Drawable finalStatus=null;
            switch (status)
            {
                case WAITING_STATUS:
                    finalStatus=ContextCompat.getDrawable(context,R.drawable.ic_offer_card);
                    break;
                case ACCEPT_STATUS:
                case START_STATUS:
                    finalStatus=ContextCompat.getDrawable(context,R.drawable.ic_offer_card2);
                    break;
                case REJECT_STATUS:
                case FINISH_STATUS:
                    finalStatus=ContextCompat.getDrawable(context,R.drawable.ic_offer_card3);
                    break;
            }
            return finalStatus;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    public static boolean isOnline(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        return !(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable());
    }

    public static void makeFullScreen(Activity activity) {
       try {
           View decorView = activity.getWindow().getDecorView();
           decorView.setSystemUiVisibility(
                   View.SYSTEM_UI_FLAG_IMMERSIVE
                           // Set the content to appear under the system bars so that the
                           // content doesn't resize when the system bars hide and show.
                           | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                           | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                           | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                           // Hide the nav bar and status bar
                           | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                           | View.SYSTEM_UI_FLAG_FULLSCREEN);

       }
       catch (Exception e)
       {
           e.printStackTrace();
       }
    }
/*
    public static void setLayoutAnim_slidedownfromtop(ViewGroup panel, Context ctx) {
        try {
            AnimationSet set = new AnimationSet(true);

            Animation animation = new AlphaAnimation(0.0f, 1.0f);
            animation.setDuration(100);
            set.addAnimation(animation);

            animation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f
            );
            animation.setDuration(500);
            set.addAnimation(animation);

            LayoutAnimationController controller =
                    new LayoutAnimationController(set, 0.25f);
            panel.setLayoutAnimation(controller);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static void setLayoutAnim_slideLeftfromRight(ViewGroup panel, Context ctx) {
        try {
            AnimationSet set = new AnimationSet(true);

            Animation animation = new AlphaAnimation(0.0f, 1.0f);
            animation.setDuration(100);
            set.addAnimation(animation);

            animation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f
            );
            animation.setDuration(500);
            set.addAnimation(animation);

            LayoutAnimationController controller =
                    new LayoutAnimationController(set, 0.25f);
            panel.setLayoutAnimation(controller);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static void setLayoutAnim_slideRightfromLeft(ViewGroup panel, Context ctx) {
        try {
            AnimationSet set = new AnimationSet(true);

            Animation animation = new AlphaAnimation(0.0f, 1.0f);
            animation.setDuration(100);
            set.addAnimation(animation);

            animation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f
            );
            animation.setDuration(500);
            set.addAnimation(animation);

            LayoutAnimationController controller =
                    new LayoutAnimationController(set, 0.25f);
            panel.setLayoutAnimation(controller);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static void setLayoutAnim_slideup(ViewGroup panel, Context ctx) {
        try {
            AnimationSet set = new AnimationSet(true);

            *//*
             * Animation animation = new AlphaAnimation(1.0f, 0.0f);
             * animation.setDuration(200); set.addAnimation(animation);
             *//*

            Animation animation = new AlphaAnimation(0.0f, 1.0f);
            animation.setDuration(100);
            set.addAnimation(animation);

            animation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                    0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, -1.0f);
            animation.setDuration(500);
            set.addAnimation(animation);
            animation.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    // MapContacts.this.mapviewgroup.setVisibility(View.INVISIBLE);

                }
            });
            set.addAnimation(animation);

            LayoutAnimationController controller = new LayoutAnimationController(
                    set, 0.25f);
            panel.setLayoutAnimation(controller);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

    }*/
    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
    public static void makeTransparent(Activity activity)
    {
        //make status bar transparent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = activity.getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.TYPE_STATUS_BAR_PANEL);
        }
/*
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);*/
    }
 /*   @SuppressLint("SetWorldReadable")
    public static void share(Context context, View ivPhoto, String restName, String reserveDate, String reserveTime, String reservePeople, String reservePoints, String offer) {
        try {
            File file = null;
            Uri photoURI = null;
            if (ContextCompat.checkSelfPermission(context,android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //File write logic here
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            }
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //File write logic here
                ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
            }
            final String APP_LINK = "http://play.google.com/store/apps/details?id=" + context.getPackageName();
            String restaurantName = restName;
            String reservationTime=reserveTime;
            String reservationDate=reserveDate;
            String reservationPeopleNum=reservePeople;
            String reservationPoints=reservePoints;
            String shareData;
            if(!ValidateData.isValid(offer)) {
                shareData = context.getResources().getString(R.string.details_reservation) + ":\n" + restaurantName + "\n" + reservationTime + " " + reservationDate + "\n" + reservationPeopleNum.replaceAll(",","");
            }
            else
            {
                shareData=offer;
            }
            String photoName;
            Bitmap bitmap =AppConfigHelper.getBitmapFromView(ivPhoto);
            if(bitmap!=null) {
                if (restaurantName.length() > 5) {
                    photoName = restaurantName.substring(0, 5);
                } else {
                    photoName = restaurantName;
                }
                file = new File(context.getExternalCacheDir(), photoName + ".png");
                FileOutputStream fOut = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                fOut.flush();
                fOut.close();
                file.setReadable(true, false);
                photoURI = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
            }
                final Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if(file!=null) {
                    intent.putExtra(Intent.EXTRA_STREAM, photoURI);
                }
                intent.putExtra(Intent.EXTRA_TEXT, shareData + "\n"+context.getResources().getString(R.string.download_app)+": " + APP_LINK);
                intent.setType("image/*");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(Intent.createChooser(intent, context.getResources().getString(R.string.share_via)));

            // new downloadImage(this,photoURL).execute();
        } catch (Exception objException) {
            objException.printStackTrace();
            Toast.makeText(context,context.getResources().getString(R.string.invalid_data_share),Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("SetWorldReadable")
    public static void shareEvent(Context context, View ivPhoto,String eventName, String restName, String reserveDate, String time) {
        try {
            Uri photoURI = null;
            File file;
            if (ContextCompat.checkSelfPermission(context,android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //File write logic here
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            }
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //File write logic here
                ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
            }
            final String APP_LINK = "http://play.google.com/store/apps/details?id=" + context.getPackageName();
            String shareData = eventName + "\n" + restName + "\n " +time+ "\n"+reserveDate;

            Bitmap bitmap =AppConfigHelper.getBitmapFromView(ivPhoto);
            try {
                file = new File(context.getExternalCacheDir(),restName+".png");
                FileOutputStream fOut = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                fOut.flush();
                fOut.close();
                file.setReadable(true, false);
                photoURI = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
                final Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if(file!=null) {
                    intent.putExtra(Intent.EXTRA_STREAM, photoURI);
                }
                intent.putExtra(Intent.EXTRA_TEXT, shareData + "\n"+context.getResources().getString(R.string.download_app)+": " + APP_LINK);
                intent.setType("image/*");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(Intent.createChooser(intent, context.getResources().getString(R.string.share_via)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            // new downloadImage(this,photoURL).execute();
        } catch (Exception objException) {
            objException.printStackTrace();
            Toast.makeText(context,context.getResources().getString(R.string.invalid_data_share),Toast.LENGTH_LONG).show();
        }
    }
    public static void pay(Activity activity, Double payValue, String restaurantID, String reservationID,boolean isAutoAccept) {
        try {
            User user=(User)SharedPrefHelper.getSharedOBJECT(activity,SharedPrefHelper.SHARED_PREFERENCE_USER_DATA);
            if(ValidateData.isValid(user.getToken())) {
                String payURL = AppConfigHelper.PAYMENT_URL + user.getToken() + "&amount=" + payValue + "&restaurant_id=" + restaurantID + "&reservation_id=" + reservationID;
                Intent objIntent = new Intent(activity, PaymentActivity.class);
                objIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                objIntent.putExtra(AppConfigHelper.PAYMENT_INTENT, payURL);
                objIntent.putExtra(AppConfigHelper.RESERVATION_KEY_ID, reservationID);
                objIntent.putExtra(AppConfigHelper.RESERVATION_ACCEPT_PAYMENT, isAutoAccept);
                activity.startActivity(objIntent);
                if(isAutoAccept)
                {
                    activity.finish();
                }
            }
            else
            {
                Toast.makeText(activity,activity.getResources().getString(R.string.couldnt_complete_process),Toast.LENGTH_SHORT).show();
            }
        } catch (Exception objException) {
            objException.printStackTrace();
        }
    }
    public static void payWebView(Activity activity, Double payValue, String restaurantID, String reservationID,String paymentToken,boolean isAutoAccept) {
        try {
            User user=(User)SharedPrefHelper.getSharedOBJECT(activity,SharedPrefHelper.SHARED_PREFERENCE_USER_DATA);
            if(ValidateData.isValid(user.getToken())) {
                String payURL=AppConfigHelper.PAYMENT_WEBVIEW_URL+paymentToken;
                //String payURL = AppConfigHelper.PAYMENT_URL + user.getToken() + "&amount=" + payValue + "&restaurant_id=" + restaurantID + "&reservation_id=" + reservationID;
                Intent objIntent = new Intent(activity, PaymentActivity.class);
                objIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                objIntent.putExtra(AppConfigHelper.PAYMENT_INTENT, payURL);
                objIntent.putExtra(AppConfigHelper.RESERVATION_KEY_ID, reservationID);
                objIntent.putExtra(AppConfigHelper.RESERVATION_ACCEPT_PAYMENT, isAutoAccept);
                activity.startActivity(objIntent);
                if(isAutoAccept)
                {
                    activity.finish();
                }
            }
            else
            {
                Toast.makeText(activity,activity.getResources().getString(R.string.couldnt_complete_process),Toast.LENGTH_SHORT).show();
            }
        } catch (Exception objException) {
            objException.printStackTrace();
        }
    }*/
   /* public static void slidingAnimation(Context context, RecyclerView recyclerView)
    {
        try{
            int resId = R.anim.layout_animation_fall_down;
            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(context, resId);
            recyclerView.setLayoutAnimation(animation);
            recyclerView.scheduleLayoutAnimation();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }*/

    public static String addLeftzer0(String number) {
        if (number.length() == 1)
            return "0" + number;
        else
            return number;
    }
    public static void gotoActivity(Activity activity,Class cls,boolean isFinishCuurentActivity) {
        try
        {
            Intent objIntent = new Intent(activity, cls);
            objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_NO_ANIMATION);
            activity.startActivity(objIntent);
            if(isFinishCuurentActivity)
            {
                finishActivity(activity);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public static void finishActivity(Activity activity) {
        try
        {
            activity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            activity.finish();
        }
        catch (Exception ex)
        {
            activity.finish();
            ex.printStackTrace();
        }
    }
    public static void setCustomStatusBar(Activity activity)
    {
        try {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(ContextCompat.getColor(activity,R.color.gray_black));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static void setCustomStatusBar(Activity activity,int color)
    {
        try {
            Window window = activity.getWindow();
            //window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(ContextCompat.getColor(activity,color));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static void setLightStatusBar(View view,Activity activity,int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            activity.getWindow().setStatusBarColor(color);
        }
    }
    public static void toggleStatusBar(Activity activity,boolean isShow)
    {
        if(isShow) {
            if (Build.VERSION.SDK_INT < 16) {
                activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
            } else {
                View decorView = activity.getWindow().getDecorView();
                // Hide Status Bar.
                int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                decorView.setSystemUiVisibility(uiOptions);
            }
        }
        else
        {
            if (Build.VERSION.SDK_INT < 16) {
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
            else {
                View decorView = activity.getWindow().getDecorView();
                // Show Status Bar.
                int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
                decorView.setSystemUiVisibility(uiOptions);
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void startWithSelectedLanguage(Activity context, Class toClass, Boolean isFinish){
        try {
            if (SharedPrefHelper.getSharedString(context, SharedPrefHelper.SHARED_PREFERENCE_LANGUAGE_KEY).equals(ARABIC_LANGUAGE)) {
                SharedPrefHelper.setSharedString(
                        context.getApplicationContext(),
                        SharedPrefHelper.SHARED_PREFERENCE_LANGUAGE_KEY,
                        ENGLISH_LANGUAGE
                );
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    context.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    context.getWindow().getDecorView().setTextDirection(View.TEXT_DIRECTION_LTR);
                }

            } else {
                SharedPrefHelper.setSharedString(
                        context.getApplicationContext(),
                        SharedPrefHelper.SHARED_PREFERENCE_LANGUAGE_KEY,
                        ARABIC_LANGUAGE
                );
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    context.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                    context.getWindow().getDecorView().setTextDirection(View.TEXT_DIRECTION_RTL);
                }
            }
            gotoActivity(context,toClass,isFinish);

        }
        catch (Exception objException) {
            objException.printStackTrace();
        }

    }
  /*  public static void initCoverPhoto (Activity activity, SimpleDraweeView simpleDraweeView, String PhotoURL)
    {
        try {
            Postprocessor postprocessor = new BlurPostprocessor(activity, 30);
            ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(PhotoURL))
                    .setPostprocessor(postprocessor)
                    .build();//newBuilderWithSource with uri of image
            PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                    .setImageRequest(imageRequest)
                    .setOldController(simpleDraweeView.getController())
                    .build();
            simpleDraweeView.setController(controller);
        }
        catch (Exception objException)
        {
            objException.printStackTrace();
        }
    }
*/
    public static void changeBackArrow(Activity activity, ImageView view)
    {
        try {
            if (SharedPrefHelper.getSharedString(activity,SharedPrefHelper.SHARED_PREFERENCE_LANGUAGE_KEY).equals(AppConfigHelper.ARABIC_LANGUAGE)) {
                //change arrow_back Image view in arabic Language
                view.setImageResource(R.drawable.ic_arrow_forward_black_right);

            } else {
                //change arrow_back Image view in english Language
                view.setImageResource(R.drawable.ic_arrow_back_left);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static int calculateCurrentDistanceToPosition(LinearLayoutManager mLayoutManager, int targetPosition) {
        int targetScrollY = targetPosition * 370;
        return targetScrollY - mLayoutManager.findLastCompletelyVisibleItemPosition();
    }
    public static int calculateCurrentDistanceToPosition(GridLayoutManager mLayoutManager, int targetPosition) {
        int targetScrollY = targetPosition * 370;
        return targetScrollY - mLayoutManager.findLastCompletelyVisibleItemPosition();
    }
    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            //Find the currently focused view, so we can grab the correct window token from it.
            View view = activity.getCurrentFocus();
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = new View(activity);
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public static Point screenDimensions(Activity activity) {
        try {
            Point size = new Point();
            Display display = activity.getWindowManager().getDefaultDisplay();
            display.getSize(size);
            return size;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
  /*  public static void setDrawabaleEditText(Activity activity, EditText customEditText, int drawableResource) {
        try {
           if(SharedPrefHelper.getSharedString(activity,SharedPrefHelper.SHARED_PREFERENCE_LANGUAGE_KEY).equals(AppConfigHelper.ARABIC_LANGUAGE))
           {
               customEditText.setCompoundDrawablesWithIntrinsicBounds(drawableResource,0,0,0);//enable vector icon in android 4.2 and 4.4
           }
           else
           {
               customEditText.setCompoundDrawablesWithIntrinsicBounds(0,0,drawableResource,0);//enable vector icon in android 4.2 and 4.4
           }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }*/
    public static void setOffetsSpinner(Spinner spinner)
    {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                // android:overlapAnchor="false" use to make spinner open form bottom of view
                // android:overlapAnchor="false" is working in api >=21 so use this step
                spinner.setDropDownVerticalOffset(30);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static void sendPushToSingleInstance(final Context activity, final HashMap dataValue) {
        try {
            //final String url = "https://fcm.googleapis.com/fcm/send";
            StringRequest myReq = new StringRequest(Request.Method.POST, AppConfigHelper.FCM_MESSAGE_URL,
                    response -> {
                        //Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show();
                    },
                    error -> {
                        // Toast.makeText(activity, "Oops error", Toast.LENGTH_SHORT).show();
                    }) {

                @Override
                public byte[] getBody() {
                    Map<String, Object> rawParameters = new Hashtable();
                    rawParameters.put("data", new JSONObject(dataValue));
                    rawParameters.put("to", dataValue.get(AppConfigHelper.DEVICE_TOKEN_FIELD));
                    return new JSONObject(rawParameters).toString().getBytes();
                }
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "key=" + AppConfigHelper.APP_SERVER_KEY);
                    return headers;
                }

            };

            Volley.newRequestQueue(activity).add(myReq);
        }
        catch (Exception objException)
        {
            objException.printStackTrace();
        }
    }

}
