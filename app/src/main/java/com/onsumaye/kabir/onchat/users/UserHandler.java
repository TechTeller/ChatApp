package com.onsumaye.kabir.onchat.users;


import android.content.Context;

import com.onsumaye.kabir.onchat.ChatUtils.ChatHandler;
import com.onsumaye.kabir.onchat.activity.UsersActivity;
import com.onsumaye.kabir.onchat.storage.ChatMessageDatabaseHandler;

import java.util.ArrayList;

public class UserHandler
{
    public static ArrayList<User> usersList;
    private static UsersActivity userActivity;

    public static void init(UsersActivity context)
    {
        usersList = new ArrayList<User>();

        ChatHandler.chatMessageDatabaseHandler = new ChatMessageDatabaseHandler(context);
        userActivity = context;
    }

    public static boolean doesUserExist(int id)
    {
        for(User user: usersList)
        {
            if(user.getId() == id)
            {
                return true;
            }
        }
        return false;
    }

    public static User getUserById(int id)
    {
        for(User user : usersList)
        {
            if(user.getId() == id)
            {
                return user;
            }
        }
        return null;
    }

    public static void refreshUserAdapter()
    {
        userActivity.refreshAdapter();
    }

}
