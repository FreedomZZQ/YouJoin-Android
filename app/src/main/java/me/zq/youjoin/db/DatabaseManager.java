package me.zq.youjoin.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import me.zq.youjoin.YouJoinApplication;
import me.zq.youjoin.model.FriendsInfo;
import me.zq.youjoin.model.PrimsgInfo;
import me.zq.youjoin.model.TweetInfo;
import me.zq.youjoin.model.UserInfo;
import me.zq.youjoin.network.NetworkManager;
import me.zq.youjoin.utils.StringUtils;

/**
 * YouJoin-Android
 * Created by ZQ on 2015/12/11.
 */
public class DatabaseManager {

    /**向数据库添加用户信息
     * @param info 要添加的用户信息
     */
    public static void addUserInfo(UserInfo info){
        SQLiteDatabase db = DatabaseHelper.getInstance(YouJoinApplication.getAppContext());
        String sql = "";
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.USER_ID, info.getId());
        contentValues.put(DatabaseHelper.USER_NAME, info.getUsername());
        contentValues.put(DatabaseHelper.USER_EMAIL, info.getEmail());
        contentValues.put(DatabaseHelper.USER_SEX, info.getSex());
        contentValues.put(DatabaseHelper.USER_BIRTH, info.getBirth());
        contentValues.put(DatabaseHelper.USER_LOCATION, info.getLocation());
        contentValues.put(DatabaseHelper.USER_PHOTO, StringUtils.getPicUrlList(info.getImg_url()).get(0));
        contentValues.put(DatabaseHelper.USER_SIGN, info.getUsersign());
        contentValues.put(DatabaseHelper.USER_WORK, info.getWork());
        contentValues.put(DatabaseHelper.USER_NICKNAME, info.getNickname());
        contentValues.put(DatabaseHelper.FOCUS_NUM, info.getFocus_num());
        contentValues.put(DatabaseHelper.FOLLOW_NUM, info.getFollow_num());
        db.insert(DatabaseHelper.TABLE_USER_INFO, null, contentValues);
        db.execSQL(sql);
    }

    /**向数据库添加多个用户信息
     * @param infos 要添加的用户信息列表
     */
    public static void addUsersInfo(List<UserInfo> infos){
        for(UserInfo info : infos){
            addUserInfo(info);
        }
    }

    /**使用userid从数据库获取某用户的信息
     * @param userId 要获取的用户的id
     * @return 获取到的用户信息，result字段为success表示获取成功，failure表示获取失败
     */
    public static UserInfo getUserInfoById(String userId){
        UserInfo info = new UserInfo();

        String sql = "select from " + DatabaseHelper.TABLE_USER_INFO
                + " where " + DatabaseHelper.USER_ID + " = '" + userId + "'";

        SQLiteDatabase db = DatabaseHelper.getInstance(YouJoinApplication.getAppContext());
        List<UserInfo> results = parseUserInfoCursor(db.rawQuery(sql, null));

        if(results.isEmpty()){
            info.setResult(NetworkManager.FAILURE);
            return info;
        }else{
            info = results.get(0);
            info.setResult(NetworkManager.SUCCESS);
            return info;
        }
    }

    /**使用username从数据库获取某用户的信息
     * @param userName 要获取的用户的username
     * @return 获取到的用户信息，result字段为success表示获取成功，failure表示获取失败
     */
    public static UserInfo getUserInfoByUserName(String userName){
        UserInfo info = new UserInfo();

        return info;
    }

    /**使用email从数据库获取某用户的信息
     * @param email 要获取的用户的 email
     * @return 获取到的用户信息，result字段为success表示获取成功，failure表示获取失败
     */
    public static UserInfo getUserInfoByEmail(String email){
        UserInfo info = new UserInfo();

        return info;
    }


    private static List<PrimsgInfo.MessageEntity> parsePrimsgInfoCursor(Cursor cursor){
        List<PrimsgInfo.MessageEntity> list = new ArrayList<>();

        if(cursor.moveToFirst()){
            do{

            }while(cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    private static List<TweetInfo.TweetsEntity> parseTweetInfoCursor(Cursor cursor){
        List<TweetInfo.TweetsEntity> list = new ArrayList<>();

        if(cursor.moveToFirst()){
            do{

            }while(cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    private static List<FriendsInfo.FriendsEntity> parseFriendInfoCursor(Cursor cursor){
        List<FriendsInfo.FriendsEntity> list = new ArrayList<>();

        if(cursor.moveToFirst()){
            do{
                FriendsInfo.FriendsEntity info = new FriendsInfo.FriendsEntity();
                info.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.USER1_ID)));

            }while(cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    private static List<UserInfo> parseUserInfoCursor(Cursor cursor){
        List<UserInfo> list = new ArrayList<>();

        if(cursor.moveToFirst()){
            do{
                UserInfo info = new UserInfo();
                info.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.USER_ID)));
                info.setUsername(cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_NAME)));
                info.setNickname(cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_NICKNAME)));
                info.setEmail(cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_EMAIL)));
                info.setSex(cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_SEX)));
                info.setBirth(cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_BIRTH)));
                info.setLocation(cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_LOCATION)));
                info.setImg_url(cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_PHOTO)));
                info.setUsersign(cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_SIGN)));
                info.setFocus_num(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.FOCUS_NUM)));
                info.setFollow_num(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.FOLLOW_NUM)));
                info.setWork(cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_WORK)));
                list.add(info);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    private DatabaseManager(){}


}
