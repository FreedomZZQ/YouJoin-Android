package me.zq.youjoin.model;

import java.util.List;

/**
 * YouJoin-Android
 * Created by ZQ on 2015/12/4.
 */
public class TweetInfo {
    /**
     * result : success
     * tweets : [{"friend_id":"16","tweets_id":"15","comment_num":"0","upvote_num":"0","upvote_status":"0","tweets_content":"haha i love mzz!","tweets_img":"http://192.168.0.103:8088/youjoin-server/upload/16/20151206033524_lufei.jpg;http://192.168.0.103:8088/youjoin-server;"}]
     */

    private String result;
    /**
     * friend_id : 16
     * tweets_id : 15
     * comment_num : 0
     * upvote_num : 0
     * upvote_status : 0
     * tweets_content : haha i love mzz!
     * tweets_img : http://192.168.0.103:8088/youjoin-server/upload/16/20151206033524_lufei.jpg;http://192.168.0.103:8088/youjoin-server;
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
        private String comment_num;
        private String upvote_num;
        private String upvote_status;
        private String tweets_content;
        private String tweets_img;

        public void setFriend_id(String friend_id) {
            this.friend_id = friend_id;
        }

        public void setTweets_id(String tweets_id) {
            this.tweets_id = tweets_id;
        }

        public void setComment_num(String comment_num) {
            this.comment_num = comment_num;
        }

        public void setUpvote_num(String upvote_num) {
            this.upvote_num = upvote_num;
        }

        public void setUpvote_status(String upvote_status) {
            this.upvote_status = upvote_status;
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

        public String getComment_num() {
            return comment_num;
        }

        public String getUpvote_num() {
            return upvote_num;
        }

        public String getUpvote_status() {
            return upvote_status;
        }

        public String getTweets_content() {
            return tweets_content;
        }

        public String getTweets_img() {
            return tweets_img;
        }
    }
}
