package com.onsumaye.kabir.onchat.users;


import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;

import com.onsumaye.kabir.onchat.chat.ChatHandler;
import com.onsumaye.kabir.onchat.activity.UsersActivity;
import com.onsumaye.kabir.onchat.chat.ChatMessage;
import com.onsumaye.kabir.onchat.storage.ChatMessageDatabaseHandler;
import com.onsumaye.kabir.onchat.storage.UserDatabaseHandler;

import java.util.ArrayList;

public class UserHandler
{
    public enum UserMode
    {
        NORMAL, SELECTION
    }

    public static UserMode userMode;

    public static ArrayList<User> usersList;
    public static ArrayList<User> selectedUsersList;
    private static UsersActivity userActivity;
    public static UserDatabaseHandler userDatabaseHandler;

    public static void init(UsersActivity context)
    {
        usersList = new ArrayList<User>();
        selectedUsersList = new ArrayList<User>();

        ChatHandler.chatMessageDatabaseHandler = new ChatMessageDatabaseHandler(context);
        ChatHandler.chatMessageDatabaseHandler.createDatabase();

        userDatabaseHandler = new UserDatabaseHandler(context);
        userActivity = context;

        userMode = UserMode.NORMAL;
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

    public static User getUserByUsername(String username)
    {
        for(User user : usersList)
        {
            if(user.getUsername().equals(username))
            {
                return user;
            }
        }
        return null;
    }

    public static void moveUserToTop(User user)
    {
        usersList.remove(user);
        usersList.add(0, user);
    }

    public static void deleteUser()
    {
        for(User user : selectedUsersList)
        {
            //Delete user from the users list
            usersList.remove(user);
            //Delete user from the database so it does not appear on relaunching the activity
            userDatabaseHandler.deleteUser(user);
        }
        selectedUsersList.clear();
        userMode = UserMode.NORMAL;
        toggleActionBar();

        //refresh adapter
        Intent intent = new Intent("refreshAdapterIntent");
        LocalBroadcastManager.getInstance(userActivity).sendBroadcast(intent);
    }

    public static void toggleActionBar()
    {
        if(userMode == UserMode.SELECTION)
        {
            userActivity.loggedInAs.setVisibility(View.INVISIBLE);
            userActivity.toolbar_deleteButton.setVisibility(View.VISIBLE);
        }
        else
        {
            userActivity.loggedInAs.setVisibility(View.VISIBLE);
            userActivity.toolbar_deleteButton.setVisibility(View.INVISIBLE);
        }
    }

}
