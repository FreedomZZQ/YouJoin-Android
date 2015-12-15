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

    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_SEX = "user_sex";
    public static final String USER_BIRTH = "user_birth";
    public static final String USER_LOCATION = "user_location";
    public static final String USER_PHOTO = "user_photo";
    public static final String USER_SIGN = "user_sign";
    public static final String USER_WORK = "user_work";
    public static final String USER_NICKNAME = "user_nickname";
    public static final String FOCUS_NUM = "focus_num";
    public static final String FOLLOW_NUM = "follow_num";

    private static final String CREATE_TABLE_USER_INFO = "create table "
            + TABLE_USER_INFO
            + " (" + USER_ID + " integer primary key, "
            + USER_NAME + " text not null, "
            + USER_EMAIL + " text not null, "
            + USER_SEX + " text not null, "
            + USER_BIRTH + " date not null, "
            + USER_LOCATION + " text not null, "
            + USER_PHOTO + " text not null, "
            + USER_SIGN + " text not null, "
            + USER_WORK + " text not null, "
            + USER_NICKNAME + " text not null, "
            + FOCUS_NUM + " integer not null, "
            + FOLLOW_NUM + " integer not null)";

    public static final String FRIEND_ID = "friend_id";
    public static final String USER1_ID = "user1_id";
    public static final String USER2_ID = "user2_id";
    public static final String FRIEND_TIME = "friend_time";

    private static final String CREATE_TABLE_FRIEND = "create table "
            + TABLE_FRIEND
            + " (" + FRIEND_ID + " integer primary key autoincrement, "
            + USER1_ID + " integer not null, "
            + USER2_ID + " integer not null, "
            + FRIEND_TIME + "timestamp)";

    public static final String TWEETS_ID = "tweets_id";
    public static final String TWEETS_CONTENT = "tweets_content";
    public static final String TWEETS_IMG = "tweets_img";
    public static final String TWEETS_TIME = "tweets_time";
    public static final String COMMENT_NUM = "comment_num";
    public static final String UPVOTE_NUM = "upvote_num";
    public static final String UPVOTE_STATUS = "upvote_status";

    private static final String CREATE_TABLE_TWEETS = "create table "
            + TABLE_TWEETS
            + " (" + TWEETS_ID + " integer primary key, "
            + USER_ID + " integer not null, "
            + TWEETS_CONTENT + " text not null, "
            + TWEETS_IMG + " text, "
            + TWEETS_TIME + " timestamp not null, "
            + COMMENT_NUM + " integer not null, "
            + UPVOTE_NUM + " integer not null, "
            + UPVOTE_STATUS + " integer not null )";

    public static final String PRIMSG_ID = "primsg_id";
    public static final String SENDER_ID = "sender_id";
    public static final String RECEIVER_ID = "receiver_id";
    public static final String PRIMSG_TIME = "primsg_time";
    public static final String PRIMSG_STATUS = "primsg_status";
    public static final String PRIMSG_CONTENT = "primsg_content";

    private static final String CREATE_TABLE_PRIMSG = "create table "
            + TABLE_PRIMSG
            + " (" + PRIMSG_ID + " integer primary key, "
            + SENDER_ID + " integer not null, "
            + RECEIVER_ID + " integer not null, "
            + PRIMSG_TIME + " timestamp not null, "
            + PRIMSG_STATUS + " integer , "
            + PRIMSG_CONTENT + " text not null)";

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
