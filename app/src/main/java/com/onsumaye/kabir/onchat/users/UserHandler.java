package com.onsumaye.kabir.onchat.users;


import com.onsumaye.kabir.onchat.chat.ChatHandler;
import com.onsumaye.kabir.onchat.activity.UsersActivity;
import com.onsumaye.kabir.onchat.storage.ChatMessageDatabaseHandler;
import com.onsumaye.kabir.onchat.storage.UserDatabaseHandler;

import java.util.ArrayList;

public class UserHandler
{
    public static ArrayList<User> usersList;
    private static UsersActivity userActivity;
    public static UserDatabaseHandler userDatabaseHandler;

    public static void init(UsersActivity context)
    {
        usersList = new ArrayList<User>();

        ChatHandler.chatMessageDatabaseHandler = new ChatMessageDatabaseHandler(context);
        ChatHandler.chatMessageDatabaseHandler.createDatabase();
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

    public static void reset()
    {
        usersList.clear();
    }

}
