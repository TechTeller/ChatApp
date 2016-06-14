package com.onsumaye.kabir.onchat.users;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.onsumaye.kabir.onchat.chat.ChatHandler;
import com.onsumaye.kabir.onchat.chat.ChatMessage;
import com.onsumaye.kabir.onchat.R;
import com.onsumaye.kabir.onchat.activity.ChatActivity;

public class UserAdapter extends BaseAdapter
{
    LayoutInflater inflater;
    Context context;

    public UserAdapter(Context context)
    {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        return UserHandler.usersList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return position;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    private class Holder
    {
        TextView usernameTextView, unreadMessageCounter, lastMessageReceived;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final User user = UserHandler.usersList.get(position);
        Holder holder = new Holder();

        convertView = inflater.inflate(R.layout.user_list_view_item, null);

        holder.unreadMessageCounter = (TextView) convertView.findViewById(R.id.unreadMessagesTextView);
        holder.usernameTextView = (TextView) convertView.findViewById(R.id.usernameTextView);
        holder.lastMessageReceived = (TextView) convertView.findViewById(R.id.lastMessage);

        if(ChatHandler.getUnreadMessageCount(user) == 0)
        {
            //There are no unread messages
            holder.unreadMessageCounter.setVisibility(View.INVISIBLE);
        }
        else
        {
            //There are a few unread messages
            holder.unreadMessageCounter.setVisibility(View.VISIBLE);
        }

        holder.unreadMessageCounter.setText(String.valueOf(ChatHandler.getUnreadMessageCount(user)));
        holder.usernameTextView.setText(UserHandler.usersList.get(position).getUsername());
        if(!ChatHandler.chatMessageDatabaseHandler.getAllChatMessagesFromUser(user).isEmpty())
        {
            int lastMessageIndex = ChatHandler.chatMessageDatabaseHandler.getAllChatMessagesFromUser(user).size() - 1;
            ChatMessage lastMessage = ChatHandler.chatMessageDatabaseHandler.getAllChatMessagesFromUser(user).get(lastMessageIndex);
            holder.lastMessageReceived.setText(lastMessage.getMessage());
        }
        else
        {
            holder.lastMessageReceived.setText("");
        }

        convertView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ChatHandler.currentlySpeakingTo_Id = user.getId();
                ChatHandler.currentlySpeakingTo_username = user.getUsername().toLowerCase();
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    public void addUser(User user)
    {
        if(!UserHandler.doesUserExist(user.getId()))
        {
            UserHandler.usersList.add(user);
        }
        else Toast.makeText(context, "User already exists in the list.", Toast.LENGTH_SHORT).show();
        notifyDataSetChanged();
    }
}
