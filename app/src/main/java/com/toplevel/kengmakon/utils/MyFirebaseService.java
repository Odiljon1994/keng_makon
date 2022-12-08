package com.toplevel.kengmakon.utils;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.toplevel.kengmakon.R;

import java.util.Map;

public class MyFirebaseService extends FirebaseMessagingService {
    private Gson gson;



    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }

    public MyFirebaseService() {
        gson = new Gson();

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        System.out.println("*** Remote Message ***");
        System.out.println(remoteMessage.toString());
        System.out.println(data.toString());

        System.out.println(remoteMessage.getNotification().getTitle());
        System.out.println(remoteMessage.getNotification().getBody());

        String title = remoteMessage.getNotification().getTitle();
        String text = remoteMessage.getNotification().getBody();



        final String CHANNEL_ID = "HEADS_UP_NOTIFICATION";

        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Heads Up Notification",
                NotificationManager.IMPORTANCE_HIGH
        );

        getSystemService(NotificationManager.class).createNotificationChannel(channel);

        Notification.Builder notification =
                new Notification.Builder(this, CHANNEL_ID)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setSmallIcon(R.drawable.scan_icon)
                        .setAutoCancel(true);

        NotificationManagerCompat.from(this).notify(1, notification.build());

        super.onMessageReceived(remoteMessage);

    }


}