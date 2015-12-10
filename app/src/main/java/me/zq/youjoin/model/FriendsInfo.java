package me.zq.youjoin.model;

import java.util.List;

import me.zq.youjoin.utils.StringUtils;

/**
 * YouJoin-Android
 * Created by ZQ on 2015/12/10.
 */
public class FriendsInfo {
    /**
     * result : success
     * friends : [{"id":"16","nickname":"zzq","img_url":"http://192.168.0.103:8088/youjoin-server/upload/16/20151207053324_lufei.jpg;"}]
     */

    private String result;
    /**
     * id : 16
     * nickname : zzq
     * img_url : http://192.168.0.103:8088/youjoin-server/upload/16/20151207053324_lufei.jpg;
     */

    private List<FriendsEntity> friends;

    public void setResult(String result) {
        this.result = result;
    }

    public void setFriends(List<FriendsEntity> friends) {
        this.friends = friends;
    }

    public String getResult() {
        return result;
    }

    public List<FriendsEntity> getFriends() {
        return friends;
    }

    public static class FriendsEntity {
        private String id;
        private String nickname;
        private String img_url;

        public String getFirstLetter() {
            String letter = StringUtils.getFirstLetters(nickname).toUpperCase().substring(0, 1);
            if(0 <= letter.compareTo("A") && letter.compareTo("Z") <= 0){
                return letter;
            }
            return "#";
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getId() {
            return id;
        }

        public String getNickname() {
            return nickname;
        }

        public String getImg_url() {
            return img_url;
        }
    }
}
