package com.onsumaye.kabir.onchat.gcm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;

import com.google.android.gms.gcm.GcmListenerService;
import com.onsumaye.kabir.onchat.chat.ChatHandler;
import com.onsumaye.kabir.onchat.chat.ChatMessage;
import com.onsumaye.kabir.onchat.activity.ChatActivity;
import com.onsumaye.kabir.onchat.app.Config;
import com.onsumaye.kabir.onchat.app.StateHolder;
import com.onsumaye.kabir.onchat.users.User;
import com.onsumaye.kabir.onchat.users.UserHandler;

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
        System.out.println("Message received!");

        if (!NotificationUtils.isAppIsInBackground(getApplicationContext()))
        {
            ChatMessage chatMessage = new ChatMessage(id, username, message, timestamp, ChatHandler.currentlySpeakingTo_Id);
            //Add to chat activity if activity is open otherwise only add to database
            if(StateHolder.appState == StateHolder.AppState.CHAT)
            {
                if(chatMessage.getUsername().equalsIgnoreCase(ChatHandler.currentlySpeakingTo_username))
                {
                    chatMessage.setRead(true);
                    ChatHandler.addMessageToActivity(chatMessage);
                }
            }

            User user = UserHandler.getUserByUsername(username);
            if(user == null)
            {
                //Add the user to the list
                Intent intent = new Intent("addUser");
                intent.putExtra("username", username);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            }
            else UserHandler.moveUserToTop(user);

            System.out.println(UserHandler.usersList.toString());

            //Save in chat messages database
            ChatHandler.chatMessageDatabaseHandler.addChatMessage(chatMessage);

            //Update the adapter view
            refreshUserAdapter(this);

            // app is in background, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
        }
        else
        {
            //App is not in focus
            Intent resultIntent = new Intent(getApplicationContext(), ChatActivity.class);
            resultIntent.putExtra("message", message);

            //Add message to database
            ChatMessage chatMessage = new ChatMessage(id, username, message, timestamp, ChatHandler.currentlySpeakingTo_Id, false);
            ChatHandler.chatMessageDatabaseHandler.addChatMessage(chatMessage);

            //Vibrate
            Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(500);

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

    private void refreshUserAdapter(Context context)
    {
        Intent intent = new Intent("refreshAdapterIntent");
        intent.putExtra("Message", "refreshAdapter");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

    }
}