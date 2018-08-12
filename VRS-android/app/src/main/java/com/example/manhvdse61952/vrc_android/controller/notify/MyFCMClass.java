package com.example.manhvdse61952.vrc_android.controller.notify;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.controller.layout.contract.ManageContractActivity;
import com.example.manhvdse61952.vrc_android.controller.layout.main.MainActivity;
import com.example.manhvdse61952.vrc_android.controller.permission.PermissionDevice;
import com.example.manhvdse61952.vrc_android.controller.layout.contract.ContractDetail;
import com.example.manhvdse61952.vrc_android.controller.layout.contract.ContractPreFinishOwner;
import com.example.manhvdse61952.vrc_android.controller.resources.ImmutableValue;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFCMClass extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getNotification() != null) {
            //Log.e("VRS", "Title: " + remoteMessage.getNotification().getTitle());
            //Log.e("VRS", "Body: " + remoteMessage.getNotification().getBody());

            //sendNotification( remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle());
            executeData(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle());
        }

        if (remoteMessage.getData().size() > 0) {
            //Log.e("VRS", "Data: " + remoteMessage.getData());
        }
    }

    private void sendNotification(String messageBody, String title, String contractID) {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (!contractID.equals("0") && !messageBody.equals(ImmutableValue.changeFeeMessageBody)) {
            SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
            editor.putString(ImmutableValue.MAIN_contractID, contractID);
            editor.apply();
            Intent it = new Intent(this, MainActivity.class);
            it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, PermissionDevice.NOTIFY_REQUEST_CODE, it, PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_menu_send)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent);
            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        } else {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_menu_send)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setSound(defaultSoundUri);
            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        }


    }

    private void executeData(String body, String title) {
        String[] temp = body.split("-");
        int userIdFromMessage = Integer.parseInt(temp[0]);
        SharedPreferences sharedPreferences = getSharedPreferences(ImmutableValue.HOME_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        int userIdFromApp = sharedPreferences.getInt(ImmutableValue.HOME_userID, 0);
        if (userIdFromMessage == userIdFromApp) {
            String contractID = temp[1];
            String messageBody = temp[3];
            sendNotification(messageBody, title, contractID);
        }

    }
}
