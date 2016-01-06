package me.zq.youjoin.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import me.zq.youjoin.DataPresenter;
import me.zq.youjoin.R;
import me.zq.youjoin.YouJoinApplication;
import me.zq.youjoin.adapter.CommentsAdapter;
import me.zq.youjoin.adapter.GridPhotoAdapter;
import me.zq.youjoin.model.CommentInfo;
import me.zq.youjoin.model.ResultInfo;
import me.zq.youjoin.model.TweetInfo;
import me.zq.youjoin.model.UserInfo;
import me.zq.youjoin.network.NetworkManager;
import me.zq.youjoin.utils.StringUtils;
import me.zq.youjoin.widget.enter.AutoHeightGridView;
import me.zq.youjoin.widget.enter.EmojiFragment;
import me.zq.youjoin.widget.enter.EnterEmojiLayout;
import me.zq.youjoin.widget.enter.EnterLayout;

public class TweetDetailActivity extends BaseActivity implements EmojiFragment.EnterEmojiLayout,
        DataPresenter.GetUserInfo, DataPresenter.GetCommentList, DataPresenter.SendComment {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.avatar)
    CircleImageView avatar;
    @Bind(R.id.username)
    TextView nickname;
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
    @Bind(R.id.btnComments)
    ImageButton btnComments;
    @Bind(R.id.comment_count)
    TextView commentCount;
    @Bind(R.id.comments_list)
    ListView commentsList;
    @Bind(R.id.sendmsg)
    ImageButton sendmsg;

    private TweetInfo.TweetsEntity tweetsEntity;
    private static final String INFO = "tweetsEntity";

    private List<CommentInfo.CommentsEntity> commentList = new ArrayList<>();
    private CommentsAdapter adapter;

    EnterEmojiLayout enterLayout;
    EditText msgEdit;
    private boolean mFirstFocus = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);
        ButterKnife.bind(this);
        tweetsEntity = getIntent().getParcelableExtra(INFO);

        initViews();

        //GlobalUtils.setListViewHeightBasedOnChildren(commentsList);
        adapter = new CommentsAdapter(TweetDetailActivity.this, commentList);
        commentsList.setAdapter(adapter);
        DataPresenter.getCommentList(tweetsEntity.getTweets_id(), TweetDetailActivity.this);

//        viewHolder.btnLike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
//                NetworkManager.postUpvoteTweet(Integer.toString(
//                        YouJoinApplication.getCurrUser().getId()),
//                        Integer.toString(dataList.get(location).getTweets_id()),
//                        new ResponseListener<ResultInfo>() {
//                            @Override
//                            public void onErrorResponse(VolleyError volleyError) {
//                                LogUtils.e(TAG, volleyError.toString());
//                            }
//
//                            @Override
//                            public void onResponse(ResultInfo tweetsEntity) {
//                                if(tweetsEntity.getResult().equals(NetworkManager.SUCCESS)){
//                                    viewHolder.btnLike.setChecked(isChecked);
//                                    int k = dataList.get(location).getUpvote_num();
//                                    if(isChecked){
//                                        k++;
//                                    }else{
//                                        k--;
//                                    }
//                                    dataList.get(location).setUpvote_num(k);
//                                    viewHolder.likeCount.setText(Integer.toString(k));
//                                }else{
//                                    viewHolder.btnLike.setChecked(!isChecked);
//                                }
//                            }
//                        });
//
//            }
//        });

        UserInfo userInfo = DataPresenter.requestUserInfoFromCache(tweetsEntity.getFriend_id());
        if (userInfo.getResult().equals(NetworkManager.SUCCESS)
                && userInfo.getImg_url() != null) {
            Picasso.with(YouJoinApplication.getAppContext())
                    .load(StringUtils.getPicUrlList(userInfo.getImg_url()).get(0))
                    .resize(200, 200)
                    .centerCrop()
                    .into(avatar);
            nickname.setText(userInfo.getNickname());
        } else {
            DataPresenter.requestUserInfoById(tweetsEntity.getFriend_id(), TweetDetailActivity.this);
        }

        List<String> urls = StringUtils.getPicUrlList(tweetsEntity.getTweets_img());
        GridPhotoAdapter adapter = new GridPhotoAdapter(YouJoinApplication.getAppContext(), urls);
        gridView.setAdapter(adapter);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
//                this.finish();
            default:
                this.finish();
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.sendmsg)
    protected void sendComment(){
        String content = msgEdit.getText().toString();
        msgEdit.setText("");
        DataPresenter.sendComment(YouJoinApplication.getCurrUser().getId(), tweetsEntity.getTweets_id(),
                content, TweetDetailActivity.this);
    }

    @Override
    public void onSendComment(ResultInfo info){
        if(info.getResult().equals(NetworkManager.SUCCESS)){
            DataPresenter.getCommentList(tweetsEntity.getTweets_id(), TweetDetailActivity.this);
        }
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        likeCount.setText(Integer.toString(tweetsEntity.getUpvote_num()));
        commentCount.setText(Integer.toString(tweetsEntity.getComment_num()));
        content.setText(StringUtils.getEmotionContent(
                YouJoinApplication.getAppContext(), content,
                tweetsEntity.getTweets_content()));
        time.setText(tweetsEntity.getTweets_time());
        if (tweetsEntity.getUpvote_status() == NetworkManager.UPVOTE_STATUS_NO) {
            btnLike.setChecked(false);
        } else {
            btnLike.setChecked(true);
        }

        initEnter();
    }

    @Override
    public void onGetCommentList(CommentInfo info) {
        if (info.getResult().equals(NetworkManager.SUCCESS)) {
            commentList.clear();
            for (CommentInfo.CommentsEntity entity : info.getComments()) {
                commentList.add(entity);
            }
            adapter.notifyDataSetChanged();
        } else {
            //Toast.makeText(TweetDetailActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onGetUserInfo(UserInfo info) {
        if (info.getResult().equals(NetworkManager.SUCCESS)) {
            Picasso.with(YouJoinApplication.getAppContext())
                    .load(StringUtils.getPicUrlList(info.getImg_url()).get(0))
                    .resize(200, 200)
                    .centerCrop()
                    .into(avatar);
            nickname.setText(info.getNickname());
        } else {
            Toast.makeText(TweetDetailActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
        }
    }

    private void initEnter() {
        enterLayout = new EnterEmojiLayout(this, null);
        msgEdit = enterLayout.content;
        enterLayout.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterLayout.popKeyboard();
            }
        });
        enterLayout.content.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (mFirstFocus && hasFocus) {
                    mFirstFocus = false;
                    enterLayout.popKeyboard();
                }
            }
        });

    }

    @Override
    protected void onStop() {
        enterLayout.closeEnterPanel();
        super.onStop();
    }

    @Override
    public EnterLayout getEnterLayout() {
        return enterLayout;
    }


    public static void actionStart(Context context, TweetInfo.TweetsEntity info) {
        Intent intent = new Intent(context, TweetDetailActivity.class);
        intent.putExtra(INFO, info);
        context.startActivity(intent);
    }

}
