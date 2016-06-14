package com.onsumaye.kabir.onchat.chat;

import android.view.View;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.onsumaye.kabir.onchat.activity.ChatActivity;
import com.onsumaye.kabir.onchat.app.Config;
import com.onsumaye.kabir.onchat.storage.ChatMessageDatabaseHandler;
import com.onsumaye.kabir.onchat.users.User;

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
    public static List<ChatMessage> selectedChatMessageList;
    public static List<ChatMessage> readChatMessageList;

    private static ChatActivity chatActivity;

    public static String myUsername;
    public static int myUserId;

    public static int currentlySpeakingTo_Id;
    public static String currentlySpeakingTo_username;

    public static ChatMessageDatabaseHandler chatMessageDatabaseHandler;

    public enum ChatMode
    {
        NORMAL, SELECTION
    }

    public static ChatMode chatMode;

    //Initialize the username and chatmessage
    public static void init(ChatActivity activity)
    {
        chatActivity = activity;
        chatMessageList = new ArrayList<ChatMessage>();
        selectedChatMessageList = new ArrayList<ChatMessage>();
        readChatMessageList = new ArrayList<ChatMessage>();
        chatMode = ChatMode.NORMAL;
    }

    public static void sendMessage(final ChatMessage message)
    {
        RequestParams params = new RequestParams();
        params.put("username", message.getUsername());
        params.put("message", message.getMessage());
        params.put("timestamp", message.getTime());
        params.put("toId", message.getToId());
        params.put("read", message.isRead() ? 1 : 0);

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
                    int id = response.getInt("id");
                    message.setId(id);

                    ChatHandler.addChatMessage(message);
                }
                catch (JSONException e)
                {
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
        chatActivity.chatListView.post(new Runnable()
        {
            @Override
            public void run()
            {
                // Select the last row so it will scroll into view=
                chatActivity.chatListView.setSelection(chatActivity.messageAdapter.getCount() - 1);
            }
        });
    }

    public static void addMessageToActivity(final ChatMessage cMessage)
    {
        chatActivity.runOnUiThread(new Runnable()
        {
            public void run()
            {
                if (!cMessage.getUsername().equals(ChatHandler.myUsername))
                    chatActivity.messageAdapter.addMessage(cMessage);

                chatActivity.chatListView.setAdapter(chatActivity.messageAdapter);
                scrollChatToBottom();
            }
        });
    }

    public static void addChatMessage(ChatMessage message)
    {
        chatMessageDatabaseHandler.addChatMessage(message);
    }

    public static void toggleActionBar()
    {
        if(chatMode == ChatMode.SELECTION)
        {
            chatActivity.toolbar_profilePicture.setVisibility(View.INVISIBLE);
            chatActivity.toolbar_username.setVisibility(View.INVISIBLE);
            chatActivity.toolbar_deleteMessageButton.setVisibility(View.VISIBLE);
        }
        else
        {
            chatActivity.toolbar_profilePicture.setVisibility(View.VISIBLE);
            chatActivity.toolbar_username.setVisibility(View.VISIBLE);
            chatActivity.toolbar_deleteMessageButton.setVisibility(View.INVISIBLE);
        }
    }

    public static void deleteMessage()
    {
        for(ChatMessage message : selectedChatMessageList)
        {
            //Delete message from the message adapter
            chatActivity.messageAdapter.removeMessage(message);
            //Delete message from the database so it does not appear on relaunching the activity
            chatMessageDatabaseHandler.deleteChatMessage(message);
        }
        selectedChatMessageList.clear();
        chatMode = ChatMode.NORMAL;
        toggleActionBar();
    }

    public static int getUnreadMessageCount(User user)
    {
        int counter = 0;
        for(ChatMessage message: chatMessageDatabaseHandler.getAllChatMessagesFromUser(user))
        {
            if(!message.isRead() && !message.getUsername().equals(ChatHandler.myUsername))
            {
                counter++;
            }
        }
        return counter;
    }

    public static void markMessageRead(int id)
    {
        ChatMessage message = chatMessageDatabaseHandler.getChatMessage(id);
        message.setRead(true);
        chatMessageDatabaseHandler.updateChatMessage(message);
    }
}
