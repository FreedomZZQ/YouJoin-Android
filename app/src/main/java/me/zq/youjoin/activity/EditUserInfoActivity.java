package me.zq.youjoin.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import me.zq.youjoin.DataPresenter;
import me.zq.youjoin.R;
import me.zq.youjoin.YouJoinApplication;
import me.zq.youjoin.model.UpdateUserInfoResult;
import me.zq.youjoin.model.UserInfo;
import me.zq.youjoin.network.NetworkManager;
import me.zq.youjoin.utils.StringUtils;

public class EditUserInfoActivity extends BaseActivity
        implements DataPresenter.UpdateUserInfo {


    @Bind(R.id.edit_avatar)
    LinearLayout editAvatar;
    @Bind(R.id.username)
    TextView username;
    @Bind(R.id.email)
    TextView email;
    @Bind(R.id.edit_nickname)
    EditText editNickname;
    @Bind(R.id.edit_sign)
    EditText editSign;
    @Bind(R.id.sex)
    TextView sex;
    @Bind(R.id.edit_sex)
    LinearLayout editSex;
    @Bind(R.id.edit_work)
    EditText editWork;
    @Bind(R.id.location)
    TextView location;
    @Bind(R.id.edit_location)
    LinearLayout editLocation;
    @Bind(R.id.birth)
    TextView birth;
    @Bind(R.id.edit_birth)
    LinearLayout editBirth;
    @Bind(R.id.edit_container)
    LinearLayout editContainer;
    @Bind(R.id.commit_fab)
    FloatingActionButton commitFab;
    @Bind(R.id.avatar)
    CircleImageView avatar;

    private UserInfo userInfo;
    private String picPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);
        ButterKnife.bind(this);

        userInfo = YouJoinApplication.getCurrUser();

        refreshViews();
        editAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiImageSelectorActivity.startSelect(EditUserInfoActivity.this, 2, 1,
                        MultiImageSelectorActivity.MODE_SINGLE);
            }
        });

        editSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSexChoose();
            }
        });

        editLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        editBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void showSexChoose(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(EditUserInfoActivity.this);
        final String[] sexs = {getString(R.string.sex_man), getString(R.string.sex_woman)};
        builder.setTitle("请选择性别");
        int defaultWhich;
        if(sex.getText().toString().equals(sexs[1])) {
            defaultWhich = 1;
        } else{
            defaultWhich = 0;
        }
        builder.setSingleChoiceItems(sexs, defaultWhich,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sex.setText(sexs[which]);
                    }
                });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void refreshViews() {
        email.setText(userInfo.getEmail());
        username.setText(userInfo.getUsername());
        birth.setText(userInfo.getBirth());
        if(userInfo.getSex().equals("0")){
            sex.setText(getString(R.string.sex_man));
        }else{
            sex.setText(getString(R.string.sex_woman));
        }
        editWork.setText(userInfo.getWork());
        location.setText(userInfo.getLocation());
        editSign.setText(userInfo.getUsersign());
        editNickname.setText(userInfo.getNickname());

        if (userInfo.getImg_url() != null) {
            Picasso.with(EditUserInfoActivity.this)
                    .load(StringUtils.getPicUrlList(userInfo.getImg_url()).get(0))
                    .resize(200, 200)
                    .centerCrop()
                    .into(avatar);
        }
    }


    @OnClick(R.id.commit_fab)
    protected void updateInfo() {
        if(sex.getText().toString().equals(getString(R.string.sex_man))){
            userInfo.setSex("0");
        }else{
            userInfo.setSex("1");
        }
        userInfo.setWork(editWork.getText().toString());
        userInfo.setLocation(location.getText().toString());
        userInfo.setBirth(birth.getText().toString());
        userInfo.setUsersign(editSign.getText().toString());
        userInfo.setNickname(editNickname.getText().toString());
        DataPresenter.updateUserInfo(userInfo, picPath, EditUserInfoActivity.this);

    }

    @Override
    public void onUpdateUserInfo(UpdateUserInfoResult result) {
        if (result.getResult().equals(NetworkManager.SUCCESS)) {
            userInfo.setImg_url(result.getImg_url());
            Toast.makeText(EditUserInfoActivity.this, getString(R.string.update_success)
                    , Toast.LENGTH_SHORT).show();
            EditUserInfoActivity.this.finish();
        } else {
            Toast.makeText(EditUserInfoActivity.this, getString(R.string.error_network)
                    , Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> mSelectPath =
                        data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);

                for (String p : mSelectPath) {
                    Picasso.with(EditUserInfoActivity.this)
                            .load(new File(p))
                            .resize(200, 200)
                            .centerCrop()
                            .into(avatar);
                    picPath = p;
                }
            }
        }
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, EditUserInfoActivity.class);
        context.startActivity(intent);
    }
}
