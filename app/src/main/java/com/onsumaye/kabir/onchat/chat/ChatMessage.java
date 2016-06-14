package com.onsumaye.kabir.onchat.chat;

public class ChatMessage
{
    private int id;
    private String username;
    private String message;
    private long time;
    private boolean sent;
    private int toId;
    private boolean isSelected;
    private boolean read;

    public ChatMessage(int id, String username, String message, String time, int toId)
    {
        this.id = id;
        this.username = username;
        this.message = message;
        this.time = Long.parseLong(time);
        this.toId = toId;
        this.read = false;
        isSelected = false;
    }

    public ChatMessage(int id, String username, String message, String time, int toId, boolean read)
    {
        this.id = id;
        this.username = username;
        this.message = message;
        this.time = Long.parseLong(time);
        this.toId = toId;
        isSelected = false;
        this.read = read;
    }

    public ChatMessage(String username, String message, String time, int toId)
    {
        this.username = username;
        this.message = message;
        this.time = Long.parseLong(time);
        this.toId = toId;
        isSelected = false;
        this.read = false;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
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

    public int getToId()
    {
        return toId;
    }

    public void setToId(int toId )
    {
        this.toId = toId;
    }

    public boolean isSelected()
    {
        return isSelected;
    }

    public void setSelected(boolean selected)
    {
        isSelected = selected;
    }

    public boolean isRead()
    {
        return read;
    }

    public void setRead(boolean read)
    {
        this.read = read;
    }
}
