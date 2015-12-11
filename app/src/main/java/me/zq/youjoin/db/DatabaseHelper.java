package me.zq.youjoin.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * YouJoin-Android
 * Created by ZQ on 2015/12/11.
 */
public class DatabaseHelper extends SQLiteOpenHelper{
    private static SQLiteDatabase mDb;
    private static DatabaseHelper mHelper;

    public static final String DB_NAME = "youjoin_db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_USER_INFO = "yj_user_info";
    public static final String TABLE_FRIEND = "yj_friend";
    public static final String TABLE_TWEETS = "yj_tweets";
    public static final String TABLE_PRIMSG = "yj_primsg";

    private static final String CREATE_TABLE_USER_INFO = "create table "
            + TABLE_USER_INFO
            + " (user_id integer primary key,"
            + " user_name text not null,"
            + " user_email text not null,"
            + " user_sex int not null,"
            + " user_birth date not null,"
            + " user_location text not null,"
            + " user_photo text not null,"
            + " user_sign text not null,"
            + " user_work text not null,"
            + " user_nickname text not null,"
            + " focus_num integer not null,"
            + " follow_num integer not null)";
    private static final String CREATE_TABLE_FRIEND = "create table "
            + TABLE_FRIEND
            + " (friend_id integer primary key,"
            + " user1_id integer not null,"
            + " user2_id integer not null,"
            + " friend_time timestamp not null)";
    private static final String CREATE_TABLE_TWEETS = "create table "
            + TABLE_TWEETS
            + " (tweets_id integer primary key,"
            + "user_id integer not null,"
            + "tweets_content text not null,"
            + "tweets_img text,"
            + "tweets_time timestamp not null,"
            + "comment_num integer not null,"
            + "upvote_num integer not null)";
    private static final String CREATE_TABLE_PRIMSG = "create table "
            + TABLE_PRIMSG
            + " (primsg_id integer primary key,"
            + " sender_id integer not null,"
            + " receiver_id integer not null,"
            + " primsg_time timestamp not null,"
            + " primsg_status integer not null,"
            + " primsg_content text not null)";

//    private static final String CREATE_TABLE_MUSIC = "create table "
//            + TABLE_MUSIC
//            + " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
//            + " songid integer, albumid integer, duration integer, musicname varchar(10), "
//            + " artist char, data char, folder char, musicnamekey char, artistkey char, "
//            + " favorite integer)";
//
//    private static final String CREATE_TABLE_MUSICLIST = "create table "
//            + TABLE_MUSICLIST
//            + " (_id integer primary key autoincrement,"
//            + " songid integer, "
//            + " listid integer)";
//
//    private static final String CREATE_TABLE_LIST = "create table "
//            + TABLE_LIST
//            + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//            + KEY_LISTNAME + " char)";

    public static SQLiteDatabase getInstance(Context context){
        if(mDb == null){
            mDb = getHelper(context).getWritableDatabase();
        }
        return mDb;
    }

    public static DatabaseHelper getHelper(Context context){
        if(mHelper == null){
            mHelper = new DatabaseHelper(context);
        }
        return mHelper;
    }

    public DatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version){
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE_USER_INFO);
        db.execSQL(CREATE_TABLE_FRIEND);
        db.execSQL(CREATE_TABLE_PRIMSG);
        db.execSQL(CREATE_TABLE_TWEETS);

//        ContentValues contentValues = new ContentValues();
//        contentValues.put(KEY_LISTNAME, MusicApp.getContext().getString(R.string.favorite_list_name));
//        db.insert(TABLE_LIST, null, contentValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

    public void deleteTables(Context context){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TWEETS, null, null);
        db.delete(TABLE_PRIMSG, null, null);
        db.delete(TABLE_FRIEND, null, null);
        db.delete(TABLE_USER_INFO, null, null);
    }
}
