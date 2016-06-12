package com.onsumaye.kabir.onchat.users;


import android.content.Context;

import com.onsumaye.kabir.onchat.ChatUtils.ChatHandler;
import com.onsumaye.kabir.onchat.storage.ChatMessageDatabaseHandler;

import java.util.ArrayList;

public class UserHandler
{
    public static ArrayList<User> usersList;

    public static void init(Context context)
    {
        usersList = new ArrayList<User>();

        ChatHandler.chatMessageDatabaseHandler = new ChatMessageDatabaseHandler(context);
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

}
