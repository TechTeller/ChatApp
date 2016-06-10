package com.onsumaye.kabir.onchat.activity;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.onsumaye.kabir.onchat.ChatUtils.ChatHandler;
import com.onsumaye.kabir.onchat.R;
import com.onsumaye.kabir.onchat.app.Config;
import com.onsumaye.kabir.onchat.users.User;
import com.onsumaye.kabir.onchat.users.UserAdapter;
import com.onsumaye.kabir.onchat.users.UserHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class UsersActivity extends AppCompatActivity
{
    ListView usersListView;
    FloatingActionButton fab;
    UserAdapter userAdapter;
    EditText addUserEditText;

    ButtonState state = ButtonState.ADD;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        UserHandler.init();
        ChatHandler.myUsername = getIntent().getStringExtra("username");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Users");
        setTitleColor(R.color.common_plus_signin_btn_text_light);
        userAdapter = new UserAdapter(getApplicationContext());

        usersListView = (ListView) findViewById(R.id.userListView);
        usersListView.setAdapter(userAdapter);

        fab = (FloatingActionButton) findViewById(R.id.addUserButton);
        addUserEditText = (EditText) findViewById(R.id.addUserEditText);

        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                toggleAddUserButton();
            }
        });

    }

    private void toggleAddUserButton()
    {
        if(state == ButtonState.ADD)
        {
            addUserEditText.setText("");
            addUserEditText.setVisibility(View.VISIBLE);
            fab.setImageResource(R.drawable.ic_done_white_18px);
            state = ButtonState.ACCEPT;
        }
        else
        {
            addUserEditText.setVisibility(View.INVISIBLE);
            fab.setImageResource(R.drawable.ic_add_white_18px);
            if(!addUserEditText.getText().toString().equals(""))
            {
                //Send request to the server which will search the database for user existing
                String userToAdd = addUserEditText.getText().toString();
                if(!userToAdd.equals(ChatHandler.myUsername))
                {
                    addUser(userToAdd);
                }
                else Toast.makeText(getApplicationContext(), "You cannot add yourself!", Toast.LENGTH_SHORT).show();
            }
            state = ButtonState.ADD;
        }
    }

    private void addUser(String username)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("username", username);

        client.post(Config.SERVER_IP + "/addUserId", params, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                System.out.println("Status code: " + statusCode);
                try
                {
                    boolean userExists = response.getBoolean("exists");
                    if(userExists)
                    {
                        int id = response.getInt("id");
                        String username = response.getString("username");
                        String gcmId = response.getString("gcmId");

                        User user = new User(id, username, gcmId, 0);
                        System.out.println(user.toString());
                        userAdapter.addUser(user);
                        usersListView.setAdapter(userAdapter);
                    }
                    else Toast.makeText(getApplicationContext(), "User does not exist.", Toast.LENGTH_SHORT).show();
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response)
            {
                System.out.println("Got status code" + statusCode);
            }

        });
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    private enum ButtonState
    {
        ADD, ACCEPT
    }
}
