package com.furniture.kengmakon.utils;


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

        System.out.println("*** Remote Message ***");
//        Map<String, String> data = remoteMessage.getData();
//        System.out.println("*** Remote Message ***");
//        System.out.println(remoteMessage.toString());
//        System.out.println(data.toString());

//
//        System.out.println("TITLE: " + remoteMessage.getNotification().getTitle());
//        System.out.println("BODY: " + remoteMessage.getNotification().getBody());

//        String title = data.get("title");
//        String body =data.get("body");

//        String title = remoteMessage.getNotification().getTitle();
//        String body = remoteMessage.getNotification().getBody();




//        String click_action = remoteMessage.getNotification().getClickAction();
//        Intent intent = new Intent(click_action);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

//        System.out.println("********* Language: " + preferencesUtil.getLANGUAGE());

//        Gson parser = new Gson();
//        PushNotificationModel pushNotificationTitleModel = parser.fromJson(title, PushNotificationModel.class);
//        PushNotificationModel pushNotificationBodyModel = parser.fromJson(body, PushNotificationModel.class);
//
//        System.out.println("Parsed title: " + pushNotificationTitleModel.getUz());
//        System.out.println("Parsed body: " + pushNotificationBodyModel.getUz());

        final String CHANNEL_ID = "HEADS_UP_NOTIFICATION";

//        String parsedTitle = pushNotificationTitleModel.getEn();
//        String parsedBody = pushNotificationBodyModel.getEn();


        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Heads Up Notification",
                NotificationManager.IMPORTANCE_HIGH
        );

        getSystemService(NotificationManager.class).createNotificationChannel(channel);

//        Toast.makeText(this, "Body not parsed: " + pushNotificationBodyModel.toString(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "Body: " + pushNotificationBodyModel.getUz(), Toast.LENGTH_SHORT).show();

        Notification.Builder notification =
                new Notification.Builder(this, CHANNEL_ID)
                        .setContentTitle(remoteMessage.getNotification().getTitle())
                        .setContentText(remoteMessage.getNotification().getBody())
                        .setSmallIcon(R.drawable.keng_makon_logo)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setAutoCancel(true);

        NotificationManagerCompat.from(this).notify(1, notification.build());

        super.onMessageReceived(remoteMessage);

    }


}