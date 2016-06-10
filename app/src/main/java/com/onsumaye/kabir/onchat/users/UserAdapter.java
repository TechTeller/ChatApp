package com.onsumaye.kabir.onchat.users;


import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.onsumaye.kabir.onchat.R;

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
        TextView usernameTextView;
        TextView unreadMessageTextView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        User user = UserHandler.usersList.get(position);
        Holder holder = new Holder();

        convertView = inflater.inflate(R.layout.user_list_view_item, null);

        holder.unreadMessageTextView = (TextView) convertView.findViewById(R.id.unreadMessagesTextView);
        holder.usernameTextView = (TextView) convertView.findViewById(R.id.usernameTextView);

        holder.unreadMessageTextView.setText(String.valueOf(UserHandler.usersList.get(position).getUnreadMessages()));
        holder.usernameTextView.setText(UserHandler.usersList.get(position).getUsername());

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
