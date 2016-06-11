package com.onsumaye.kabir.onchat.users;


public class User
{
    private int id;
    private String username;
    private String gcmId;
    private int unreadMessages;
    public User(int id, String username, String gcmId)
    {
        this.id = id;
        this.username = username;
        this.gcmId = gcmId;
        unreadMessages = 0;
    }

    public User(int id, String username, String gcmId, int unreadMessages)
    {
        this.id = id;
        this.username = username;
        this.gcmId = gcmId;
        this.unreadMessages = unreadMessages;
    }

    public void setUnreadMessages(int unreadMessages)
    {
        this.unreadMessages = unreadMessages;
    }

    public int getUnreadMessages()
    {
        return unreadMessages;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getUsername()
    {
        return username;
    }

    public int getId()
    {
        return id;
    }

    public String getGcmId()
    {
        return gcmId;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setGcmId(String gcmId)
    {
        this.gcmId = gcmId;
    }

}
