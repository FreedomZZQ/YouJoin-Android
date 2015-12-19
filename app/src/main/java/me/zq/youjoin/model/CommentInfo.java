package me.zq.youjoin.model;

import java.util.List;

/**
 * YouJoin-Android
 * Created by ZQ on 2015/12/15.
 */
public class CommentInfo {

    /**
     * result : success
     * comments : [{"comment_id":"8","friend_id":"17","tweet_id":"14","comment_content":"nice!","comment_time":"2015-12-06 03:42:12"},{"comment_id":"9","friend_id":"17","tweet_id":"14","comment_content":"nice!","comment_time":"2015-12-06 03:46:25"},{"comment_id":"10","friend_id":"17","tweet_id":"14","comment_content":"nice!","comment_time":"2015-12-06 03:47:02"},{"comment_id":"11","friend_id":"17","tweet_id":"14","comment_content":"nice!","comment_time":"2015-12-15 02:49:53"}]
     */

    private String result;
    /**
     * comment_id : 8
     * friend_id : 17
     * tweet_id : 14
     * comment_content : nice!
     * comment_time : 2015-12-06 03:42:12
     */

    private List<CommentsEntity> comments;

    public void setResult(String result) {
        this.result = result;
    }

    public void setComments(List<CommentsEntity> comments) {
        this.comments = comments;
    }

    public String getResult() {
        return result;
    }

    public List<CommentsEntity> getComments() {
        return comments;
    }

    public static class CommentsEntity {
        private int comment_id;
        private int friend_id;
        private int tweet_id;
        private String comment_content;
        private String comment_time;

        public void setComment_id(int comment_id) {
            this.comment_id = comment_id;
        }

        public void setFriend_id(int friend_id) {
            this.friend_id = friend_id;
        }

        public void setTweet_id(int tweet_id) {
            this.tweet_id = tweet_id;
        }

        public void setComment_content(String comment_content) {
            this.comment_content = comment_content;
        }

        public void setComment_time(String comment_time) {
            this.comment_time = comment_time;
        }

        public int getComment_id() {
            return comment_id;
        }

        public int getFriend_id() {
            return friend_id;
        }

        public int getTweet_id() {
            return tweet_id;
        }

        public String getComment_content() {
            return comment_content;
        }

        public String getComment_time() {
            return comment_time;
        }
    }
}
