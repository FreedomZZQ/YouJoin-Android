package me.zq.youjoin.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    /**
     * 获取特定URI的每条线程已经下载的文件长度
     * threadid：代表线程的id
     * downlength:代表线程下载的最后位置
     * downpath:代表当前线程下载的资源
     * @param path
     * @return
     */
    public static Map<Integer, Integer> getDataDownload(String path)
    {
        //获取可读取的数据库句柄，一般情况下在该操作的内部实现中，其返回的其实是可写的数据库句柄
        SQLiteDatabase db = DatabaseHelper.getInstance(YouJoinApplication.getAppContext());
        //建立一个哈希表用于存放每条线程的已经下载的文件长度
        Map<Integer, Integer> data=new HashMap<Integer, Integer>();
        if(db.isOpen()){

            //根据下载路径查询所有线程下载数据，返回的Cursor指向第一条记录之前
            Cursor cursor = db.rawQuery("select " + DatabaseHelper.DOWNLOAD_THREADID + " , "
                    + DatabaseHelper.DOWNLOAD_DOWNLENGTH + " from "
                    + DatabaseHelper.TABLE_DOWNLOAD + " where "
                    + DatabaseHelper.DOWNLOAD_PATH + "=?",new String[]{ path });


            //从第一条记录开始开始遍历Cursor对象
            while(cursor.moveToNext())
            {
                //把线程ID和该线程已下载的长度设置进data哈希表中
                data.put(cursor.getInt(0), cursor.getInt(1));
                data.put(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.DOWNLOAD_THREADID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.DOWNLOAD_DOWNLENGTH)));
            }
            cursor.close();
            db.close();
        }

        return data;
    }
    /**
     * 保存每条线程已经下载的文件长度
     * @param path 下载的路径
     * @param map 现在的ID和已经下载的长度的集合
     */
    public static void saveDownload(String path,Map<Integer, Integer> map){
        //获取可写的数据库句柄
        SQLiteDatabase db = DatabaseHelper.getInstance(YouJoinApplication.getAppContext());
        if(db.isOpen()){
            //开始食物，因为此处要插入多批数据
            db.beginTransaction();
            try{
                for(Map.Entry<Integer, Integer> entry:map.entrySet())
                {
                    //采用for-each的方式遍历数据集合
                    //插入特定下载路径，特定线程ID,已经下载的数据
                    db.execSQL("insert into " + DatabaseHelper.TABLE_DOWNLOAD
                                    + " (" + DatabaseHelper.DOWNLOAD_PATH
                                    + " ," + DatabaseHelper.DOWNLOAD_THREADID
                                    + " ," + DatabaseHelper.DOWNLOAD_DOWNLENGTH
                                    + ") values(?,?,?)",
                            new Object[]{path, entry.getKey(), entry.getValue()});
                }
                db.setTransactionSuccessful();
            }finally{
                db.endTransaction();
            }
            db.close();
        }

    }

    /**
     * 实时更新每条线程已经下载的文件长度
     * @param path
     * @param threadId
     * @param pos
     */
    public static void updateDownload(String path,int threadId,int pos){
        SQLiteDatabase db = DatabaseHelper.getInstance(YouJoinApplication.getAppContext());
        if(db.isOpen()){
            //更新特定下载路径，特定线程，已经下载的文件长度
            db.execSQL("update " + DatabaseHelper.TABLE_DOWNLOAD
                    + " set " + DatabaseHelper.DOWNLOAD_DOWNLENGTH
                    + " =? where " + DatabaseHelper.DOWNLOAD_PATH + " =? "
                    + "and " + DatabaseHelper.DOWNLOAD_THREADID + " =? ",new Object[]{pos,path,threadId});
            db.close();
        }

    }

    /**
     * 当文件下载完成后，删除对应的下载记录
     * @param path
     */
    public static void deleteDownload(String path)
    {
        SQLiteDatabase db = DatabaseHelper.getInstance(YouJoinApplication.getAppContext());
        if(db.isOpen()){
            db.execSQL("delete from " + DatabaseHelper.TABLE_DOWNLOAD
                            + " where " + DatabaseHelper.DOWNLOAD_PATH + " =?",
                    new Object[]{path});
            db.close();
        }

    }

    /**向数据库添加多条私信
     * @param info 要添加的私信
     */
    public static void addPrimsgList(PrimsgInfo info){
        for(PrimsgInfo.MessageEntity entity : info.getMessage()){
            addPrimsg(entity);
        }
    }

    /** 向数据库添加一条私信记录
     * @param entity 要添加的私信对象
     */
    public static void addPrimsg(PrimsgInfo.MessageEntity entity){

        SQLiteDatabase db = DatabaseHelper.getInstance(YouJoinApplication.getAppContext());
        String sql = "select * from "
                + DatabaseHelper.TABLE_PRIMSG
                + " where " + DatabaseHelper.PRIMSG_ID + " = " + Integer.toString(entity.getPrismg_id());
        if(parsePrimsgInfoCursor(db.rawQuery(sql, null)).size() != 0){
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.PRIMSG_ID, entity.getPrismg_id());
        contentValues.put(DatabaseHelper.SENDER_ID, entity.getSender_id());
        contentValues.put(DatabaseHelper.RECEIVER_ID, entity.getReceiver_id());
        contentValues.put(DatabaseHelper.PRIMSG_CONTENT, entity.getContent());
        contentValues.put(DatabaseHelper.PRIMSG_TIME, entity.getReceive_date());
        db.insert(DatabaseHelper.TABLE_PRIMSG, null, contentValues);

    }

    /**获取当前用户与某用户的私信记录
     * @param friendId 某用户id
     * @return 私信记录
     */
    public static PrimsgInfo getPrimsgList(int friendId){
        int userId = YouJoinApplication.getCurrUser().getId();

        String sql = "select * from "
                + DatabaseHelper.TABLE_PRIMSG
                + " where ( " + DatabaseHelper.SENDER_ID + " = " + Integer.toString(userId)
                + " and " + DatabaseHelper.RECEIVER_ID + " = " + Integer.toString(friendId)
                + " ) or ( " + DatabaseHelper.SENDER_ID + " = " + Integer.toString(friendId)
                + " and " + DatabaseHelper.RECEIVER_ID + " = " + Integer.toString(userId) + " ) ";

        return getPrimsgInfo(sql);
    }


    /**当前用户关注friendid
     * @param friendId 要关注的friendid
     */
    public static void addFriendInfo(int friendId){

        String sql = "select *from " + DatabaseHelper.TABLE_FRIEND
                + " where " + DatabaseHelper.USER1_ID
                + " = " + Integer.toString(friendId)
                + " and " + DatabaseHelper.USER2_ID
                + " = " + Integer.toString(YouJoinApplication.getCurrUser().getId());
        SQLiteDatabase db = DatabaseHelper.getInstance(YouJoinApplication.getAppContext());
        if(parseFriendInfoCursor(db.rawQuery(sql, null)).size() != 0){
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.USER1_ID, friendId);
        contentValues.put(DatabaseHelper.USER2_ID, YouJoinApplication.getCurrUser().getId());

        db.insert(DatabaseHelper.TABLE_FRIEND, null, contentValues);

    }

    /**从数据库获取用户关注的列表
     * @param userId 用户Id
     * @return 该用户的关注信息
     */
    public static FriendsInfo getFriendList(int userId){

        String sql = "select * from " + DatabaseHelper.TABLE_FRIEND
                + " where " + DatabaseHelper.USER2_ID
                + " = " + Integer.toString(userId);

        return getFriendsInfo(sql);
    }

    /**向数据库添加或更新用户信息
     * @param info 要添加或更新的用户信息
     */
    public static void addUserInfo(UserInfo info){
        SQLiteDatabase db = DatabaseHelper.getInstance(YouJoinApplication.getAppContext());
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

        if(getUserInfoById(info.getId()).getResult().equals(NetworkManager.SUCCESS)){
            db.update(DatabaseHelper.TABLE_USER_INFO, contentValues,
                    DatabaseHelper.USER_ID + " = ?", new String[] {Integer.toString(info.getId())});
        }else {
            db.insert(DatabaseHelper.TABLE_USER_INFO, null, contentValues);
        }
    }

    /**向数据库添加或更新多个用户信息
     * @param infos 要添加或更新的用户信息列表
     */
    public static void addUsersInfo(List<UserInfo> infos){
        for(UserInfo info : infos){
            addUserInfo(info);
        }
    }


    public static UserInfo getUserInfoAuto(String param){
        String type = StringUtils.getParamType(param);
        switch (type) {
            case NetworkManager.PARAM_TYPE_USER_ID:
                return getUserInfoById(Integer.getInteger(param));
            case NetworkManager.PARAM_TYPE_USER_NAME:
                return getUserInfoByUserName(param);
            case NetworkManager.PARAM_TYPE_USER_EMAIL:
                return getUserInfoByEmail(param);
            default:
                UserInfo info = new UserInfo();
                info.setResult(NetworkManager.FAILURE);
                return info;
        }
    }
    /**使用userid从数据库获取某用户的信息
     * @param userId 要获取的用户的id
     * @return 获取到的用户信息，result字段为success表示获取成功，failure表示获取失败
     */
    public static UserInfo getUserInfoById(int userId){

        String sql = "select * from " + DatabaseHelper.TABLE_USER_INFO
                + " where " + DatabaseHelper.USER_ID + " = " + Integer.toString(userId);

        return getUserInfo(sql);
    }

    /**使用username从数据库获取某用户的信息
     * @param userName 要获取的用户的username
     * @return 获取到的用户信息，result字段为success表示获取成功，failure表示获取失败
     */
    public static UserInfo getUserInfoByUserName(String userName){

        String sql = "select * from " + DatabaseHelper.TABLE_USER_INFO
                + " where " + DatabaseHelper.USER_NAME + " = '" + userName + "'";

        return getUserInfo(sql);
    }


    /**使用email从数据库获取某用户的信息
     * @param email 要获取的用户的 email
     * @return 获取到的用户信息，result字段为success表示获取成功，failure表示获取失败
     */
    public static UserInfo getUserInfoByEmail(String email){
        String sql = "select * from " + DatabaseHelper.TABLE_USER_INFO
                + " where " + DatabaseHelper.USER_EMAIL + " = '" + email + "'";

        return getUserInfo(sql);
    }

    @NonNull
    private static FriendsInfo getFriendsInfo(String sql) {
        SQLiteDatabase db = DatabaseHelper.getInstance(YouJoinApplication.getAppContext());
        List<FriendsInfo.FriendsEntity> results = parseFriendInfoCursor(db.rawQuery(sql, null));

        FriendsInfo info = new FriendsInfo();
        if(results.isEmpty()){
            info.setResult(NetworkManager.FAILURE);
            return info;
        }else{
            info.setFriends(results);
            info.setResult(NetworkManager.SUCCESS);
            return info;
        }
    }

    @NonNull
    private static UserInfo getUserInfo(String sql) {
        UserInfo info = new UserInfo();
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

    @NonNull
    private static PrimsgInfo getPrimsgInfo(String sql) {
        PrimsgInfo info = new PrimsgInfo();
        SQLiteDatabase db = DatabaseHelper.getInstance(YouJoinApplication.getAppContext());
        List<PrimsgInfo.MessageEntity> results = parsePrimsgInfoCursor(db.rawQuery(sql, null));
        if(results.isEmpty()){
            info.setResult(NetworkManager.FAILURE);
            return info;
        }else{
            info.setResult(NetworkManager.SUCCESS);
            info.setMessage(results);
            return info;
        }
    }

    private static List<PrimsgInfo.MessageEntity> parsePrimsgInfoCursor(Cursor cursor){
        List<PrimsgInfo.MessageEntity> list = new ArrayList<>();

        if(cursor.moveToFirst()){
            do{
                PrimsgInfo.MessageEntity info = new PrimsgInfo.MessageEntity();
                info.setPrismg_id(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.PRIMSG_ID)));
                info.setSender_id(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.SENDER_ID)));
                info.setReceiver_id(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.RECEIVER_ID)));
                info.setContent(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PRIMSG_CONTENT)));
                info.setReceive_date(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PRIMSG_TIME)));
                list.add(info);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    private static List<TweetInfo.TweetsEntity> parseTweetInfoCursor(Cursor cursor){
        List<TweetInfo.TweetsEntity> list = new ArrayList<>();

        if(cursor.moveToFirst()){
            do{
                TweetInfo.TweetsEntity info = new TweetInfo.TweetsEntity();
                info.setTweets_id(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TWEETS_ID)));
                info.setFriend_id(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.USER_ID)));
                info.setComment_num(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COMMENT_NUM)));
                info.setUpvote_num(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.UPVOTE_NUM)));
                info.setTweets_content(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TWEETS_CONTENT)));
                info.setTweets_img(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TWEETS_IMG)));

                list.add(info);
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
                list.add(info);
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
