package me.zq.youjoin.model;

import me.zq.youjoin.utils.StringUtils;

/**
 * Created by ZQ on 2015/11/12.
 */
public class UserInfo {

    private String id;
    private String username;
    private String email;
    private String result;
    private String work;
    private String sex;
    private String birth;
    private String location;
    private String img_url;
    private String usersign;

    public String getFirstLetter() {
        String letter = StringUtils.getFirstLetters(username).toUpperCase().substring(0, 1);
        if(0 <= letter.compareTo("A") && letter.compareTo("Z") <= 0){
            return letter;
        }
        return "#";
    }

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

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public void setUsersign(String usersign) {
        this.usersign = usersign;
    }

    public String getUsersign() {
        return usersign;
    }
}
