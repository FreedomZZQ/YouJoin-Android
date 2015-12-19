package me.zq.youjoin.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextSwitcher;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import me.zq.youjoin.R;
import me.zq.youjoin.model.TweetInfo;
import me.zq.youjoin.utils.GlobalUtils;
import me.zq.youjoin.widget.enter.AutoHeightGridView;

public class TweetDetailActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.avatar)
    CircleImageView avatar;
    @Bind(R.id.username)
    TextView username;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.content)
    TextView content;
    @Bind(R.id.gridView)
    AutoHeightGridView gridView;
    @Bind(R.id.btnLike)
    CheckBox btnLike;
    @Bind(R.id.like_count)
    TextView likeCount;
    @Bind(R.id.tsLikesCounter)
    TextSwitcher tsLikesCounter;
    @Bind(R.id.btnComments)
    ImageButton btnComments;
    @Bind(R.id.comment_count)
    TextView commentCount;
    @Bind(R.id.comments_list)
    ListView commentsList;
    @Bind(R.id.comment)
    EditText comment;
    @Bind(R.id.send)
    ImageButton send;
    @Bind(R.id.sendText)
    TextView sendText;
    @Bind(R.id.popEmoji)
    CheckBox popEmoji;
    @Bind(R.id.sendmsg)
    ImageButton sendmsg;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.emojiKeyboardIndicator)
    LinearLayout emojiKeyboardIndicator;
    @Bind(R.id.selectEmoji)
    ImageView selectEmoji;
    @Bind(R.id.emojiKeyboardLayout)
    LinearLayout emojiKeyboardLayout;

    private TweetInfo.TweetsEntity info;
    private static final String INFO = "info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        GlobalUtils.setListViewHeightBasedOnChildren(commentsList);

    }

    public static void actionStart(Context context, TweetInfo.TweetsEntity info) {
        Intent intent = new Intent(context, TweetDetailActivity.class);
        intent.putExtra(INFO, info);
        context.startActivity(intent);
    }

}
