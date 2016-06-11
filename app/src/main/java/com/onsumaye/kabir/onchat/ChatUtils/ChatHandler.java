package com.onsumaye.kabir.onchat.ChatUtils;

import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.onsumaye.kabir.onchat.activity.ChatActivity;
import com.onsumaye.kabir.onchat.app.Config;
import com.onsumaye.kabir.onchat.storage.ChatMessageDatabaseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;


public class ChatHandler
{
    public static List<ChatMessage> chatMessageList;
    private static ChatActivity chatActivity;

    public static String myUsername;
    public static int myUserId;

    public static int currentlySpeakingTo_Id;
    public static String currentlySpeakingTo_username;

    public static ChatMessageDatabaseHandler chatMessageDatabaseHandler;

    //Initialize the username and chatmessage
    public static void init(ChatActivity activity)
    {
        chatActivity = activity;
        chatMessageList = new ArrayList<ChatMessage>();
        chatMessageDatabaseHandler = new ChatMessageDatabaseHandler(activity);
    }

    public static void sendMessage(final ChatMessage message)
    {
        RequestParams params = new RequestParams();
        params.put("username", message.getUsername());
        params.put("message", message.getMessage());
        params.put("timestamp", message.getTime());
        params.put("toId", message.getToId());

        AsyncHttpClient client = new AsyncHttpClient();
        chatMessageList.add(message);

        client.post(Config.SERVER_IP + "/messages", params, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                System.out.println("Status code: " + statusCode);
                try
                {
                    long id = response.getLong("id");
                    message.setId(id);

                    ChatHandler.addChatMessage(message);
                }
                catch (JSONException e)
                {
                    System.out.println("An exception occurred!");
                    e.printStackTrace();
                }
                message.setSent(true);
                chatActivity.messageAdapter.updateSentIcon(message);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response)
            {
                Toast.makeText(chatActivity.getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
            }
        });
    }

    public static String getTimeStamp(long time)
    {
        String timeStamp;
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm", Locale.getDefault());
        timeStamp = formatter.format(time);
        return timeStamp;
    }


    public static void scrollChatToBottom() {
        chatActivity.chatListView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view
                chatActivity.chatListView.setSelection(chatActivity.messageAdapter.getCount() - 1);
            }
        });
    }

    public static void addMessageToActivity(final int id, final String username, final String message, final String timestamp)
    {
        chatActivity.runOnUiThread(new Runnable()
        {
            public void run()
            {
                final ChatMessage cMessage;
                cMessage = new ChatMessage(id, username, message, timestamp, currentlySpeakingTo_Id);
                if (!cMessage.getUsername().equals(ChatHandler.myUsername))
                    chatActivity.messageAdapter.addMessage(cMessage);

                chatActivity.chatListView.setAdapter(chatActivity.messageAdapter);
                scrollChatToBottom();
            }
        });

        final ChatMessage cMessage;
        cMessage = new ChatMessage(id, username, message, timestamp, currentlySpeakingTo_Id);

        chatMessageDatabaseHandler.addChatMessage(cMessage);
    }

    public static void addChatMessage(ChatMessage message)
    {
        chatMessageDatabaseHandler.addChatMessage(message);
    }
}
