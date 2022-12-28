package com.toplevel.kengmakon.utils;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.toplevel.kengmakon.R;
import com.toplevel.kengmakon.models.PushNotificationModel;

import java.util.Map;

import javax.inject.Inject;

public class MyFirebaseService extends FirebaseMessagingService {
    private Gson gson;

    @Inject
    PreferencesUtil preferencesUtil;

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

        System.out.println("TITLE: " + remoteMessage.getNotification().getTitle());
        System.out.println("BODY: " + remoteMessage.getNotification().getBody());

        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();

        Gson parser = new Gson();
        PushNotificationModel pushNotificationTitleModel = parser.fromJson(remoteMessage.getNotification().getTitle(), PushNotificationModel.class);
        PushNotificationModel pushNotificationBodyModel = parser.fromJson(remoteMessage.getNotification().getBody(), PushNotificationModel.class);


//        String title = "";
//        String body = "";
//        if (preferencesUtil.getLANGUAGE().equals("en")) {
//            title = pushNotificationTitleModel.getEn();
//            body = pushNotificationBodyModel.getEn();
//        } else if (preferencesUtil.getLANGUAGE().equals("ru")) {
//            title = pushNotificationTitleModel.getRu();
//            body = pushNotificationBodyModel.getRu();
//        } else {
//            title = pushNotificationTitleModel.getUz();
//            body = pushNotificationBodyModel.getUz();
//        }

        final String CHANNEL_ID = "HEADS_UP_NOTIFICATION";

        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Heads Up Notification",
                NotificationManager.IMPORTANCE_HIGH
        );

        getSystemService(NotificationManager.class).createNotificationChannel(channel);

        Notification.Builder notification =
                new Notification.Builder(this, CHANNEL_ID)
                        .setContentTitle(pushNotificationTitleModel.getUz())
                        .setContentText(pushNotificationBodyModel.getUz())
                        .setSmallIcon(R.drawable.keng_makon_logo)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setAutoCancel(true);

        NotificationManagerCompat.from(this).notify(1, notification.build());

        super.onMessageReceived(remoteMessage);

    }


}