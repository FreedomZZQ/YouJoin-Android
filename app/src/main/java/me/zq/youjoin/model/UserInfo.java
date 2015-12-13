package me.zq.youjoin.model;

import android.os.Parcel;
import android.os.Parcelable;

import me.zq.youjoin.utils.StringUtils;

/**
 * Created by ZQ on 2015/11/12.
 */
public class UserInfo implements Parcelable {

    private int id;
    private String username;
    private String email;
    private String result;
    private String work;
    private String sex;
    private String birth;
    private String location;
    private String img_url;
    private String usersign;
    private int follow_num;
    private int focus_num;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    private String nickname;

    public String getFirstLetter() {
        String letter = StringUtils.getFirstLetters(nickname).toUpperCase().substring(0, 1);
        if(0 <= letter.compareTo("A") && letter.compareTo("Z") <= 0){
            return letter;
        }
        return "#";
    }

    public void setId(int id) {
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

    public int getId() {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.username);
        dest.writeString(this.email);
        dest.writeString(this.result);
        dest.writeString(this.work);
        dest.writeString(this.sex);
        dest.writeString(this.birth);
        dest.writeString(this.location);
        dest.writeString(this.img_url);
        dest.writeString(this.usersign);
        dest.writeString(this.nickname);
    }

    public UserInfo() {
    }

    protected UserInfo(Parcel in) {
        this.id = in.readInt();
        this.username = in.readString();
        this.email = in.readString();
        this.result = in.readString();
        this.work = in.readString();
        this.sex = in.readString();
        this.birth = in.readString();
        this.location = in.readString();
        this.img_url = in.readString();
        this.usersign = in.readString();
        this.nickname = in.readString();
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        public UserInfo createFromParcel(Parcel source) {
            return new UserInfo(source);
        }

        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    public void setFollow_num(int follow_num) {
        this.follow_num = follow_num;
    }

    public void setFocus_num(int focus_num) {
        this.focus_num = focus_num;
    }

    public int getFollow_num() {
        return follow_num;
    }

    public int getFocus_num() {
        return focus_num;
    }
}
