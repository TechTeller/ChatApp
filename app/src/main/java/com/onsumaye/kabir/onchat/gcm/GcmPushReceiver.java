package com.onsumaye.kabir.onchat.gcm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.gcm.GcmListenerService;
import com.onsumaye.kabir.onchat.ChatUtils.ChatHandler;
import com.onsumaye.kabir.onchat.ChatUtils.ChatMessage;
import com.onsumaye.kabir.onchat.activity.ChatActivity;
import com.onsumaye.kabir.onchat.app.Config;

import org.json.JSONObject;

public class GcmPushReceiver extends GcmListenerService {

    private static final String TAG = GcmPushReceiver.class.getSimpleName();

    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(String from, Bundle bundle)
    {
        final int id = Integer.parseInt(bundle.getString("id"));
        final String username = bundle.getString("username");
        final String message = bundle.getString("message");
        final String timestamp = bundle.getString("timestamp");
        final int toId = Integer.parseInt(bundle.getString("toId"));
        System.out.println(username + " " + message + " at " + timestamp + ".");

        if (!NotificationUtils.isAppIsInBackground(getApplicationContext()))
        {
            //Add to chatmessage
            ChatHandler.addMessageToActivity(id, username, message, timestamp);
            ChatMessage cMessage = new ChatMessage(id, username, message, timestamp, toId);
            //Save in chat messages database

            // app is in background, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
        }
        else
        {
            Intent resultIntent = new Intent(getApplicationContext(), ChatActivity.class);
            resultIntent.putExtra("message", message);

            showNotificationMessage(getApplicationContext(), username, message, String.valueOf(timestamp) , resultIntent);
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