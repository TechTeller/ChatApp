package com.onsumaye.kabir.onchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.onsumaye.kabir.onchat.ChatUtils.ChatHandler;
import com.onsumaye.kabir.onchat.ChatUtils.ChatMessage;
import com.onsumaye.kabir.onchat.ChatUtils.MessageAdapter;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;

import org.json.JSONException;
import org.json.JSONObject;

public class ChatActivity extends AppCompatActivity
{

    public ListView chatListView;
    public MessageAdapter messageAdapter;

    public EditText chatBox;
    Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ChatHandler.init(this);
        setTitle("");
        chatListView = (ListView) findViewById(R.id.chatListView);
        sendButton = (Button) findViewById(R.id.sendButton);
        chatBox = (EditText) findViewById(R.id.chatBox);
        ChatHandler.myUsername = getIntent().getStringExtra("username");
        listenForMessages();
        messageAdapter = new MessageAdapter(this);
        chatListView.setAdapter(messageAdapter);

    }


    public void onSendClicked(View view)
    {
        if(chatBox.getText().toString().equals(""))
            return;
        //Send the chatMessage
        ChatMessage message = new ChatMessage(ChatHandler.myUsername, chatBox.getText().toString(), System.currentTimeMillis());
        ChatHandler.sendMessage(message);
        scrollChatToBottom();
        chatBox.setText("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.logOutButton:
                finish();
                setResult(1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void scrollChatToBottom() {
        chatListView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view
                chatListView.setSelection(messageAdapter.getCount() - 1);
            }
        });
    }

    public void listenForMessages()
    {
        PusherOptions options = new PusherOptions();
        options.setCluster("ap1");
        Pusher pusher = new Pusher("baa865fd345548f09300", options);

        Channel channel = pusher.subscribe("messages");

        channel.bind("new_message", new SubscriptionEventListener()
        {
            @Override
            public void onEvent(String channelName, String eventName, final String data)
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        JSONObject obj;
                        ChatMessage message;
                        try
                        {
                            obj = new JSONObject(data);
                                    message = new ChatMessage(obj.getString("username"),
                                                              obj.getString("message"),
                                                              obj.getLong("timestamp"));

                            if(!message.getUsername().equals(ChatHandler.myUsername))
                                messageAdapter.addMessage(message);
                        }
                        catch(JSONException e)
                        {
                            e.printStackTrace();
                            System.out.println("Invalid JSON!");
                        }

                        chatListView.setAdapter(messageAdapter);
                        scrollChatToBottom();
                    }
                });
            }
        });

        pusher.connect();
    }
}
