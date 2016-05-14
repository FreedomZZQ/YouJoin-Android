package me.zq.youjoin.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import me.zq.youjoin.DataPresenter;
import me.zq.youjoin.R;
import me.zq.youjoin.event.UserInfoUpdateEvent;
import me.zq.youjoin.model.ResultInfo;
import me.zq.youjoin.model.UserInfo;
import me.zq.youjoin.network.NetworkManager;
import me.zq.youjoin.utils.StringUtils;

public class UserInfoActivity extends BaseActivity
        implements DataPresenter.GetUserInfo, DataPresenter.AddFriend {

    public static final int TYPE_CURR_USER = 0;
    public static final int TYPE_OTHER_USER = 1;
    public static final String TYPE = "type";
    public static final String USER_ID = "user_id";
    
    @Bind(R.id.avatar)
    CircleImageView avatar;
    @Bind(R.id.nickname)
    TextView nickname;
    @Bind(R.id.sex)
    ImageView sex;
    @Bind(R.id.email)
    TextView email;
    @Bind(R.id.work)
    TextView work;
    @Bind(R.id.location)
    TextView location;
    @Bind(R.id.sign)
    TextView sign;
    @Bind(R.id.birth)
    TextView birth;

    UserInfo info = new UserInfo();
    @Bind(R.id.follow_num)
    TextView followNum;
    @Bind(R.id.focus_num)
    TextView focusNum;
    @Bind(R.id.btn_follow)
    CheckBox btnFollow;
    @Bind(R.id.curr_user_fab)
    FloatingActionButton currUserFab;
    @Bind(R.id.other_user_fab)
    FloatingActionButton otherUserFab;

    int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UserInfoUpdateEvent event){
        if(type == TYPE_CURR_USER){
            this.info = event.userInfo;
            refreshView();
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

    @Override
    public void onGetUserInfo(UserInfo userInfo) {
        if (userInfo.getResult().equals(NetworkManager.FAILURE)) {
            Toast.makeText(UserInfoActivity.this, getString(R.string.error_network),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        this.info = userInfo;
        refreshView();
    }

    private void refreshView(){
        nickname.setText(info.getNickname());
        email.setText(info.getEmail());
        work.setText(info.getWork());
        sign.setText(info.getUsersign());
        birth.setText(info.getBirth());
        followNum.setText(Integer.toString(info.getFollow_num()));
        focusNum.setText(Integer.toString(info.getFocus_num()));
        btnFollow.setChecked(true);

        if (info.getSex().equals("0")) {
            sex.setBackground(getResources().getDrawable(R.drawable.ic_sex_boy));
        } else {
            sex.setBackground(getResources().getDrawable(R.drawable.ic_sex_girl));
        }

        location.setText(info.getLocation());
        Picasso.with(UserInfoActivity.this)
                .load(StringUtils.getPicUrlList(info.getImg_url()).get(0))
                .resize(200, 200)
                .centerCrop()
                .into(avatar);
    }

    private void initView() {

        type = getIntent().getExtras().getInt(TYPE);
        info.setId(getIntent().getExtras().getInt(USER_ID));

        nickname.setText("");
        email.setText("");
        work.setText("");
        sign.setText("");
        birth.setText("");
        sex.setBackground(getResources().getDrawable(R.drawable.ic_sex_boy));
        location.setText("");
        followNum.setText("");
        focusNum.setText("");
        btnFollow.setChecked(false);

        DataPresenter.requestUserInfoById(info.getId(), UserInfoActivity.this);

        if (type == TYPE_CURR_USER) {
            currUserFab.setVisibility(View.VISIBLE);
            btnFollow.setVisibility(View.GONE);
        } else if (type == TYPE_OTHER_USER) {
            otherUserFab.setVisibility(View.VISIBLE);
            btnFollow.setVisibility(View.VISIBLE);
        }

        // TODO: 2016/4/20 添加显示当前是否已关注的逻辑

        currUserFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditUserInfoActivity.actionStart(UserInfoActivity.this);

            }
        });

        otherUserFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageActivity.actionStart(UserInfoActivity.this, info.getUsername());
            }
        });
    }

    @OnClick(R.id.btn_follow)
    protected void followUser() {
        DataPresenter.addFriend(info.getId(), UserInfoActivity.this);
    }

    @Override
    public void onAddFriend(ResultInfo info) {
        // TODO: 2016/4/20 添加是否成功的显示逻辑
        if (info.getResult().equals(NetworkManager.SUCCESS)) {
            Toast.makeText(UserInfoActivity.this, "Add Friend Success!",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(UserInfoActivity.this, "Add Friend Failure!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public static void actionStart(Context context, int type, int userId) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        intent.putExtra(TYPE, type);
        intent.putExtra(USER_ID, userId);
        context.startActivity(intent);
    }

}
