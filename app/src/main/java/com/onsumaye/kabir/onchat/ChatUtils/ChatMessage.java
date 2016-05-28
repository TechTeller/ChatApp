package com.onsumaye.kabir.onchat.ChatUtils;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ChatMessage
{
    private String username;
    private String message;
    private long time;

    public ChatMessage(String username, String message, long time)
    {
        this.username = username;
        this.message = message;
        this.time = time;
    }

    public JSONObject serialize()
    {
        JSONObject obj = new JSONObject();
        try {
            obj.put("username", username);
            obj.put("message", message);
            obj.put("time", time);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return obj;
    }

    public void deserialize(JSONObject obj)
    {
        try
        {
            username = obj.getString("username");
            message = obj.getString("message");
            time = obj.getLong("time");
        }
        catch(JSONException e)
        {
            username = "null";
            message = "An error occured in deserializing the JSON Object. Please check stacktrace";
            time = 0;
            e.printStackTrace();
        }
    }

    public String getUsername()
    {
        return username;
    }

    public String getMessage()
    {
        return message;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getTimeStamp()
    {
        String timeStamp;
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm", Locale.getDefault());
        timeStamp = formatter.format(new Date(time));
        return timeStamp;
    }
}
