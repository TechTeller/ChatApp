package com.onsumaye.kabir.onchat.storage;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.onsumaye.kabir.onchat.users.User;
import com.onsumaye.kabir.onchat.users.UserHandler;

import java.util.ArrayList;
import java.util.List;

public class UserDatabaseHandler extends SQLiteOpenHelper
{
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "userDatabase";

    public static final String TABLE_USERS = "users";

    //Users Table Column Names
    public static final String KEY_ID = "id";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_GCMID = "gcmId";

    public UserDatabaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_USERS_TABLE = "CREATE TABLE "  + TABLE_USERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USERNAME + " TEXT,"
                + KEY_GCMID + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        onCreate(db);
    }

    public void addUser(User user)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, user.getId());
        values.put(KEY_USERNAME, user.getUsername());
        values.put(KEY_GCMID, user.getGcmId());

        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    public User getUser(int id)
    {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS, new String[] { KEY_ID, KEY_USERNAME, KEY_GCMID },
                KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if( cursor != null )
            cursor.moveToFirst();

        User user = new User(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));

        return user;
    }

    public int getUserCount()
    {
        String countQuery = "SELECT * FROM " + TABLE_USERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    public List<User> getAllUsers()
    {
        List<User> UserList = new ArrayList<User>();

        String selectQuery = "SELECT  * FROM " + TABLE_USERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do
            {
                User user = new User(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
                UserList.add(user);
            }
            while (cursor.moveToNext());
        }

        // return User list
        return UserList;
        
    }
    public int updateUser(User user)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_ID, user.getId());
        values.put(KEY_USERNAME, user.getUsername());
        values.put(KEY_GCMID, user.getGcmId());

        return db.update(TABLE_USERS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(user.getId()) });
    }

    public void deleteUser(User user)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, KEY_ID + "= ?", new String[] { String.valueOf(user.getId() )});
        db.close();
    }


}
