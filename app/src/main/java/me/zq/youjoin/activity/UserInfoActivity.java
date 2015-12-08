package me.zq.youjoin.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import me.zq.youjoin.R;
import me.zq.youjoin.model.UserInfo;

public class UserInfoActivity extends BaseActivity {

    public static final int TYPE_CURR_USER = 0;
    public static final int TYPE_OTHER_USER = 1;
    public static final String TYPE = "type";
    public static final String USER_INFO = "info";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
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
    @Bind(R.id.user_fab)
    FloatingActionButton userFab;

    int type = 0;
    UserInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        setSupportActionBar(toolbar);

        type = getIntent().getExtras().getInt(TYPE);
        info = getIntent().getParcelableExtra(USER_INFO);

        nickname.setText(info.getNickname());
        email.setText(info.getEmail());
        work.setText(info.getWork());
        sign.setText(info.getUsersign());
        birth.setText(info.getBirth());

        if(info.getSex().equals("0")){
            sex.setBackground(getResources().getDrawable(R.drawable.ic_sex_boy));
        }else{
            sex.setBackground(getResources().getDrawable(R.drawable.ic_sex_girl));
        }

        location.setText(info.getLocation());
        Picasso.with(UserInfoActivity.this)
                .load(info.getImg_url())
                .resize(200, 200)
                .centerCrop()
                .into(avatar);

        if(type == TYPE_CURR_USER){
            userFab.setBackground(getResources().getDrawable(R.drawable.ic_popup_attachment_rename));
        }else if(type == TYPE_OTHER_USER){
            userFab.setBackground(getResources().getDrawable(R.drawable.ic_project_topic_label_add));
        }

        userFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(type == TYPE_CURR_USER){
                    EditUserInfoActivity.actionStart(UserInfoActivity.this);
                }else if(type == TYPE_OTHER_USER){

                }else{
                    Snackbar.make(view, "You Shouldn't see this...", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });
    }


    public static void actionStart(Context context, int type, UserInfo info) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        intent.putExtra(TYPE, type);
        intent.putExtra(USER_INFO, info);
        context.startActivity(intent);
    }

}
