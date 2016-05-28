package com.onsumaye.kabir.onchat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseObject;

public class LoginActivity extends AppCompatActivity {

    private boolean loggedIn = false;
    private double clientId;

    EditText usernameEditText;
    CheckBox rememberMe;

    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        clientId = 0; //Get this data from server

        usernameEditText = (EditText) findViewById(R.id.username);
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

    }

    public void onClickLogin(View v)
    {
        //Perform checks against database here
        if(usernameEditText.getText().toString().equals("") || usernameEditText.getText().toString().equals("Username"))
        {
            Toast.makeText(getApplicationContext(), "Please enter a username", Toast.LENGTH_LONG).show();
        }
        else
        {
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("username", usernameEditText.getText().toString());

            if(rememberMe.isChecked())
            {
                editor.putString("username", usernameEditText.getText().toString());
                editor.commit();
            }

            loggedIn = true;

            startActivity(intent);
        }

    }

}
