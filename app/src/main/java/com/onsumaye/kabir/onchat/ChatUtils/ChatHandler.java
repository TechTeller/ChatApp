package com.onsumaye.kabir.onchat.ChatUtils;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

public class ChatHandler
{
    public static List<ChatMessage> chatMessageList;


    //Initialize the username and chatmessage
    public static void init()
    {
        chatMessageList = new ArrayList<ChatMessage>();

    }

    public static void sendMessage(ChatMessage message)
    {
        ParseObject messageObject = new ParseObject("chatMessage");
        messageObject.put("username", message.getMessage());
        messageObject.put("message", message.getMessage());
        messageObject.saveInBackground();

        chatMessageList.add(message); //Later replace this code with sending message and querying the server for it
    }
}
