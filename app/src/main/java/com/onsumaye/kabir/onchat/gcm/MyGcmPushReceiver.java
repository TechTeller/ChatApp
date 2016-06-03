package com.onsumaye.kabir.onchat.gcm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.onsumaye.kabir.onchat.activity.ChatActivity;
import com.onsumaye.kabir.onchat.activity.LoginActivity;
import com.onsumaye.kabir.onchat.app.Config;

public class MyGcmPushReceiver extends GcmListenerService {

    private static final String TAG = MyGcmPushReceiver.class.getSimpleName();

    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(String from, Bundle bundle)
    {
        String username = bundle.getString("username");
        String message = bundle.getString("message");
        String timestamp = bundle.getString("timestamp");
        System.out.println(username + " " + message + " at " + timestamp + ".");

        if (!NotificationUtils.isAppIsInBackground(getApplicationContext()))
        {

            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils();
            notificationUtils.playNotificationSound();
        } else
        {

            Intent resultIntent = new Intent(getApplicationContext(), ChatActivity.class);
            resultIntent.putExtra("message", message);

            showNotificationMessage(getApplicationContext(), username, message, timestamp, resultIntent);
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }
}