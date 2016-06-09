package com.onsumaye.kabir.onchat.users;


public class User
{
    private String id;
    private String username;
    private String gcmId;
    private int unreadMessages;
    public User(String id, String username, String gcmId)
    {
        this.id = id;
        this.username = username;
        this.gcmId = gcmId;
        unreadMessages = 0;
    }

    public void setUnreadMessages(int unreadMessages)
    {
        this.unreadMessages = unreadMessages;
    }

}
