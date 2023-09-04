package com.pcs.tim.myapplication.new_added_classes;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pcs.tim.myapplication.R;

public class FirebaseNotification extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService__";
    private static final String CHANNEL_ID = "my_channel";
    String id;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        // Handle data payload of FCM messages.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            id = remoteMessage.getData().get("sound");
            Log.d(TAG + "b", "onMessageReceived: " + id);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("my_channel", "User Notification", NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);
            channel.setShowBadge(true);
            channel.setImportance(NotificationManager.IMPORTANCE_HIGH);
            channel.setVibrationPattern(new long[]{1000, 100, 1000, 100});

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "my_channel")
                .setContentTitle("VeriMyRc")
                .setSmallIcon(R.mipmap.ic_tris_logo)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[]{1000, 100, 1000, 100, 1000})
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Deepak"));
        /*.setContentText(message)*//*

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(79, builder.build());

    }

*/


    }}
