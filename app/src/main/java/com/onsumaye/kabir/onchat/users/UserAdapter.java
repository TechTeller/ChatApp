package com.onsumaye.kabir.onchat.users;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onsumaye.kabir.onchat.chat.ChatHandler;
import com.onsumaye.kabir.onchat.chat.ChatMessage;
import com.onsumaye.kabir.onchat.R;
import com.onsumaye.kabir.onchat.activity.ChatActivity;
import com.onsumaye.kabir.onchat.helper.Common;

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
        TextView usernameTextView, unreadMessageCounter, lastMessageReceived, lastMessageTime;
        LinearLayout userBackground;
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
        holder.lastMessageTime = (TextView) convertView.findViewById(R.id.lastMessageTime);
        holder.userBackground = (LinearLayout) convertView.findViewById(R.id.userBackground);

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
            holder.lastMessageTime.setText(ChatHandler.getTimeStamp(lastMessage.getTime()));
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
                if(UserHandler.userMode == UserHandler.UserMode.NORMAL)
                {
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ChatHandler.currentlySpeakingTo_Id = user.getId();
                    ChatHandler.currentlySpeakingTo_username = user.getUsername().toLowerCase();
                    context.startActivity(intent);
                }
                else
                {
                    if(!user.isSelected())
                    {
                        user.setSelected(true);
                        v.setBackgroundColor(Common.chat_selectedColor);
                        UserHandler.selectedUsersList.add(user);
                    }
                    else
                    {
                        user.setSelected(false);
                        v.setBackgroundColor(Common.chat_originalColor);
                        UserHandler.selectedUsersList.remove(user);

                        if(UserHandler.selectedUsersList.isEmpty())
                        {
                            UserHandler.userMode = UserHandler.UserMode.NORMAL;
                            UserHandler.toggleActionBar();
                        }
                    }
                }
            }
        });

        holder.userBackground.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                v.setBackgroundColor(Common.chat_selectedColor);
                user.setSelected(true);
                UserHandler.userMode = UserHandler.UserMode.SELECTION;
                if(!UserHandler.selectedUsersList.contains(user))
                    UserHandler.selectedUsersList.add(user);
                UserHandler.toggleActionBar();
                return true;
            }
        });

        return convertView;
    }
}
