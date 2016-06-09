package com.onsumaye.kabir.onchat.users;


import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class UsersAdapter extends BaseAdapter
{
    LayoutInflater inflater;
    Context context;

    public UsersAdapter(Activity activity)
    {
        this.context = activity;
        inflater = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        return UsersHandler.usersList.size();
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        return convertView;
    }
}
