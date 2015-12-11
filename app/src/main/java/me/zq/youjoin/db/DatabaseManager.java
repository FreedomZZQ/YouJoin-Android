package me.zq.youjoin.db;

import java.util.List;

import me.zq.youjoin.model.UserInfo;

/**
 * YouJoin-Android
 * Created by ZQ on 2015/12/11.
 */
public class DatabaseManager {

    /**向数据库添加用户信息
     * @param info 要添加的用户信息
     */
    public static void addUserInfo(UserInfo info){

    }

    /**向数据库添加多个用户信息
     * @param infos 要添加的用户信息列表
     */
    public static void addUsersInfo(List<UserInfo> infos){

    }

    /**使用userid从数据库获取某用户的信息
     * @param userId 要获取的用户的id
     * @return 获取到的用户信息，result字段为success表示获取成功，failure表示获取失败
     */
    public static UserInfo getUserInfoById(String userId){
        UserInfo info = new UserInfo();

        return info;
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



    private DatabaseManager(){}


}
