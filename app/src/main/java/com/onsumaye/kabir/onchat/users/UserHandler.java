package com.onsumaye.kabir.onchat.users;


import java.util.ArrayList;

public class UserHandler
{
    public static ArrayList<User> usersList;

    public static void init()
    {
        usersList = new ArrayList<User>();
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
