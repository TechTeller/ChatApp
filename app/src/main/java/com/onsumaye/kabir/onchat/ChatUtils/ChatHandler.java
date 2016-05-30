package com.onsumaye.kabir.onchat.ChatUtils;

import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.onsumaye.kabir.onchat.ChatActivity;

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


    //Initialize the username and chatmessage
    public static void init(ChatActivity activity)
    {
        chatActivity = activity;
        chatMessageList = new ArrayList<ChatMessage>();
    }

    public static void sendMessage(ChatMessage message)
    {
        RequestParams params = new RequestParams();
        params.put("username", message.getUsername());
        params.put("message", message.getMessage());
        params.put("timestamp", message.getTime());

        AsyncHttpClient client = new AsyncHttpClient();

      client.post("http://107.6.174.180:3000" + "/messages", params, new JsonHttpResponseHandler()
//        client.post("http://192.168.42.181:3000" + "/messages", params, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                chatActivity.chatBox.setText("");
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
}
