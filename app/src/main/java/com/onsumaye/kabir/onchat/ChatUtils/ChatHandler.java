package com.onsumaye.kabir.onchat.ChatUtils;

import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.onsumaye.kabir.onchat.activity.ChatActivity;

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

    //Initialize the username and chatmessage
    public static void init(ChatActivity activity)
    {
        chatActivity = activity;
        chatMessageList = new ArrayList<ChatMessage>();
    }

    public static void sendMessage(final ChatMessage message)
    {
        RequestParams params = new RequestParams();
        params.put("username", message.getUsername());
        params.put("message", message.getMessage());
        params.put("timestamp", message.getTime());

        AsyncHttpClient client = new AsyncHttpClient();
        chatMessageList.add(message);

        client.post("http://107.6.174.180:3000" + "/messages", params, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                System.out.println("Status code: " + statusCode);
                try {
                    long id = response.getLong("id");
                    System.out.println("Message ID : " + response.getInt("id"));
                    message.setId(id);
                } catch (JSONException e) {
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

    public static void listenForMessages()
    {

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

}
