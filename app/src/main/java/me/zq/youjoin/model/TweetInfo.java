package me.zq.youjoin.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * YouJoin-Android
 * Created by ZQ on 2015/12/4.
 */
public class TweetInfo {
    /**
     * result : success
     * tweets : [{"friend_id":"16","tweets_id":"14","comment_num":"4","upvote_num":"0","upvote_status":"0","tweets_content":"haha i love mzz!","tweets_img":"http://www.tekbroaden.com/youjoin-server/upload/16/20151206032335_lufei.jpg","tweets_time":"2015-12-06 03:23:35"},{"friend_id":"16","tweets_id":"25","comment_num":"0","upvote_num":"0","upvote_status":"0","tweets_content":"ahahah\n:smile::smile::smiley:","tweets_img":"","tweets_time":"2015-12-15 10:52:34"}]
     */

    private String result;
    /**
     * friend_id : 16
     * tweets_id : 14
     * comment_num : 4
     * upvote_num : 0
     * upvote_status : 0
     * tweets_content : haha i love mzz!
     * tweets_img : http://www.tekbroaden.com/youjoin-server/upload/16/20151206032335_lufei.jpg
     * tweets_time : 2015-12-06 03:23:35
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

    public static class TweetsEntity implements Parcelable {
        private int friend_id;
        private int tweets_id;
        private int comment_num;
        private int upvote_num;
        private int upvote_status;
        private String tweets_content;
        private String tweets_img;
        private String tweets_time;

        public void setFriend_id(int friend_id) {
            this.friend_id = friend_id;
        }

        public void setTweets_id(int tweets_id) {
            this.tweets_id = tweets_id;
        }

        public void setComment_num(int comment_num) {
            this.comment_num = comment_num;
        }

        public void setUpvote_num(int upvote_num) {
            this.upvote_num = upvote_num;
        }

        public void setUpvote_status(int upvote_status) {
            this.upvote_status = upvote_status;
        }

        public void setTweets_content(String tweets_content) {
            this.tweets_content = tweets_content;
        }

        public void setTweets_img(String tweets_img) {
            this.tweets_img = tweets_img;
        }

        public void setTweets_time(String tweets_time) {
            this.tweets_time = tweets_time;
        }

        public int getFriend_id() {
            return friend_id;
        }

        public int getTweets_id() {
            return tweets_id;
        }

        public int getComment_num() {
            return comment_num;
        }

        public int getUpvote_num() {
            return upvote_num;
        }

        public int getUpvote_status() {
            return upvote_status;
        }

        public String getTweets_content() {
            return tweets_content;
        }

        public String getTweets_img() {
            return tweets_img;
        }

        public String getTweets_time() {
            return tweets_time;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.friend_id);
            dest.writeInt(this.tweets_id);
            dest.writeInt(this.comment_num);
            dest.writeInt(this.upvote_num);
            dest.writeInt(this.upvote_status);
            dest.writeString(this.tweets_content);
            dest.writeString(this.tweets_img);
            dest.writeString(this.tweets_time);
        }

        public TweetsEntity() {
        }

        protected TweetsEntity(Parcel in) {
            this.friend_id = in.readInt();
            this.tweets_id = in.readInt();
            this.comment_num = in.readInt();
            this.upvote_num = in.readInt();
            this.upvote_status = in.readInt();
            this.tweets_content = in.readString();
            this.tweets_img = in.readString();
            this.tweets_time = in.readString();
        }

        public static final Parcelable.Creator<TweetsEntity> CREATOR = new Parcelable.Creator<TweetsEntity>() {
            public TweetsEntity createFromParcel(Parcel source) {
                return new TweetsEntity(source);
            }

            public TweetsEntity[] newArray(int size) {
                return new TweetsEntity[size];
            }
        };
    }
}
