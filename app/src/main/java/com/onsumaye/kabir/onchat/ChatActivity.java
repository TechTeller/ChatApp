package com.onsumaye.kabir.onchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.onsumaye.kabir.onchat.ChatUtils.ChatHandler;
import com.onsumaye.kabir.onchat.ChatUtils.ChatMessage;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity
{

    ListView chatListView;
    MessageAdapter messageAdapter;
    ChatMessage recievedMessage;

    String username;

    EditText chatBox;
    Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ChatHandler.init();
        setTitle("");
        chatListView = (ListView) findViewById(R.id.chatListView);
        sendButton = (Button) findViewById(R.id.sendButton);
        chatBox = (EditText) findViewById(R.id.chatBox);
        username = getIntent().getStringExtra("username");
        System.out.print("Username is" + username);

        messageAdapter = new MessageAdapter(this);
        chatListView.setAdapter(messageAdapter);

        sendButton.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if(chatBox.getText().toString().equals(""))
                            return;
                        //Send the chatMessage
                        ChatMessage message = new ChatMessage(username, chatBox.getText().toString(), System.currentTimeMillis());
                        ChatHandler.sendMessage(message);
                        chatBox.setText("");
                        scrollChatToBottom();

                        //Refresh the adapter
                        chatListView.setAdapter(messageAdapter);
                    }
                }
        );
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
}
