package com.pcs.tim.myapplication.new_added_classes;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pcs.tim.myapplication.R;

public class FirebaseMessaging extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            // Handle the received FCM message and display a notification
            showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }
    }

    private void showNotification(String title, String message) {

        Activity context = null;


        NotificationManager notificationManager = (NotificationManager) context.getApplication().getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "default_channel_id";
        String channelName = "Default Channel";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
           // channel.setDescription("Default Notification Channel");
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext(), channelId)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.tris_logo) // Replace with your notification icon
                .setAutoCancel(true);

        notificationManager.notify(0, builder.build());
    }
}
