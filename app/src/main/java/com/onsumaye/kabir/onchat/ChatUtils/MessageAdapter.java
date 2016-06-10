package com.onsumaye.kabir.onchat.ChatUtils;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.onsumaye.kabir.onchat.ChatUtils.ChatHandler;
import com.onsumaye.kabir.onchat.ChatUtils.ChatMessage;
import com.onsumaye.kabir.onchat.R;

public class MessageAdapter extends BaseAdapter
{
    LayoutInflater inflater;
    Context context;

    public MessageAdapter(Activity activity)
    {
        this.context = activity;
        inflater = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return ChatHandler.chatMessageList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        TextView username, message,timeStamp;
        ImageView deliveryIcon;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ChatMessage message = ChatHandler.chatMessageList.get(position);
        Holder holder = new Holder();


        //Set the gravity of the whole thing depending on whether the username matches or not
        if(message.getUsername().equals(ChatHandler.myUsername))
        {
            //Set layout gravity to the right
            convertView = setSenderFormat();
        }
        else
        {
            //Set the layout gravity to left (normal)
            convertView = setReceiverFormat();
        }

        holder.username = (TextView) convertView.findViewById(R.id.userDisplay);
        holder.message = (TextView) convertView.findViewById(R.id.messageDisplay);
        holder.timeStamp = (TextView) convertView.findViewById(R.id.timeStamp);
        holder.deliveryIcon = (ImageView) convertView.findViewById(R.id.deliveryIcon);

        //Assign the chat message to the views
        holder.username.setText(ChatHandler.chatMessageList.get(position).getUsername());
        holder.message.setText(ChatHandler.chatMessageList.get(position).getMessage());
        holder.timeStamp.setText(ChatHandler.getTimeStamp(ChatHandler.chatMessageList.get(position).getTime()));

        return convertView;
    }

    public void addMessage(ChatMessage message)
    {
        ChatHandler.chatMessageList.add(message);
        notifyDataSetChanged();
    }

    private View setSenderFormat()
    {
        return inflater.inflate(R.layout.sender_chat_view_item, null);
    }

    private View setReceiverFormat()
    {
        return inflater.inflate(R.layout.receiver_chat_view_item, null);
    }

    public void updateSentIcon(ChatMessage message)
    {
        int position = ChatHandler.chatMessageList.indexOf(message);
        if(message.isSent())
        {
            
        }
    }


}
