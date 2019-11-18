package com.emergency.app.notification;
import com.emergency.app.ui.employee.RequestDetailsEmployeeActivity;
import com.emergency.app.ui.guest.RequestDetailsGuestActivity;
import com.emergency.app.util.appconfighelper.AppConfigHelper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
           String userType=remoteMessage.getData().get(AppConfigHelper.IS_GUEST_FIELD);
           if(userType.equals(AppConfigHelper.GUEST))
           {
               //notification to employee
               NotificationHelper.ShowNotificationEmployee(this,remoteMessage.getData(), RequestDetailsEmployeeActivity.class);
           }
           else
           {
               //notification to guest
               NotificationHelper.ShowNotificationGuest(this,remoteMessage.getData(), RequestDetailsGuestActivity.class);
           }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
