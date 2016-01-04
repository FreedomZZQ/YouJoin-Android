package me.zq.youjoin.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import me.zq.youjoin.DataPresenter;
import me.zq.youjoin.R;
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
        initView();

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//
//        }
    }

    @Override
    public void onGetUserInfo(UserInfo userInfo) {
        if (userInfo.getResult().equals(NetworkManager.FAILURE)) {
            Toast.makeText(UserInfoActivity.this, getString(R.string.error_network),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        this.info = userInfo;
        nickname.setText(info.getNickname());
        email.setText(info.getEmail());
        work.setText(info.getWork());
        sign.setText(info.getUsersign());
        birth.setText(info.getBirth());
        followNum.setText(Integer.toString(info.getFollow_num()));
        focusNum.setText(Integer.toString(info.getFocus_num()));
        btnFollow.setChecked(false);

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
        } else if (type == TYPE_OTHER_USER) {
            otherUserFab.setVisibility(View.VISIBLE);
        }

        currUserFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditUserInfoActivity.actionStart(UserInfoActivity.this);

            }
        });

        otherUserFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageActivity.actionStart(UserInfoActivity.this, info);
            }
        });
    }

    @OnClick(R.id.btn_follow)
    protected void followUser() {

    }

    @Override
    public void onAddFriend(ResultInfo info) {
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
