package com.onsumaye.kabir.onchat.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.onsumaye.kabir.onchat.ChatUtils.ChatHandler;
import com.onsumaye.kabir.onchat.ChatUtils.ChatMessage;
import com.onsumaye.kabir.onchat.ChatUtils.MessageAdapter;
import com.onsumaye.kabir.onchat.ChatUtils.NotificationHandler;
import com.onsumaye.kabir.onchat.R;
import com.onsumaye.kabir.onchat.app.Config;
import com.onsumaye.kabir.onchat.dialogs.DeleteMessageConfirmationDialog;
import com.onsumaye.kabir.onchat.users.UserHandler;


public class ChatActivity extends AppCompatActivity
{
    public MessageAdapter messageAdapter;

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    public ListView chatListView;
    public EditText chatBox;
    public ImageButton sendButton;
    public Toolbar toolbar;
    public ImageButton toolbar_deleteMessageButton;
    public TextView toolbar_username;
    public ImageView toolbar_profilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ChatHandler.init(this);

        //Initialization of all view elements    ---------------------------------------------------
        chatListView = (ListView) findViewById(R.id.chatListView);
        sendButton = (ImageButton) findViewById(R.id.sendButton);
        toolbar_deleteMessageButton = (ImageButton) findViewById(R.id.toolbar_deleteMessageButton);
        toolbar_username = (TextView) findViewById(R.id.toolbar_username);
        toolbar_profilePicture = (ImageView) findViewById(R.id.toolbar_profilePicture);
        chatBox = (EditText) findViewById(R.id.chatBox);
        //------------------------------------------------------------------------------------------

        ChatHandler.chatMessageList.addAll(ChatHandler.chatMessageDatabaseHandler.getAllChatMessagesFromUser(UserHandler.getUserById(ChatHandler.currentlySpeakingTo_Id)));

        messageAdapter = new MessageAdapter(this);
        chatListView.setAdapter(messageAdapter);
        NotificationHandler.init(getApplicationContext());
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    String token = intent.getStringExtra("token");
                    System.out.println("GCM Token: " + token);

                    //Send the registration to the server to store in the users database
                    AsyncHttpClient client = new AsyncHttpClient();

                    RequestParams params = new RequestParams();
                    params.put("token", token);
                    params.put("username", ChatHandler.myUsername);

                } else if (intent.getAction().equals(Config.SENT_TOKEN_TO_SERVER)) {
                    // gcm registration id is stored in our server's MySQL

                    Toast.makeText(getApplicationContext(), "GCM registration token is stored in server!", Toast.LENGTH_LONG).show();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                }
            }
        };

        toolbar_deleteMessageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DeleteMessageConfirmationDialog dialog = new DeleteMessageConfirmationDialog();
                dialog.show(getFragmentManager(), "OnChat");
            }
        });

        ChatHandler.scrollChatToBottom();
    }


    public void onSendClicked(View view)
    {
        if(chatBox.getText().toString().equals(""))
            return;
        //Send the chatMessage
        ChatMessage message = new ChatMessage(ChatHandler.myUsername, chatBox.getText().toString(), System.currentTimeMillis() + "", ChatHandler.currentlySpeakingTo_Id);
        ChatHandler.sendMessage(message);
        ChatHandler.scrollChatToBottom();
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

    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
    }
}
