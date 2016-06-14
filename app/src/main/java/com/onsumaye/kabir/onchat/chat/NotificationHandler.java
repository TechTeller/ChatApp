package com.onsumaye.kabir.onchat.chat;


import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.onsumaye.kabir.onchat.R;

public class NotificationHandler
{
    private static Context context;

    public static void init(Context contextToAssign)
    {
        context = contextToAssign;
    }

    public static void issueNotification(int unreadMessages)
    {
        NotificationManager nManager = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("ChatApplication")
                        .setContentText("You have " + unreadMessages + " unread messages.");

        nManager.notify(1234, mBuilder.build());
    }
}
