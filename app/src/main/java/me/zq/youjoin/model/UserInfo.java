package me.zq.youjoin.model;

/**
 * Created by ZQ on 2015/11/12.
 */
public class UserInfo {
    /**
     * id : 1
     * username : zzq
     * email : ksudzhan@vip.qq.com
     * result : success
     */

    private String id;
    private String username;
    private String email;
    private String result;

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getResult() {
        return result;
    }

}
