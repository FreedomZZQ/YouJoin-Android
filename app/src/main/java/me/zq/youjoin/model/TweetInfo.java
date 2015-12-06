package me.zq.youjoin.model;

import java.util.List;

/**
 * YouJoin-Android
 * Created by ZQ on 2015/12/4.
 */
public class TweetInfo {
    /**
     * result : success
     * tweets : [{"friend_id":"3","tweets_id":"18","tweets_content":"sadads:heart_eyes::heart_eyes::heart_eyes::heart_eyes::heart_eyes:","tweets_img":"http://110.65.7.55:8088/youjoin-server/upload/3/20151206090328_482f42c5fa484a8abe99ec527fb59b38.jpg;http://110.65.7.55:8088/youjoin-server/upload/3/20151206090328_lufei.jpg;http://110.65.7.55:8088/youjoin-server;"}]
     */

    private String result;
    /**
     * friend_id : 3
     * tweets_id : 18
     * tweets_content : sadads:heart_eyes::heart_eyes::heart_eyes::heart_eyes::heart_eyes:
     * tweets_img : http://110.65.7.55:8088/youjoin-server/upload/3/20151206090328_482f42c5fa484a8abe99ec527fb59b38.jpg;http://110.65.7.55:8088/youjoin-server/upload/3/20151206090328_lufei.jpg;http://110.65.7.55:8088/youjoin-server;
     */

    private List<TweetsEntity> tweets;

    public void setResult(String result) {
        this.result = result;
    }

    public void setTweets(List<TweetsEntity> tweets) {
        this.tweets = tweets;
    }

    public String getResult() {
        return result;
    }

    public List<TweetsEntity> getTweets() {
        return tweets;
    }

    public static class TweetsEntity {
        private String friend_id;
        private String tweets_id;
        private String tweets_content;
        private String tweets_img;

        public void setFriend_id(String friend_id) {
            this.friend_id = friend_id;
        }

        public void setTweets_id(String tweets_id) {
            this.tweets_id = tweets_id;
        }

        public void setTweets_content(String tweets_content) {
            this.tweets_content = tweets_content;
        }

        public void setTweets_img(String tweets_img) {
            this.tweets_img = tweets_img;
        }

        public String getFriend_id() {
            return friend_id;
        }

        public String getTweets_id() {
            return tweets_id;
        }

        public String getTweets_content() {
            return tweets_content;
        }

        public String getTweets_img() {
            return tweets_img;
        }
    }
//
//    /**
//     * tweets_id :
//     * tweets_content :
//     * tweets_img :
//     * [
//     *  {"tweets_id":" " , "tweets_content":"" , "tweets_img":"  "},
//     *  {"tweets_id":" " , "tweets_content":"" , "tweets_img":"  "},
//     *  {"tweets_id":" " , "tweets_content":"" , "tweets_img":"  "}
//     * ]
//     */
//
//    private String tweets_id;
//    private String tweets_content;
//    private String tweets_img;
//
//    public void setTweets_id(String tweets_id) {
//        this.tweets_id = tweets_id;
//    }
//
//    public void setTweets_content(String tweets_content) {
//        this.tweets_content = tweets_content;
//    }
//
//    public void setTweets_img(String tweets_img) {
//        this.tweets_img = tweets_img;
//    }
//
//    public String getTweets_id() {
//        return tweets_id;
//    }
//
//    public String getTweets_content() {
//        return tweets_content;
//    }
//
//    public String getTweets_img() {
//        return tweets_img;
//    }
}
