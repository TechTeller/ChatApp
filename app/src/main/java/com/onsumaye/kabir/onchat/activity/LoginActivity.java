package com.onsumaye.kabir.onchat.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.onsumaye.kabir.onchat.chat.ChatHandler;
import com.onsumaye.kabir.onchat.R;
import com.onsumaye.kabir.onchat.app.Config;
import com.onsumaye.kabir.onchat.app.StateHolder;
import com.onsumaye.kabir.onchat.gcm.GcmIntentService;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    private boolean loggedIn = false;
    private double clientId;
    private String TAG = LoginActivity.class.getSimpleName();

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    EditText usernameEditText;
    CheckBox rememberMe;


    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        clientId = 0; //Get this data from server

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    String token = intent.getStringExtra("token");

                } else if (intent.getAction().equals(Config.SENT_TOKEN_TO_SERVER)) {
                    // gcm registration id is stored in our server's MySQL


                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                }
            }
        };

        usernameEditText = (EditText) findViewById(R.id.usernameTextView);
        rememberMe = (CheckBox) findViewById(R.id.rememberMe);

        String savedUsername = getSharedPreferences("loginPrefs", MODE_PRIVATE).getString("username", null);
        if(savedUsername != null)
        {
            rememberMe.setChecked(true);
            usernameEditText.setTextColor(Color.BLACK);
            usernameEditText.setText(savedUsername);
        }

        editor = getSharedPreferences("loginPrefs", MODE_PRIVATE).edit();

        usernameEditText.setOnTouchListener(
                new View.OnTouchListener()
                {
                    @Override
                    public boolean onTouch(View v, MotionEvent event)
                    {
                        if(usernameEditText.getText().toString().equals("Username"))
                        {
                            usernameEditText.setText("");
                            usernameEditText.setTextColor(Color.BLACK);
                        }
                        return false;
                    }
                });

        usernameEditText.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if(event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_ENTER:
                            onClickLogin(findViewById(R.id.loginButton));
                    }
                }
                return false;
            }
        });
    }

    public void onClickLogin(final View v)
    {
        v.setEnabled(false);

        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        try
        {
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        catch (java.lang.NullPointerException e)
        {
            e.printStackTrace();
        }

        Toast.makeText(this, "Logging in...", Toast.LENGTH_LONG).show();
        //Perform checks against database here
        if(usernameEditText.getText().toString().equals("") || usernameEditText.getText().toString().equals("Username"))
        {
            Toast.makeText(getApplicationContext(), "Please enter a username", Toast.LENGTH_LONG).show();
        }
        else
        {
            final Intent intent = new Intent(this, UsersActivity.class);
            intent.putExtra("username", usernameEditText.getText().toString());

            if(rememberMe.isChecked())
            {
                editor.putString("username", usernameEditText.getText().toString());
                editor.commit();
            }

            if (checkPlayServices())
            {
                registerGCM();
            }

            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();

            client.post(Config.SERVER_IP + "/login", params, new JsonHttpResponseHandler()
            {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response)
                {
                    System.out.println("Status code: " + statusCode);
                    if(statusCode == 200)
                    {
                        v.setEnabled(true);
                        startActivity(intent);
                    }
                    else Toast.makeText(getApplicationContext(), "Failed to connect to chat server.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response)
                {
                    System.out.println("Got status code" + statusCode);
                    Toast.makeText(getApplicationContext(), "Failed to connect to chat server.", Toast.LENGTH_SHORT).show();
                }

            });
        }

        ChatHandler.myUsername = usernameEditText.getText().toString().toLowerCase();
    }

    // starting the service to register with GCM
    private void registerGCM() {
        Intent intent = new Intent(this, GcmIntentService.class);
        intent.putExtra("key", "register");
        startService(intent);
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported. Google Play Services not installed!");
                Toast.makeText(getApplicationContext(), "This device is not supported. Google Play Services not installed!", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        StateHolder.appState = StateHolder.AppState.LOGIN;
    }



}
