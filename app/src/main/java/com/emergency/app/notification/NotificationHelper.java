package com.emergency.app.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.emergency.app.R;

public class NotificationHelper {
    public static void clear(Context context) {
        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyMgr.cancelAll();
    }
    public static void ShowNotification(Context context, String Title, String message, String imagePath, int NotifID, Class notificationIntentClass, boolean isSound, int restaurantID, String notifyType, String paymentToken, String reservationID) {
        try {
       /*     Intent resultIntent = null;
            NotificationCompat.Builder objBuilder;
            if (imagePath != null && !imagePath.isEmpty()) {
                objBuilder =
                        new NotificationCompat.Builder(context)
                                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))//show Image of the sender
                                .setContentTitle(Title)
                                .setContentText(message)
                                .setAutoCancel(true);
            }else {
                objBuilder =
                        new NotificationCompat.Builder(context)
                                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                                .setContentTitle(Title)
                                .setContentText(message)
                                .setAutoCancel(true);
            }
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                objBuilder.setColor(context.getResources().getColor(R.color.colorPrimary))
                        .setSmallIcon(R.mipmap.ic_launcher);
            }
            else
            {
                objBuilder.setSmallIcon(R.mipmap.ic_launcher);
            }

            if (isSound)
                objBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            //if there is restaurant id
            if(restaurantID!=-1) {

            }
            //if there is no restaurant id
            else
            {
                resultIntent = new Intent(context, notificationIntentClass);
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_NO_ANIMATION);
            }

            PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
            objBuilder.setContentIntent(resultPendingIntent);
            NotificationManager objNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            objNotificationManager.notify(NotifID, objBuilder.build());*/
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
