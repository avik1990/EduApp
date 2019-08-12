package com.app.eduapp.sqlitedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.app.eduapp.helper.EdUtils;
import com.app.eduapp.pojo.ModelInbox;

import java.util.ArrayList;
import java.util.List;


public class LCDatabaseHandler extends SQLiteOpenHelper {
    private static LCDatabaseHandler instance;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "LCDataBaseManager";
    private static final String TABLE_INBOX = "lc_inbox";
    ////////////////////////////////////
    ////////////// inbox .........................................
    private static final String KEY_INBOX_ID = "inbox_id";
    private static final String KEY_INBOX_MSG = "inbox_msg";
    private static final String KEY_INBOX_FLAG = "inbox_flag";
    private static final String KEY_INBOX_DATE = "inbox_date";
    private static final String KEY_INBOX_TITLE = "inbox_title";
    private static final String KEY_INBOX_DETAILS = "inbox_details";
    private static final String KEY_INBOX_SUBTITLE = "inbox_subtitle";
    private static final String KEY_INBOX_IMAGEURL = "inbox_imageurl";
    private static final String KEY_INBOX_READ_STATUS = "inbox_read_status";
    private static final String KEY_INBOX_CURRENT_TIME = "inbox_current_time";

    public static synchronized LCDatabaseHandler getInstance(Context context) {
        if (instance == null) {
            instance = new LCDatabaseHandler(context);
        }
        return instance;
    }

    SQLiteDatabase mdb;

    public LCDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mdb = getWritableDatabase();
        onCreate(mdb);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        /////////////////////// inbox/////////////
        String CREATE_INBOX_TABLE = "CREATE TABLE  IF NOT EXISTS " + TABLE_INBOX + "("
                + KEY_INBOX_ID + " INTEGER PRIMARY KEY  AUTOINCREMENT,"
                + KEY_INBOX_MSG + " TEXT,"
                + KEY_INBOX_TITLE + " TEXT,"
                + KEY_INBOX_DATE + " TEXT,"
                + KEY_INBOX_FLAG + " TEXT,"
                + KEY_INBOX_DETAILS + " TEXT,"
                + KEY_INBOX_IMAGEURL + " TEXT,"
                + KEY_INBOX_SUBTITLE + " TEXT,"
                + KEY_INBOX_CURRENT_TIME + " TEXT,"
                + KEY_INBOX_READ_STATUS + " TEXT" + ")";


        db.execSQL(CREATE_INBOX_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INBOX);
        onCreate(db);
    }

    public long getNotificationCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_INBOX);
        db.close();
        return count;
    }


    public void insertInbox(ModelInbox inbox) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_INBOX_MSG, inbox.getInbox_msg());
        values.put(KEY_INBOX_DATE, inbox.getInbox_date());
        values.put(KEY_INBOX_FLAG, inbox.getInbox_flag());
        values.put(KEY_INBOX_TITLE, inbox.getInbox_title());
        values.put(KEY_INBOX_DETAILS, inbox.getInbox_details());
        values.put(KEY_INBOX_SUBTITLE, inbox.getInbox_subtitle());
        values.put(KEY_INBOX_IMAGEURL, inbox.getInbox_imageurl());
        values.put(KEY_INBOX_READ_STATUS, inbox.getInbox_read_status());
        values.put(KEY_INBOX_CURRENT_TIME, inbox.getInbox_current_time());

        db.insert(TABLE_INBOX, null, values);
        Log.d("inserted_inbox_values", String.valueOf(values));
        db.close();
    }

    public List<ModelInbox> getPushlist(String type) {
        List<ModelInbox> pushList = new ArrayList<>();
        int now=Integer.parseInt(EdUtils.getCurrentDateMinus())-7;

        //String selectQuery = "SELECT * FROM " + TABLE_INBOX + " where " + KEY_INBOX_FLAG + " = " + type;
        String selectQuery = "SELECT * FROM lc_inbox where inbox_flag = '1'  and inbox_read_status > " + now + " order by inbox_date desc";
        Log.d("PostQuery", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                ModelInbox pushes = new ModelInbox();
                pushes.setInboxId(cursor.getString((cursor.getColumnIndex(KEY_INBOX_ID))));
                pushes.setInbox_msg(cursor.getString((cursor.getColumnIndex(KEY_INBOX_MSG))));
                pushes.setInbox_flag(cursor.getString((cursor.getColumnIndex(KEY_INBOX_FLAG))));
                pushes.setInbox_date(cursor.getString((cursor.getColumnIndex(KEY_INBOX_DATE))));
                pushes.setInbox_title(cursor.getString((cursor.getColumnIndex(KEY_INBOX_TITLE))));
                pushes.setInbox_details(cursor.getString((cursor.getColumnIndex(KEY_INBOX_DETAILS))));
                pushes.setInbox_imageurl(cursor.getString((cursor.getColumnIndex(KEY_INBOX_IMAGEURL))));
                pushes.setInbox_subtitle(cursor.getString((cursor.getColumnIndex(KEY_INBOX_SUBTITLE))));
                pushes.setInbox_read_status(cursor.getString((cursor.getColumnIndex(KEY_INBOX_READ_STATUS))));
                pushes.setInbox_current_time(cursor.getString((cursor.getColumnIndex(KEY_INBOX_CURRENT_TIME))));
                pushList.add(pushes);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return pushList;
    }


    public void deleteNote(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_INBOX, KEY_INBOX_ID + " = ?",
                new String[]{id});
        db.close();
    }
}
