package com.onsumaye.kabir.onchat;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.onsumaye.kabir.onchat.ChatUtils.ChatHandler;
import com.onsumaye.kabir.onchat.ChatUtils.ChatMessage;

import java.util.Date;

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
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Holder holder = new Holder();
        System.out.print("Updating view");
        convertView = inflater.inflate(R.layout.chat_view_item, null);
        holder.username = (TextView) convertView.findViewById(R.id.userDisplay);
        holder.message = (TextView) convertView.findViewById(R.id.messageDisplay);
        holder.timeStamp = (TextView) convertView.findViewById(R.id.timeStamp);

        //Assign the chat message to the views
        holder.username.setText(ChatHandler.chatMessageList.get(position).getUsername());
        holder.message.setText(ChatHandler.chatMessageList.get(position).getMessage());
        holder.timeStamp.setText(ChatHandler.chatMessageList.get(position).getTimeStamp());

        return convertView;
    }

    public void addMessage(ChatMessage message)
    {
        ChatHandler.chatMessageList.add(message);
        notifyDataSetChanged();
    }
}
