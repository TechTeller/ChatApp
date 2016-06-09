package com.onsumaye.kabir.onchat.ChatUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ChatMessage
{
    private long id;
    private String username;
    private String message;
    private long time;
    private boolean sent;

    public ChatMessage(String username, String message, String time)
    {
        this.username = username;
        this.message = message;
        this.time = Long.parseLong(time);
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
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

    public void setSent(boolean val)
    {
        sent = val;
    }

    public boolean isSent()
    {
        return sent;
    }
}
