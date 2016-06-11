package com.onsumaye.kabir.onchat.storage;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.onsumaye.kabir.onchat.ChatUtils.ChatMessage;

import java.util.ArrayList;
import java.util.List;

public class ChatMessageDatabaseHandler extends SQLiteOpenHelper
{
    public static final int DATABASE_VERSION = 2;

    public static final String DATABASE_NAME = "messageDatabase";

    public static final String TABLE_MESSAGES = "messages";

    //ChatMessages Table Column Names
    public static final String KEY_ID = "id";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_TIMESTAMP = "timestamp";
    public static final String KEY_TOID = "toId";

    public ChatMessageDatabaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_MESSAGES_TABLE = "CREATE TABLE "  + TABLE_MESSAGES + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_USERNAME + " TEXT,"
                + KEY_MESSAGE + " TEXT,"
                + KEY_TIMESTAMP + " INT,"
                + KEY_TOID + " INT"
                + ")";
        db.execSQL(CREATE_MESSAGES_TABLE);

        System.out.println("Created chat messages table in database.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);

        onCreate(db);
    }

    public void addChatMessage(ChatMessage cMessage)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, cMessage.getId());
        values.put(KEY_USERNAME, cMessage.getUsername());
        values.put(KEY_MESSAGE, cMessage.getMessage());
        values.put(KEY_TIMESTAMP, cMessage.getTime());
        values.put(KEY_TOID, cMessage.getToId());

        db.insert(TABLE_MESSAGES, null, values);
        db.close();
    }

    public ChatMessage getChatMessage(int id)
    {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_MESSAGES, new String[] { KEY_ID, KEY_USERNAME, KEY_MESSAGE, KEY_TIMESTAMP, KEY_TOID },
                KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if( cursor != null )
            cursor.moveToFirst();

        ChatMessage cMessage = new ChatMessage(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2),
                cursor.getString(3), Integer.parseInt(cursor.getString(4)));

        return cMessage;
    }

    public int getChatMessageCount()
    {
        String countQuery = "SELECT * FROM " + TABLE_MESSAGES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    public List<ChatMessage> getAllChatMessagesFromUser(String username)
    {
        System.out.println("Getting messages of User: " + username);
        List<ChatMessage> chatMessageList = new ArrayList<ChatMessage>();

        String selectQuery = "SELECT * FROM " + TABLE_MESSAGES + " WHERE username= \"" + username + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do
            {
                ChatMessage cMessage = new ChatMessage(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), Integer.parseInt(cursor.getString(4)));
                chatMessageList.add(cMessage);
            }
            while (cursor.moveToNext());
        }


        System.out.println("Message 1" + chatMessageList.get(0));

        // return ChatMessage list
        return chatMessageList;
    }

    public int updateChatMessage(ChatMessage cMessage)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_ID, cMessage.getId());
        values.put(KEY_USERNAME, cMessage.getUsername());
        values.put(KEY_MESSAGE, cMessage.getMessage());
        values.put(KEY_TIMESTAMP, cMessage.getTime());
        values.put(KEY_TOID, cMessage.getToId());

        return db.update(TABLE_MESSAGES, values, KEY_ID + " = ?",
                new String[] { String.valueOf(cMessage.getId()) });
    }

    public void deleteChatMessage(ChatMessage cMessage)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MESSAGES, KEY_ID + "= ?", new String[] { String.valueOf(cMessage.getId() )});
        db.close();
    }
    }
