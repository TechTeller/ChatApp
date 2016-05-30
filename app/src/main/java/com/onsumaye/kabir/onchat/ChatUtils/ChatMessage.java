package com.onsumaye.kabir.onchat.ChatUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;

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

    public long getTime()
    {
        return time;
    }
}
