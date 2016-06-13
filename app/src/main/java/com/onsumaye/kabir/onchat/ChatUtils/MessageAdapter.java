package com.onsumaye.kabir.onchat.ChatUtils;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.onsumaye.kabir.onchat.R;
import com.onsumaye.kabir.onchat.helper.Color;

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
        TextView message,timeStamp;
        LinearLayout chatBackground;
        ImageView deliveryIcon;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final ChatMessage message = ChatHandler.chatMessageList.get(position);
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

        //Initialize the view elements
        holder.message = (TextView) convertView.findViewById(R.id.messageDisplay);
        holder.timeStamp = (TextView) convertView.findViewById(R.id.timeStamp);
        holder.chatBackground = (LinearLayout) convertView.findViewById(R.id.chatBackground);
        //holder.deliveryIcon = (ImageView) convertView.findViewById(R.id.deliveryIcon);


        //Assign the chat message to the views
        holder.message.setText(ChatHandler.chatMessageList.get(position).getMessage());
        holder.timeStamp.setText(ChatHandler.getTimeStamp(ChatHandler.chatMessageList.get(position).getTime()));

        if(!message.isSelected())
        {
            holder.chatBackground.setBackgroundColor(Color.chat_originalColor);
        }
        else
        {
            holder.chatBackground.setBackgroundColor(Color.chat_selectedColor);
        }

        holder.chatBackground.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                if(ChatHandler.chatMode == ChatHandler.ChatMode.NORMAL)
                {
                    v.setBackgroundColor(Color.chat_selectedColor);
                    message.setSelected(true);
                    ChatHandler.chatMode = ChatHandler.ChatMode.SELECTION;
                    ChatHandler.selectedChatMessageList.add(message);
                    ChatHandler.toggleActionBar();
                }
                return true;
            }
        });

        holder.chatBackground.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(ChatHandler.chatMode == ChatHandler.ChatMode.SELECTION)
                {
                    if(!message.isSelected())
                    {
                        message.setSelected(true);
                        v.setBackgroundColor(Color.chat_selectedColor);
                        ChatHandler.selectedChatMessageList.add(message);
                    }
                    else
                    {
                        message.setSelected(false);
                        v.setBackgroundColor(Color.chat_originalColor);
                        ChatHandler.selectedChatMessageList.remove(message);

                        if(ChatHandler.selectedChatMessageList.isEmpty())
                        {
                            ChatHandler.chatMode = ChatHandler.ChatMode.NORMAL;
                            ChatHandler.toggleActionBar();
                        }
                    }
                }
            }
        });

        if(!ChatHandler.readChatMessageList.contains(message))
        {
            holder.chatBackground.setAlpha(0.0f);
            holder.chatBackground.animate()
                    .alpha(1.0f);
            ChatHandler.readChatMessageList.add(message);
        }

        return convertView;
    }

    public void addMessage(ChatMessage message)
    {
        ChatHandler.chatMessageList.add(message);
        notifyDataSetChanged();
    }

    public void removeMessage(ChatMessage message)
    {
        ChatHandler.chatMessageList.remove(message);
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
