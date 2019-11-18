package com.emergency.app.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.emergency.app.R;
import com.emergency.app.ui.employee.HomeEmployeeActivity;
import com.emergency.app.util.appconfighelper.AppConfigHelper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NotificationHelper {
    public static void ShowNotificationEmployee(Context context, Map<String,String> map, Class notificationIntentClass) {
        try {
            int requestID = Integer.parseInt(String.valueOf(map.get(AppConfigHelper.ID_REQUEST_FIELD)));
            Intent resultIntent = new Intent(context, notificationIntentClass);
            String user;
            if (String.valueOf(map.get(AppConfigHelper.IS_GUEST_FIELD)).equals(AppConfigHelper.GUEST)) {
                user = String.valueOf(map.get(AppConfigHelper.EMP_NAME_FIELD));
            } else {
                user = String.valueOf(map.get(AppConfigHelper.GUEST_NAME_FIELD));
            }
            //if android version is not Oreo
            if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {

                NotificationCompat.Builder objBuilder =
                        new NotificationCompat.Builder(context)
                                .setStyle(new NotificationCompat.BigTextStyle().bigText(user + " " + context.getResources().getString(R.string.sent_request_msg)))
                                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo))
                                .setContentTitle(String.valueOf(map.get(AppConfigHelper.REQUEST_TYPE_FIELD)))
                                .setContentText(user + " " + context.getResources().getString(R.string.sent_request_msg))
                                .setVibrate(new long[]{1000, 1000})
                                .setPriority(Notification.PRIORITY_HIGH)
                                .setAutoCancel(true);
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    objBuilder.setColor(context.getResources().getColor(R.color.white))
                            .setSmallIcon(R.drawable.logo);
                } else {
                    objBuilder.setSmallIcon(R.drawable.logo);
                }
                //if (isSound)
                objBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                resultIntent.putExtra(AppConfigHelper.REQUEST_ID_INTENT, requestID);


                PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
                objBuilder.setContentIntent(resultPendingIntent);
                NotificationManager objNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                objNotificationManager.notify((int) ((new Date(System.currentTimeMillis()).getTime() / 1000L) % Integer.MAX_VALUE), objBuilder.build());
            }
            //if android version is Oreo
            else {
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                resultIntent.putExtra(AppConfigHelper.REQUEST_ID_INTENT, requestID);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 , resultIntent, PendingIntent.FLAG_ONE_SHOT);

                String channel_id = createNotificationChannel(context);

                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channel_id)
                        .setContentTitle(String.valueOf(map.get(AppConfigHelper.REQUEST_TYPE_FIELD)))
                        .setContentText(user + " " + context.getResources().getString(R.string.sent_request_msg))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(user + " " + context.getResources().getString(R.string.sent_request_msg)))
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo))
                        .setSmallIcon(R.drawable.logo) //needs white icon with transparent BG (For all platforms)
                        .setColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
                        .setVibrate(new long[]{1000, 1000})
                        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                        .setContentIntent(pendingIntent)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setAutoCancel(true);
                NotificationManager objNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                objNotificationManager.notify((int) ((new Date(System.currentTimeMillis()).getTime() / 1000L) % Integer.MAX_VALUE) /* ID of notification */, notificationBuilder.build());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static void ShowNotificationGuest(Context context, Map<String,String> map, Class notificationIntentClass) {
        try {
            int requestID = Integer.parseInt(String.valueOf(map.get(AppConfigHelper.ID_REQUEST_FIELD)));
            Intent resultIntent = new Intent(context, notificationIntentClass);
            String user,message="";
            if (String.valueOf(map.get(AppConfigHelper.IS_GUEST_FIELD)).equals(AppConfigHelper.GUEST)) {
                user = String.valueOf(map.get(AppConfigHelper.EMP_NAME_FIELD));
            } else {
                user = String.valueOf(map.get(AppConfigHelper.GUEST_NAME_FIELD));
            }
            switch (String.valueOf(map.get(AppConfigHelper.STATUS_REQUEST_FIELD)))
            {
                case AppConfigHelper.ACCEPT_STATUS:
                    message=context.getResources().getString(R.string.accepted);
                    break;
                case AppConfigHelper.START_STATUS:
                    message=context.getResources().getString(R.string.started);
                    break;
                case AppConfigHelper.REJECT_STATUS:
                    message=context.getResources().getString(R.string.reject);
                    break;
                case AppConfigHelper.FINISH_STATUS:
                    message=context.getResources().getString(R.string.finished);
                    break;
            }
            //if android version is not Oreo
            if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {

                NotificationCompat.Builder objBuilder =
                        new NotificationCompat.Builder(context)
                                .setStyle(new NotificationCompat.BigTextStyle().bigText(user + " "+message+ " "+ context.getResources().getString(R.string.order_title)))
                                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo))
                                .setContentTitle(String.valueOf(map.get(AppConfigHelper.REQUEST_TYPE_FIELD)))
                                .setContentText(user + " "+message+ " "+ context.getResources().getString(R.string.order_title))
                                .setVibrate(new long[]{1000, 1000})
                                .setPriority(Notification.PRIORITY_HIGH)
                                .setAutoCancel(true);
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    objBuilder.setColor(context.getResources().getColor(R.color.white))
                            .setSmallIcon(R.drawable.logo);
                } else {
                    objBuilder.setSmallIcon(R.drawable.logo);
                }
                //if (isSound)
                objBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                resultIntent.putExtra(AppConfigHelper.REQUEST_ID_INTENT, requestID);


                PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
                objBuilder.setContentIntent(resultPendingIntent);
                NotificationManager objNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                objNotificationManager.notify((int) ((new Date(System.currentTimeMillis()).getTime() / 1000L) % Integer.MAX_VALUE), objBuilder.build());
            }
            //if android version is Oreo
            else {
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                resultIntent.putExtra(AppConfigHelper.REQUEST_ID_INTENT, requestID);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 , resultIntent, PendingIntent.FLAG_ONE_SHOT);

                String channel_id = createNotificationChannel(context);

                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channel_id)
                        .setContentTitle(String.valueOf(map.get(AppConfigHelper.REQUEST_TYPE_FIELD)))
                        .setContentText(user + " "+message+ " "+ context.getResources().getString(R.string.order_title))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(user + " "+message+ " "+ context.getResources().getString(R.string.order_title)))
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo))
                        .setSmallIcon(R.drawable.logo) //needs white icon with transparent BG (For all platforms)
                        .setColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
                        .setVibrate(new long[]{1000, 1000})
                        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                        .setContentIntent(pendingIntent)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setAutoCancel(true);
                NotificationManager objNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                objNotificationManager.notify((int) ((new Date(System.currentTimeMillis()).getTime() / 1000L) % Integer.MAX_VALUE) /* ID of notification */, notificationBuilder.build());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static String createNotificationChannel(Context context) {

        // NotificationChannels are required for Notifications on O (API 26) and above.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "Channel_id";
            CharSequence channelName = context.getResources().getString(R.string.app_name);
            String channelDescription = context.getResources().getString(R.string.app_name);
            int channelImportance = NotificationManager.IMPORTANCE_DEFAULT;
            boolean channelEnableVibrate = true;
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, channelImportance);
            notificationChannel.setDescription(channelDescription);
            notificationChannel.enableVibration(channelEnableVibrate);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);

            return channelId;
        } else {
            // Returns null for pre-O (26) devices.
            return null;
        }
    }
}
