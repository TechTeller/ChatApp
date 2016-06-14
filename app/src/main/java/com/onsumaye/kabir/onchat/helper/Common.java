package com.onsumaye.kabir.onchat.helper;


import com.onsumaye.kabir.onchat.chat.ChatHandler;

public class Common
{
    public static int chat_selectedColor = android.graphics.Color.parseColor("#97d0f3");
    public static int chat_originalColor = android.graphics.Color.parseColor("#f0f0f0");

    public static String capitalizeFirstLetter(String s)
    {
        String s1 = s.substring(0, 1).toUpperCase();
        String nameCapitalized = s1 + s.substring(1);
        return nameCapitalized;
    }
}
