package me.zq.youjoin.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import me.zq.youjoin.R;
import me.zq.youjoin.YouJoinApplication;
import me.zq.youjoin.model.UpdateUserInfoResult;
import me.zq.youjoin.model.UserInfo;
import me.zq.youjoin.network.NetworkManager;
import me.zq.youjoin.network.ResponseListener;
import me.zq.youjoin.utils.LogUtils;

public class EditUserInfoActivity extends BaseActivity {

    @Bind(R.id.yj_personal_email)
    TextView yjPersonalEmail;
    @Bind(R.id.yj_personal_username)
    TextView yjPersonalUsername;
    @Bind(R.id.yj_personal_sex)
    EditText yjPersonalSex;
    @Bind(R.id.yj_personal_work)
    EditText yjPersonalWork;
    @Bind(R.id.yj_personal_location)
    EditText yjPersonalLocation;
    @Bind(R.id.yj_personal_birth)
    EditText yjPersonalBirth;
    @Bind(R.id.yj_personal_commit)
    Button yjPersonalCommit;
    @Bind(R.id.yj_personal_choose_photo)
    Button yjPersonalChoosePhoto;
    @Bind(R.id.yj_personal_sign)
    EditText yjPersonalSign;
    @Bind(R.id.yj_personal_userphoto)
    ImageView yjPersonalUserphoto;

    private UserInfo userInfo;
    private String picPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);
        ButterKnife.bind(this);

        userInfo = YouJoinApplication.getCurrUser();

        initViews();
    }

    private void initViews() {
        yjPersonalEmail.setText(userInfo.getEmail());
        yjPersonalUsername.setText(userInfo.getUsername());
        yjPersonalBirth.setText(userInfo.getBirth());
        yjPersonalSex.setText(userInfo.getSex());
        yjPersonalWork.setText(userInfo.getWork());
        yjPersonalLocation.setText(userInfo.getLocation());
        yjPersonalSign.setText(userInfo.getUsersign());

        Picasso.with(EditUserInfoActivity.this)
                .load(userInfo.getImg_url())
                .resize(200, 200)
                .centerCrop()
                .into(yjPersonalUserphoto);




//        yjPersonalCommit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                updateInfo();
//            }
//        });
//
//        yjPersonalChoosePhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MultiImageSelectorActivity.startSelect(EditUserInfoActivity.this, 2, 1,
//                        MultiImageSelectorActivity.MODE_SINGLE);
//            }
//        });
    }

    @OnClick(R.id.yj_personal_choose_photo)
    protected void onCommit(){
        MultiImageSelectorActivity.startSelect(EditUserInfoActivity.this, 2, 1,
                MultiImageSelectorActivity.MODE_SINGLE);
    }

    @OnClick(R.id.yj_personal_commit)
    protected void updateInfo() {
        userInfo.setSex(yjPersonalSex.getText().toString());
        userInfo.setWork(yjPersonalWork.getText().toString());
        userInfo.setLocation(yjPersonalLocation.getText().toString());
        userInfo.setBirth(yjPersonalBirth.getText().toString());
        userInfo.setUsersign(yjPersonalSign.getText().toString());

        NetworkManager.postUpdateUserInfo(userInfo, picPath, new ResponseListener<UpdateUserInfoResult>() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(EditUserInfoActivity.this, "UserInfo Update Fail with Network Error!"
                        , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(UpdateUserInfoResult result) {
                if (result.getResult().equals("success")) {
                    LogUtils.d("hehe", "photo url is : " + result.getImg_url());
                    userInfo.setImg_url(result.getImg_url());
                    Toast.makeText(EditUserInfoActivity.this, "UserInfo Update Successfully!"
                            , Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditUserInfoActivity.this, "UserInfo Update Fail!"
                            , Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> mSelectPath =
                        data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);

                for (String p : mSelectPath) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(p);

                    Picasso.with(EditUserInfoActivity.this)
                            .load(new File(p))
                            .resize(200, 200)
                            .centerCrop()
                            .into(yjPersonalUserphoto);
                    picPath = sb.toString();
                }
            }
        }
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, EditUserInfoActivity.class);
        context.startActivity(intent);
    }
}
