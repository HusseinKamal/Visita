package com.emergency.app.notification;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        //Log.d(TAG, "From: " + remoteMessage.getFrom());
        try {
         /*   if(SharedPrefHelper.getSharedBoolean(this,SharedPrefHelper.SHARED_PREFERENCE_RECEIVE_PUSH_NOYIFICAITONS_KEY)) {
                databaseHelper = new DatabaseHelper(this);
                if (SharedPrefHelper.getSharedString(this, SharedPrefHelper.SHARED_PREFERENCE_LANGUAGE_KEY).equals(AppConfigHelper.ARABIC_LANGUAGE)) {
                    message = remoteMessage.getData().get("message_ar");
                    title = remoteMessage.getData().get("title_ar");
                } else {
                    message = remoteMessage.getData().get("message_en");
                    title = remoteMessage.getData().get("title_en");
                }
                //restaurant id
                if (remoteMessage.getData().containsKey("restaurant_id")) {
                    restaurantID = Integer.parseInt(remoteMessage.getData().get("restaurant_id"));
                } else {
                    restaurantID = -1;
                }
                //reservation id
                if (remoteMessage.getData().containsKey("reservation_id")) {
                    reservationID = remoteMessage.getData().get("reservation_id");
                } else {
                    reservationID = "";
                }

                if (remoteMessage.getData().containsKey("type")) {
                    notifyType = remoteMessage.getData().get("type");
                } else {
                    notifyType = "";
                }
                image = remoteMessage.getData().get("image_url");
                if (!ValidateData.isValid(image)) {
                    image = "";
                }
                if (!ValidateData.isValid(message)) {
                    message = getString(R.string.app_name);
                }
                if (!ValidateData.isValid(title)) {
                    title = getString(R.string.app_name);
                }
                if (remoteMessage.getData().containsKey("payment_token")) {
                    payment_token = remoteMessage.getData().get("payment_token");
                } else {
                    payment_token = "";
                }
                //update reservations count
                if (remoteMessage.getData().containsKey("reservations_count")) {
                    String reserveCount =remoteMessage.getData().get("reservations_count");
                    User user=(User)SharedPrefHelper.getSharedOBJECT(this,SharedPrefHelper.SHARED_PREFERENCE_USER_DATA);
                    user.getData().get(0).setReservations_count(reserveCount);
                    SharedPrefHelper.setSharedOBJECT(this,SharedPrefHelper.SHARED_PREFERENCE_USER_DATA,user);
                }
                Random r = new Random();
                int notifyID = r.nextInt((100 - 10) + 1) + 10;

                //update status of reservation
                if (reservationID.length() > 0 && reservationID != null && !reservationID.equals("") && !reservationID.isEmpty() && !TextUtils.isEmpty(reservationID) && notifyType.length() > 0 && notifyType != null && !notifyType.equals("") && !notifyType.isEmpty() && !TextUtils.isEmpty(notifyType)) {
                    status = AppConfigHelper.getReservationStatus(notifyType);
                    if (status != null) {
                        if (status.length() > 0 && status != null && !status.equals("") && !status.isEmpty() && !TextUtils.isEmpty(status)) {
                            databaseHelper.updateStatus(reservationID, status);
                        }
                    }
                }

                if (restaurantID != -1) {
                    NotificationHelper.ShowNotification(this, title, message, image, notifyID, ReservationDetailsActivity.class, true, restaurantID, notifyType, payment_token, reservationID);
                } else {
                    NotificationHelper.ShowNotification(this, title, message, image, notifyID, MainActivity.class, true, restaurantID, notifyType, payment_token, reservationID);
                }
            }*/

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
