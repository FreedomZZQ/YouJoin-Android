package me.zq.youjoin.model;

/**
 * YouJoin-Android
 * Created by ZQ on 2015/12/4.
 */
public class TweetInfo {

    /**
     * tweets_id :
     * tweets_content :
     * tweets_img :
     * [
     *  {"tweets_id":" " , "tweets_content":"" , "tweets_img":"  "},
     *  {"tweets_id":" " , "tweets_content":"" , "tweets_img":"  "},
     *  {"tweets_id":" " , "tweets_content":"" , "tweets_img":"  "}
     * ]
     */

    private String tweets_id;
    private String tweets_content;
    private String tweets_img;

    public void setTweets_id(String tweets_id) {
        this.tweets_id = tweets_id;
    }

    public void setTweets_content(String tweets_content) {
        this.tweets_content = tweets_content;
    }

    public void setTweets_img(String tweets_img) {
        this.tweets_img = tweets_img;
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
