package com.onsumaye.kabir.onchat.ChatUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartListenerReceiver extends BroadcastReceiver
{
    public StartListenerReceiver()
    {

    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Intent startServiceIntent = new Intent(context, MessageListenerService.class);
        context.startService(startServiceIntent);
    }
}
